package com.demo.learn.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author luwt
 * @date 2021/1/19.
 */
@Component
public class RedisMsgListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(RedisMsgListener.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // key过期时调用
        LOG.info("onPMessage pattern " + Arrays.toString(bytes) + " " + " " + message);
        String channel = new String(message.getChannel());
        String msg = String.valueOf(redisTemplate.getValueSerializer().deserialize(message.getBody()));
        LOG.info("消息是：" + msg);
    }

}
