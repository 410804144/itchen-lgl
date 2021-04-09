package vip.itchen.manager.ac;

import vip.itchen.model.req.ac.LoginReq;
import vip.itchen.model.req.ac.RegisterReq;
import vip.itchen.model.resp.ac.LoginResp;

/**
 * @author ckh
 * @date 2021-04-09
 */
public interface IApiUserManager {

    /**
     * 注册
     * @param req 参数
     */
    void register(RegisterReq req);

    /**
     * 登陆
     * @param req 参数
     * @return 用户信息
     */
    LoginResp login(LoginReq req);
}
