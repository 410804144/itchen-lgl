package vip.itchen.manager;

import vip.itchen.model.req.ac.AdminLoginReq;
import vip.itchen.model.req.ac.AdminRegisterReq;
import vip.itchen.model.resp.AdminLoginResp;

/**
 * @author ckh
 * @date 2021-04-09
 */
public interface IAdminUserManager {

    /**
     * 注册
     * @param req 参数
     */
    void register(AdminRegisterReq req);

    /**
     * 登陆
     * @param req 参数
     * @return 用户信息
     */
    AdminLoginResp login(AdminLoginReq req);
}
