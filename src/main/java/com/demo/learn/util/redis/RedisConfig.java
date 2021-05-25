package com.demo.learn.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//@Configuration
@Slf4j
public class RedisConfig {

    private JedisPool jedisPool = null;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public JedisPool redisPoolFactory()  throws Exception{
        log.info("JedisPool注入成功！！");
        log.info("redis地址：" + host + ":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(true);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        return jedisPool;
    }

    /**
     * 从连接池获取一个可用连接
     * @return
     */
    public Jedis getJedisInstance() {
        Jedis jedis = null;
        // 失败重试次数
        int frequency = 3;
        while (frequency-- > 0) {
            try {
                log.info("try to get Resource...");
                jedis = jedisPool.getResource();
                log.info("get Resource done!");
            } catch (Exception e) {
                log.error("got an exception:", e);
                log.error("retry times " + (3 - frequency) + ":");
                continue;
            }
            return jedis;
        }

        return jedis;
    }


    /**
     * 释放jedis资源
     * @param jedis
     */
    public void returnResource(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
                log.info("return the jedis connection");
            } catch (Exception e) {
                log.error("return jedis connection error: " + e.getMessage());
            }
        }
    }


}
