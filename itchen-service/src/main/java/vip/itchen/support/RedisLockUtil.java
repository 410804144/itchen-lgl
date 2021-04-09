package vip.itchen.support;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;

/**
 * @author alabimofa
 * @date 2020/8/14
 */
@Component
@Slf4j
public class RedisLockUtil {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 解锁脚本，原子操作
     */
    private static final String UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 尝试获取分布式锁
     * @param lockKey KYE
     * @param requestId 唯一请求标识
     * @param expireTime 过期时间
     * @return true:获取锁成功 false:获取失败
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, Duration expireTime) {
        if (StringUtils.isBlank(lockKey) || StringUtils.isBlank(requestId)) {
            return false;
        }
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime);
        return null == result ? false : result;
    }

    /**
     * 等待获取分布式锁
     * @param lockKey KEY
     * @param requestId 唯一请求标识
     * @return true：等待OK false:等待NG
     */
    public boolean wait4Lock(String lockKey, String requestId) {
        return wait4Lock(lockKey, requestId, Duration.ofSeconds(30L), 13, 2000L);
    }

    /**
     * 等待获取分布式锁
     * @param lockKey KEY
     * @param requestId 唯一请求标识
     * @param expireSecond 过期时间
     * @param retryTimes 重试次数
     * @param waitMillis 重试等待毫秒数
     * @return true：等待OK false:等待NG
     */
    public boolean wait4Lock(String lockKey, String requestId, Duration expireSecond, Integer retryTimes, Long waitMillis) {
        int waitTimes = 0;
        while (waitTimes <= retryTimes) {
            boolean lock = tryGetDistributedLock(lockKey, requestId, expireSecond);
            if (lock) {
                return true;
            }
            waitTimes = waitTimes + 1;
            ThreadUtil.sleep(waitMillis);
        }
        return false;
    }

    /**
     * 解锁
     * @param lockKey KEY
     * @param requestId 唯一请求标识
     * @return true:解锁成功 false:解锁失败
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>(UNLOCK_SCRIPT, Boolean.class);
        Boolean result = redisTemplate.execute(
                redisScript,
                redisTemplate.getValueSerializer(),
                new Jackson2JsonRedisSerializer<>(Boolean.class),
                Collections.singletonList(lockKey),
                requestId);
        return null == result ? false : result;
    }
}
