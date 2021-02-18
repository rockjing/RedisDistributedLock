package com.ds.component.locker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiongyx
 * on 2019/5/18.
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class JedisConfig {

    public String host;

    public int port;

    public int database;

    public String timeout;


    @Value("${spring.redis.jedis.pool.max-idle}")
    public int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    public int minIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    public int maxActive;
    @Value("${spring.redis.jedis.pool.max-wait}")
    public String maxWait;



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public String getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}