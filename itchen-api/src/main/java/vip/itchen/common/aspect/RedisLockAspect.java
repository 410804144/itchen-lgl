package vip.itchen.common.aspect;

import cn.hutool.core.util.StrUtil;
import vip.itchen.common.annotation.RedisLock;
import vip.itchen.common.config.jwt.JwtHolder;
import vip.itchen.support.RedisLockUtil;
import vip.itchen.support.ToolUtil;
import vip.itchen.support.exceptions.BizMsgException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * redis锁切面
 * @author ckh
 * @date 2021-04-09
 */
@Component
@Aspect
@Scope
@Order(1)
@Slf4j
public class RedisLockAspect {

    @Resource
    private RedisLockUtil redisLockUtil;

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {

        String lockKey = redisLock.value();
        if (StringUtils.isBlank(lockKey)) {
            lockKey = joinPoint.getSignature().getName();
        }
        if (redisLock.keySuffixWithUid()) {
            lockKey = lockKey.concat(StringUtils.stripToEmpty(StrUtil.toString(JwtHolder.currentUid())));
        }
        String requestId = ToolUtil.getUuidString();
        boolean result = redisLockUtil.tryGetDistributedLock(lockKey, requestId, Duration.ofSeconds(redisLock.lockSeconds()));

        try {
            if (result) {
                return joinPoint.proceed();
            } else {
                throw new BizMsgException("common.00015");
            }
        } finally {
            if (result) {
                redisLockUtil.releaseDistributedLock(lockKey, requestId);
            }
        }
    }
}
