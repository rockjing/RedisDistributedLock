package com.ds.component.locker.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JedisConfigTest {

    @Autowired
    JedisConfig jedisConfig;
    @Test
    public void testConfigIsOK() {

        Assert.notNull(jedisConfig.getHost(),"should not null!");
        Assert.notNull(jedisConfig.getTimeout(),"should not null!");


        Assert.isTrue(jedisConfig.getMaxIdle()>0,"should not null!");


    }
}