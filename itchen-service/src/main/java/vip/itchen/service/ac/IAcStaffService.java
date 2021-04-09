package vip.itchen.service.ac;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.itchen.domain.ac.AcStaff;

/**
 * <p>
 * AC.员工信息 服务类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface IAcStaffService extends IService<AcStaff> {

    /**
     * 根据uid获取员工信息
     * @param sid sid
     * @return 员工信息
     */
    AcStaff getBySid(Long sid);

    /**
     * 根据员工名获取员工信息
     * @param username 员工名
     * @return 员工信息
     */
    AcStaff getByUsername(String username);

    /**
     * 创建员工
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @param staffName 员工名字
     * @return 员工信息
     */
    void create(String username, String password, String nickname, String staffName);
}
