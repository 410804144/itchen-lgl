package vip.itchen.conts;

/**
 * @author ckh
 * @date 2021-04-09
 */
public interface RedisConst {

    /**
     * 缓存序列号Key
     */
    String SEQ_UNIQUE_KEY = "seq:uk:";

    /**
     * API登录token
     */
    String API_LOGIN_TOKEN_KEY = "api:login:token:";

    /**
     * ADMIN登陆token
     */
    String ADMIN_LOGIN_TOKEN_KEY = "admin:login:token:";
}
