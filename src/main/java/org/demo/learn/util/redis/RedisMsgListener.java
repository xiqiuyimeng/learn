package org.demo.learn.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

/**
 * @author luwt
 * @date 2021/1/19.
 */
@Slf4j
//@Component
public class RedisMsgListener implements MessageListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // key过期时调用
        log.info("onPMessage pattern " + Arrays.toString(bytes) + " " + " " + message);
        String channel = new String(message.getChannel());
        String msg = String.valueOf(redisTemplate.getValueSerializer().deserialize(message.getBody()));
        log.info("消息是：" + msg);
    }

}
