package vip.itchen.common.annotation;

import vip.itchen.dict.ac.BlackType;

import java.lang.annotation.*;

/**
 * 黑名单校验
 * @author ckh
 * @date 2021-04-09
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BlackCheck {

    /**
     * 黑名单类型
     */
    BlackType blackType();
}
