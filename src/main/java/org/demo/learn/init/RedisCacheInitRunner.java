package org.demo.learn.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.demo.learn.dao.UserMapper;
import org.demo.learn.model.User;
import org.demo.learn.util.gson.GsonUtil;
import org.demo.learn.util.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luwt
 * @date 2021/3/26.
 */
@Slf4j
//@Component
public class RedisCacheInitRunner implements CommandLineRunner {

    private static final String REDIS_TEMPLATE_KEY = "template";

    private static final String REDIS_JEDIS_KEY = "jedis";

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    RedisConfig redisConfig;
    @Autowired
    UserMapper userMapper;

    @Override
    public void run(String... args) throws Exception {
//        List<User> userList = userMapper.getUserList();
//        useTemplate(userList);
//        useJedis(userList);
    }

    private void useTemplate(List<User> userList){
        log.info("template开始缓存数据");
        if (CollectionUtils.isNotEmpty(userList)) {
            Map<String, String> userMap = userList.stream().collect(Collectors.toMap(u -> String.valueOf(u.getUserId()), u -> GsonUtil.toJson(u)));
            log.info("数据：{}", userMap);
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                    redisOperations.opsForHash().putAll((K) REDIS_TEMPLATE_KEY, userMap);
                    return null;
                }
            });
        }
        log.info("template缓存数据完毕");
    }

    private void useJedis(List<User> userList) {
        log.info("jedis开始缓存数据");
        if (CollectionUtils.isNotEmpty(userList)) {
            Jedis jedis = redisConfig.getJedisInstance();
            Pipeline pipeline = jedis.pipelined();
            userList.forEach(u -> pipeline.hmset(REDIS_JEDIS_KEY, new HashMap(){{put(String.valueOf(u.getUserId()), GsonUtil.toJson(u));}}));
            pipeline.sync();
        }
        log.info("jedis缓存数据完毕");
    }
}
