package vip.itchen.service.ac;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.itchen.dict.ac.BlackType;
import vip.itchen.domain.ac.AcUserBlack;

/**
 * <p>
 * AC.黑名单 服务类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface IAcUserBlackService extends IService<AcUserBlack> {

    /**
     * 获取黑名单信息
     * @param uid uid
     * @param blackType 黑名单类型
     * @return 黑名单信息
     */
    AcUserBlack getByUid(Long uid, BlackType blackType);

    /**
     * 判断是否被拉黑
     * @param uid uid
     * @param blackType 黑名单类型
     * @return 是否被拉黑
     */
    boolean isBlack(Long uid, BlackType blackType);
}
