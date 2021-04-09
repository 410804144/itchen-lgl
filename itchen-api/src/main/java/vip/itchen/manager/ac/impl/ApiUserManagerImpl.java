package vip.itchen.manager.ac.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.itchen.common.constant.Const;
import vip.itchen.conts.ConfigConst;
import vip.itchen.conts.RedisConst;
import vip.itchen.dict.ac.PasswordType;
import vip.itchen.dict.com.CommonStatus;
import vip.itchen.dict.com.OffOrOn;
import vip.itchen.domain.ac.AcUser;
import vip.itchen.domain.ac.AcUserPassword;
import vip.itchen.manager.ac.IApiUserManager;
import vip.itchen.model.req.ac.LoginReq;
import vip.itchen.model.req.ac.RegisterReq;
import vip.itchen.model.resp.ac.LoginResp;
import vip.itchen.service.ac.IAcUserPasswordService;
import vip.itchen.service.ac.IAcUserService;
import vip.itchen.service.com.IComConfigService;
import vip.itchen.support.JwtTokenUtil;
import vip.itchen.support.RedisToolUtil;
import vip.itchen.support.exceptions.BizMsgException;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Service
public class ApiUserManagerImpl implements IApiUserManager {

    @Value("${jwt-secret}")
    private String jwtSecret;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RedisToolUtil redisToolUtil;
    @Resource
    private IComConfigService comConfigService;
    @Resource
    private IAcUserService acUserService;
    @Resource
    private IAcUserPasswordService acUserPasswordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterReq req) {
        String value = comConfigService.getByConfigType(ConfigConst.TYPE_USER, ConfigConst.USER_REGISTER).getVarcharValue1();
        if (!OffOrOn.ON.name().equals(value)) {
            // 注册功能已关闭
            throw new BizMsgException("E.100001");
        }
        AcUser user = acUserService.getByUsername(req.getUsername());
        if (null != user) {
            // 用户名已经被注册
            throw new BizMsgException("E.100002");
        }

        user = acUserService.create(req.getUsername(), req.getNickname());
        acUserPasswordService.createOrModifyPassword(user.getUid(), PasswordType.LOGIN, req.getPassword());
    }

    @Override
    public LoginResp login(LoginReq req) {
        AcUser user = acUserService.getByUsername(req.getUsername());
        if (null == user) {
            // 用户名或密码错误
            throw new BizMsgException("E.100003");
        }

        AcUserPassword userPassword = acUserPasswordService.getByUid(user.getUid(), PasswordType.LOGIN);
        if (null == userPassword || !passwordEncoder.matches(req.getPassword(), userPassword.getPasswordValue())) {
            // 用户名或密码错误
            throw new BizMsgException("E.100003");
        }

        if (!CommonStatus.ENABLE.name().equals(user.getUserStatus())) {
            // 用户已被禁止登陆
            throw new BizMsgException("E.100004");
        }

        // 生成token
        String token = Const.COMMON_TOKEN_PREFIX + JwtTokenUtil.generateToken(user.getUid().toString(), jwtSecret);
        LoginResp resp = new LoginResp(user, token);

        // 存储到redis
        redisToolUtil.set(RedisConst.API_LOGIN_TOKEN_KEY.concat(resp.getUid().toString()), resp, JwtTokenUtil.JWT_TOKEN_VALIDITY + 60000, TimeUnit.MILLISECONDS);

        return resp;
    }
}
