package vip.itchen.service.ac;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.itchen.dict.ac.PasswordType;
import vip.itchen.domain.ac.AcUserPassword;

/**
 * <p>
 * AC.用户密码 服务类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface IAcUserPasswordService extends IService<AcUserPassword> {

    /**
     * 获取用户密码
     * @param uid uid
     * @param passwordType 密码类型
     * @return 用户密码
     */
    AcUserPassword getByUid(Long uid, PasswordType passwordType);

    /**
     * 创建或者修改密码
     * @param uid uid
     * @param passwordType 密码类型
     * @param password 密码
     */
    void createOrModifyPassword(Long uid, PasswordType passwordType, String password);
}
