package vip.itchen.common.annotation;

import java.lang.annotation.*;

/**
 * redis锁
 * @author ckh
 * @date 2021-04-09
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    /**
     * 锁资源的Key
     */
    String value() default "";

    /**
     * 锁持续的最长秒数
     */
    long lockSeconds() default 30;

    /**
     * 是否添加用户uid到Key后缀
     */
    boolean keySuffixWithUid() default false;
}
