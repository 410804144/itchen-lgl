package vip.itchen.manager.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.itchen.common.constant.Const;
import vip.itchen.conts.ConfigConst;
import vip.itchen.conts.RedisConst;
import vip.itchen.dict.com.CommonStatus;
import vip.itchen.dict.com.OffOrOn;
import vip.itchen.domain.ac.AcStaff;
import vip.itchen.manager.IAdminUserManager;
import vip.itchen.model.req.ac.AdminLoginReq;
import vip.itchen.model.req.ac.AdminRegisterReq;
import vip.itchen.model.resp.AdminLoginResp;
import vip.itchen.service.ac.IAcStaffService;
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
public class AdminUserManagerImpl implements IAdminUserManager {

    @Value("${jwt-secret}")
    private String jwtSecret;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RedisToolUtil redisToolUtil;
    @Resource
    private IComConfigService comConfigService;
    @Resource
    private IAcStaffService acStaffService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(AdminRegisterReq req) {
        String value = comConfigService.getByConfigType(ConfigConst.TYPE_STAFF, ConfigConst.STAFF_REGISTER).getVarcharValue1();
        if (!OffOrOn.ON.name().equals(value)) {
            // 注册功能已关闭
            throw new BizMsgException("E.110001");
        }
        AcStaff staff = acStaffService.getByUsername(req.getUsername());
        if (null != staff) {
            // 用户名已经被注册
            throw new BizMsgException("E.110002");
        }

        acStaffService.create(
                req.getUsername(),
                req.getPassword(),
                req.getNickname(),
                req.getStaffName()
        );
    }

    @Override
    public AdminLoginResp login(AdminLoginReq req) {
        AcStaff staff = acStaffService.getByUsername(req.getUsername());
        if (null == staff) {
            // 用户名或密码错误
            throw new BizMsgException("E.110003");
        }

        if (!passwordEncoder.matches(req.getPassword(), staff.getPassword())) {
            // 用户名或密码错误
            throw new BizMsgException("E.110003");
        }

        if (!CommonStatus.ENABLE.name().equals(staff.getStaffStatus())) {
            // 用户已被禁止登陆
            throw new BizMsgException("E.110004");
        }

        // 生成token
        String token = Const.COMMON_TOKEN_PREFIX + JwtTokenUtil.generateToken(staff.getSid().toString(), jwtSecret);
        AdminLoginResp resp = new AdminLoginResp(staff, token);

        // 存储到redis
        redisToolUtil.set(RedisConst.API_LOGIN_TOKEN_KEY.concat(resp.getSid().toString()), resp, JwtTokenUtil.JWT_TOKEN_VALIDITY + 60000, TimeUnit.MILLISECONDS);

        return resp;
    }
}
