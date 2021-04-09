package vip.itchen.common.constant;

import org.springframework.http.HttpHeaders;

/**
 * @author ckh
 * @date 2021-04-09
 */
public class Const {

    /**
     * jwt凭证起始内容
     */
    public static final String COMMON_TOKEN_PREFIX = "Bearer ";

    /**
     * jwt在header中的key
     */
    public static final String COMMON_TOKEN_KEY = HttpHeaders.AUTHORIZATION;

    /**
     * 多语言在header中的key
     */
    public static final String COMMON_LANGUAGE_KEY = HttpHeaders.ACCEPT_LANGUAGE;
}
