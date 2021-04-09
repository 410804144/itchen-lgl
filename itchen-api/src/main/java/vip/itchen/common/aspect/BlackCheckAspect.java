package vip.itchen.common.aspect;

import vip.itchen.common.annotation.BlackCheck;
import vip.itchen.common.config.jwt.JwtHolder;
import vip.itchen.service.ac.IAcUserBlackService;
import vip.itchen.support.exceptions.BizMsgException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 黑名单切面
 * @author ckh
 * @date 2021-04-09
 */
@Component
@Aspect
@Scope
@Order(1)
@Slf4j
public class BlackCheckAspect {

    @Resource
    private IAcUserBlackService acUserBlackService;

    @Around("@annotation(blackCheck)")
    public Object around(ProceedingJoinPoint joinPoint, BlackCheck blackCheck) throws Throwable {
        if (acUserBlackService.isBlack(JwtHolder.currentUser().getUid(), blackCheck.blackType())) {
            // 您已被限制使用该功能
            throw new BizMsgException("E.100005");
        } else {
            return joinPoint.proceed();
        }
    }
}
