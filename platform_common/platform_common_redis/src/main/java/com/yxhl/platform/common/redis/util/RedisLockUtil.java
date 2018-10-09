package com.yxhl.platform.common.redis.util;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

/**
 * redis分布式锁.<br>
 * 思路：
 * <pre>
 * 用SETNX命令，SETNX只有在key不存在时才返回成功。这意味着只有一个线程可以成功运行SETNX命令，而其他线程会失败，然后不断重试，直到它们能建立锁。
 * 然后使用脚本来创建锁，因为一个redis脚本同一时刻只能运行一次。
 * 创建锁代码：
 * <code>
-- KEYS[1] key,
-- ARGV[1] value,
-- ARGV[2] expireTimeMilliseconds

if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then 
    redis.call('pexpire', KEYS[1], ARGV[2]) 
    return 1 
else 
    return 0 
end
 * </code>
 * 最后使用脚本来解锁。
 * 解锁代码：
 * 
 * <code>
-- KEYS[1] key,
-- ARGV[1] value
if redis.call("get", KEYS[1]) == ARGV[1]
then
    return redis.call("del", KEYS[1])
else
    return 0
end
 * </code>
 * </pre>
 */
@Component
public class RedisLockUtil {

    private static final Long SUCCESS = 1L;

    // 加锁脚本
    private static final String SCRIPT_LOCK = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('pexpire', KEYS[1], ARGV[2]) return 1 else return 0 end";
    // 解锁脚本
    private static final String SCRIPT_UNLOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    // 加锁脚本sha1值
    private static final String SCRIPT_LOCK_SHA1 = Sha1Util.encrypt(SCRIPT_LOCK);
    // 解锁脚本sha1值
    private static final String SCRIPT_UNLOCK_SHA1 = Sha1Util.encrypt(SCRIPT_UNLOCK);
    
    @SuppressWarnings("rawtypes")
	@Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取分布式锁
     * 
     * @param lockedResource
     *            需要锁定的资源
     * @param privateKey
     *            私钥
     * @param expireTimeMilliseconds
     *            超期时间，多少毫秒后自动释放锁
     * @return 返回true表示拿到锁；false表示该资源已经被其他线程锁定
     */
    @SuppressWarnings("unchecked")
    public boolean lock(final String lockedResource, final String privateKey, final long expireTimeMilliseconds) {
    	//防止重复锁定，每次都更新锁
    	if(redisTemplate.hasKey(lockedResource) && !unlock(lockedResource,privateKey)) {
    		return false;
    	}
    	
        Object result = redisTemplate.execute(new RedisScript<Long>() {
            @Override
            public String getSha1() {
                return SCRIPT_LOCK_SHA1;
            }

            @Override
            public Class<Long> getResultType() {
                return Long.class;
            }

            @Override
            public String getScriptAsString() {
                return SCRIPT_LOCK;
            }

        }, Collections.singletonList(lockedResource),// KEYS[1]
                privateKey, // ARGV[1]
                expireTimeMilliseconds // ARGV[2]
                );

        return SUCCESS.equals(result);
    }

    /**
     * 释放分布式锁
     * 
     * @param lockedResource
     *            锁定的资源
     * @param privateKey
     *            私钥
     * @return 返回true表示释放锁成功
     */
    @SuppressWarnings("unchecked")
    public boolean unlock(String lockedResource, String privateKey) {
        Object result = redisTemplate.execute(new RedisScript<Long>() {
            @Override
            public String getSha1() {
                return SCRIPT_UNLOCK_SHA1;
            }

            @Override
            public Class<Long> getResultType() {
                return Long.class;
            }

            @Override
            public String getScriptAsString() {
                return SCRIPT_UNLOCK;
            }

        }, Collections.singletonList(lockedResource), privateKey);

        return SUCCESS.equals(result);
    }
}
