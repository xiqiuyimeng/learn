package com.demo.learn.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author luwt
 * @date 2021/1/19.
 * 需要redis服务端开启配置，notify-keyspace-events "" -> notify-keyspace-events Ex
 */
//@Configuration
public class PubSubConfiguration {

    @Autowired
    private RedisMsgListener redisMsgListener;

    @Bean
    public ChannelTopic expiredTopic() {
        // __keyevent@0__:expired 订阅过期队列固定写法，0代表是序号为0的库
        return new ChannelTopic("__keyevent@0__:expired");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Autowired RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
//        redisMessageListenerContainer.addMessageListener(redisMsgListener, expiredTopic());
        return redisMessageListenerContainer;
    }


}
