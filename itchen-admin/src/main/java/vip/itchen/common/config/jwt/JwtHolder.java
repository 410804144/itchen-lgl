package vip.itchen.common.config.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import vip.itchen.model.resp.AdminLoginResp;

/**
 * @author ckh
 * @date 2021-04-09
 */
public class JwtHolder {

    /**
     * 获取登录用户SID
     */
    public static Long currentSid() {
        return currentUser().getSid();
    }

    /**
     * 获取登录用户信息
     */
    public static AdminLoginResp currentUser() {
        return (AdminLoginResp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
