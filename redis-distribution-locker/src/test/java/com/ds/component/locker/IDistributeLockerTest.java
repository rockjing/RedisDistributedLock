package com.ds.component.locker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class IDistributeLockerTest {

    @Autowired
    IDistributeLocker iDistributeLocker;

    @Test
    public void lockOnce() {
        boolean unLockSuccessed = iDistributeLocker.unLock("ABC","123");
        Assert.isTrue(unLockSuccessed,"无");


        String key = iDistributeLocker.lock("ABC",20);
        System.out.println(key);
        Assert.notNull(key, "should not be null!");

        key = iDistributeLocker.lock("ABC",20);
        System.out.println(key);
        Assert.isTrue(key==null, "should  be null!");




    }

    @Test
    public void testLock() {
    }
}