package com.demo.learn.service.impl;

import com.demo.learn.dao.GoodMapper;
import com.demo.learn.model.Good;
import com.demo.learn.service.GoodService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
//import org.redisson.Redisson;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
//    @Autowired
//    private RedissonClient redisson;

    @Override
    public Good getGood(Integer id) {
        return goodMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addGood(Good good) {
        good.setCreateTime(new Date());
        good.setUpdateTime(new Date());
        goodMapper.insertSelective(good);
    }

    @Override
    public void deleteGood(Integer id) {
        goodMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void editGood(Good good) {
        good.setUpdateTime(new Date());
        goodMapper.updateByPrimaryKeySelective(good);
    }

    @Override
    public List<Good> getGoodList() {
        return goodMapper.getGoodList();
    }

    @Override
    public boolean buy(String goodName) {
        return buyRedis(goodName);
    }

    @Override
    public void resetStore(Integer store) {
        goodMapper.resetStore(store);
    }

    /**
     * 普通形式
     * @param goodName
     * @return
     */
    public boolean buyNormal(String goodName) {
        return buyDB(goodName);
    }

    /**
     * 加同步锁形式
     * @param goodName
     * @return
     */
    public boolean buySync(String goodName) {
        synchronized (this) {
            return buyDB(goodName);
        }
    }

    /**
     * 使用redis作为分布式锁形式
     * @param goodName
     * @return
     */
    public boolean buyRedis(String goodName) {
        String lockId = UUID.randomUUID().toString();
        // 如果不存在就添加，添加成功视为加锁成功，加锁的时候，value设为唯一id，用以在释放时判断，以免释放错
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(goodName, lockId, 10, TimeUnit.SECONDS);
        if (lock) {
            // 获取锁成功，开启子线程，自动给锁续时间，使用定时器，每隔三分之一过期时间进行续期，续期为过期时间
            new Thread(() -> {
                String lockValue = redisTemplate.opsForValue().get(goodName);
                // 如果锁不存在了，直接返回
                if (StringUtils.isBlank(lockValue)) {
                    return;
                }
                // 如果是自己的锁，续时间
                if (lockId.equals(lockValue)) {
                    redisTemplate.expire(goodName, 10, TimeUnit.SECONDS);
                }
            }).start();
            try {
                return buyDB(goodName);
            } finally {
                // 释放锁
                if (lockId.equals(redisTemplate.opsForValue().get(goodName))) {
                    redisTemplate.delete(goodName);
                }
            }
        } else {
            // 没有获取到锁，那么自旋获取锁
            while (true) {

            }
        }
    }

    /**
     * 使用redisson分布式锁形式
     * @param goodName
     * @return
     */
//    public boolean buyRedisson(String goodName) {
//        // redisson 实现分布式锁
//        RLock redissonLock = redisson.getLock(goodName);
//        redissonLock.lock(10, TimeUnit.SECONDS);
//        try {
//            return buyDB(goodName);
//        } finally {
//            redissonLock.unlock();
//        }
//    }

    /**
     * 具体抢购，与数据库交互逻辑
     * @param goodName
     * @return
     */
    private boolean buyDB(String goodName) {
        Good good = goodMapper.getGoodStore(goodName);
        if (good.getStore() > 0) {
            log.info("当前库存量为：{}", good.getStore());
            good.setStore(good.getStore() - 1);
            this.editGood(good);
            return true;
        }
        return false;
    }


}