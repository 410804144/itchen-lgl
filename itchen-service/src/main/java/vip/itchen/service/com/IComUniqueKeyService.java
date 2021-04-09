package vip.itchen.service.com;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.itchen.domain.com.ComUniqueKey;
import vip.itchen.dict.com.UniqueType;

/**
 * <p>
 * COM.自增序列号 服务类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface IComUniqueKeyService extends IService<ComUniqueKey> {

    /**
     * 根据类型进行序列号自增长
     * @param type 类型
     * @return 自增长序列号
     */
    Long get(UniqueType type);
}
