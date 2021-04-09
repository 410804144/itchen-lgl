package vip.itchen.service.ac.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import vip.itchen.dict.ac.PasswordType;
import vip.itchen.domain.ac.AcUserPassword;
import vip.itchen.mapper.ac.AcUserPasswordMapper;
import vip.itchen.service.ac.IAcUserPasswordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * AC.用户密码 服务实现类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Service
public class AcUserPasswordServiceImpl extends ServiceImpl<AcUserPasswordMapper, AcUserPassword> implements IAcUserPasswordService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public AcUserPassword getByUid(Long uid, PasswordType passwordType) {
        return lambdaQuery()
                .eq(AcUserPassword::getUid, uid)
                .eq(AcUserPassword::getPasswordType, passwordType)
                .last("limit 1")
                .orderByAsc(AcUserPassword::getId)
                .one();
    }

    @Override
    public void createOrModifyPassword(Long uid, PasswordType passwordType, String password) {
        AcUserPassword userPassword = getByUid(uid, passwordType);
        if (null == userPassword) {
            userPassword = new AcUserPassword();
            userPassword.setUid(uid);
            userPassword.setPasswordType(passwordType.name());
            userPassword.setPasswordValue(passwordEncoder.encode(password));
            userPassword.setCreateTime(new Date());
            userPassword.setModifyTime(new Date());
            baseMapper.insert(userPassword);
        } else {
            userPassword.setPasswordType(passwordEncoder.encode(password));
            userPassword.setModifyTime(new Date());
            baseMapper.updateById(userPassword);
        }
    }
}
