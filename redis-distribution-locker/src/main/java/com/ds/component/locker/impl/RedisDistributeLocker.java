package com.ds.component.locker.impl;

import com.ds.component.locker.IDistributeLocker;
import com.ds.component.locker.constants.LockResult;
import com.ds.component.locker.IRedisClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.ds.component.locker.util.ResourceFileUtil.readFile;

/**
 * redis 分布式锁的简单实现
 * @author xiongyx
 */
@Component("redisIDistributeLocker")
public final class RedisDistributeLocker implements IDistributeLocker {

    /**
     * 无限重试
     * */
    public static final int UN_LIMIT_RETRY_COUNT = -1;

//    /**
//     * 初始化lua脚本
//     */
//    private RedisDistributeLocker() {
//        try {
//            ResourceFileUtil.initLockScript();
//            ResourceFileUtil.initUnLockScript();
//        } catch (IOException e) {
//            throw new RuntimeException("LuaScript init error!",e);
//        }
//    }

//    /**
//     * 持有锁 成功标识
//     * */
//    private static final Long ADD_LOCK_SUCCESS = 1L;
//    /**
//     * 释放锁 失败标识
//     * */
//    private static final Long RELEASE_LOCK_SUCCESS = 1L;

    /**
     * 默认过期时间 单位：秒
     * */
    private static final int DEFAULT_EXPIRE_TIME_SECOND = 300;
    /**
     * 默认加锁重试时间 单位：毫秒
     * */
    private static final int DEFAULT_RETRY_FIXED_TIME = 100;
    /**
     * 默认的加锁浮动时间区间 单位：毫秒
     * */
    private static final int DEFAULT_RETRY_TIME_RANGE = 10;
    /**
     * 默认的加锁重试次数
     * */
    private static final int DEFAULT_RETRY_COUNT = 30;

    /**
     * 加锁脚本 lock.lua
     * 1. 判断key是否存在
     * 2. 如果存在，判断requestID是否相等
     * 相等，则删除掉key重新创建新的key值，重置过期时间
     * 不相等，说明已经被抢占，加锁失败，返回null
     * 3. 如果不存在，说明恰好已经过期，重新生成key
     */
    private final static String LUA_LOCK_SCRIPT;
    private final static String LUA_UNLOCK_SCRIPT;

    static {

        try {
            LUA_LOCK_SCRIPT = readFile("lock.lua");
            LUA_UNLOCK_SCRIPT = readFile("unlock.lua");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Resource
    private IRedisClient iRedisClient;



    @Override
    public String lock(String lockKey) {
        String uuid = UUID.randomUUID().toString();

        return lock(lockKey,uuid);
    }

    @Override
    public String lock(String lockKey, int expireTime) {
        String uuid = UUID.randomUUID().toString();

        return lock(lockKey,uuid,expireTime);
    }

    @Override
    public String lock(String lockKey, String requestId) {
        return lock(lockKey, requestId,DEFAULT_EXPIRE_TIME_SECOND);
    }

    @Override
    public String lock(String lockKey, String requestId, int expireTime) {
        List<String> keyList = Collections.singletonList(lockKey);

        List<String> argsList = Arrays.asList(
                requestId,
                expireTime + ""
        );
        if(LockResult
                .getResultByResultCode( iRedisClient.eval(LUA_LOCK_SCRIPT, keyList, argsList)) == LockResult.OPERATION_OK)
        {
            return requestId;
        }
        else
        {
            return null;
        }
//        Long result = (Long) iRedisClient.eval(LUA_LOCK_SCRIPT, keyList, argsList);
//
//        if(result.equals(ADD_LOCK_SUCCESS)){
//            return requestID;
//        }else{
//            return null;
//        }
    }

    @Override
    public String lockAndRetry(String lockKey) {
        String uuid = UUID.randomUUID().toString();

        return lockAndRetry(lockKey,uuid);
    }

    @Override
    public String lockAndRetry(String lockKey, String requestId) {
        return lockAndRetry(lockKey,requestId,DEFAULT_EXPIRE_TIME_SECOND);
    }

    @Override
    public String lockAndRetry(String lockKey, int expireTime) {
        String uuid = UUID.randomUUID().toString();

        return lockAndRetry(lockKey,uuid,expireTime);
    }

    @Override
    public String lockAndRetry(String lockKey, int expireTime, int retryCount) {
        String uuid = UUID.randomUUID().toString();

        return lockAndRetry(lockKey,uuid,expireTime,retryCount);
    }

    @Override
    public String lockAndRetry(String lockKey, String requestID, int expireTime) {
        return lockAndRetry(lockKey,requestID,expireTime,DEFAULT_RETRY_COUNT);
    }

    @Override
    public String lockAndRetry(String lockKey, String requestId, int expireTime, int retryCount) {
        if(retryCount <= 0){
            // retryCount小于等于0 无限循环，一直尝试加锁
            while(true){
                String result = lock(lockKey,requestId,expireTime);
                if(result != null){
                    return result;
                }

                // 休眠一会
                sleepSomeTime();
            }
        }else{
            // retryCount大于0 尝试指定次数后，退出
            for(int i=0; i<retryCount; i++){
                String result = lock(lockKey,requestId,expireTime);
                if(result != null){
                    return result;
                }

                // 休眠一会
                sleepSomeTime();
            }

            return null;
        }
    }

    @Override
    public boolean unLock(String lockKey, String requestId) {
        List<String> keyList = Collections.singletonList(lockKey);

        List<String> argsList = Collections.singletonList(requestId);

//        Object result = iRedisClient.eval(LUA_UNLOCK_SCRIPT, keyList, argsList);
//
//        // 释放锁成功
//        return RELEASE_LOCK_SUCCESS.equals(result);

        Object result = iRedisClient.eval(LUA_UNLOCK_SCRIPT, keyList, argsList);
        LockResult lockResult = LockResult.getResultByResultCode(result);
        return lockResult != LockResult.OPERATION_NOK;
    }



    /**
     * 获得最终的获得锁的重试时间
     * */
    private int getFinallyGetLockRetryTime(){
        Random ra = new Random();

        // 最终重试时间 = 固定时间 + 浮动时间
        return DEFAULT_RETRY_FIXED_TIME + ra.nextInt(DEFAULT_RETRY_TIME_RANGE);
    }

    /**
     * 当前线程 休眠一段时间
     * */
    private void sleepSomeTime(){
        // 重试时间 单位：毫秒
        int retryTime = getFinallyGetLockRetryTime();
        try {
            Thread.sleep(retryTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("redis锁重试时，出现异常",e);
        }
    }
}
