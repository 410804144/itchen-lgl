package vip.itchen.service.ac;

import vip.itchen.domain.ac.AcUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * AC.用户信息 服务类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface IAcUserService extends IService<AcUser> {

    /**
     * 根据uid获取用户信息
     * @param uid uid
     * @return 用户信息
     */
    AcUser getByUid(Long uid);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    AcUser getByUsername(String username);

    /**
     * 创建用户
     * @param username 用户名
     * @param nickname 昵称
     * @return 用户信息
     */
    AcUser create(String username, String nickname);
}
