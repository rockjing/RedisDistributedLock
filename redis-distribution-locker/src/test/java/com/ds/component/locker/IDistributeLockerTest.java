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
    public void lock() {
        boolean unLockSuccessed = iDistributeLocker.unLock("ABC","123");
        Assert.isTrue(unLockSuccessed,"释放的时候，尽管没有存在的locker，但仍然是成功的");
        String key = iDistributeLocker.lock("ABC",20);
        Assert.notNull(key, "should not be null!");
        System.out.println(key);

    }

    @Test
    public void testLock() {
    }
}