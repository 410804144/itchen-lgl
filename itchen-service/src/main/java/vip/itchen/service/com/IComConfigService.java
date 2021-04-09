package vip.itchen.service.com;

import vip.itchen.domain.com.ComConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * COM.配置 服务类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface IComConfigService extends IService<ComConfig> {

    /**
     * 根据配置类型、key1获取配置
     * @param configType 配置类型
     * @param configKey 配置key
     * @return 配置
     */
    ComConfig getByConfigType(String configType, String configKey);

    /**
     * 配置bigint字段
     * @param configType 配置类型
     * @param configKey 配置key
     * @param bigValue1 值1
     * @param bigValue2 值2
     * @param bigValue3 值3
     */
    void modifyBigint(String configType, String configKey, Long bigValue1, Long bigValue2, Long bigValue3);

    /**
     * 配置decimal字段
     * @param configType 配置类型
     * @param configKey 配置key
     * @param decimalValue1 值1
     * @param decimalValue2 值2
     * @param decimalValue3 值3
     */
    void modifyDecimal(String configType, String configKey, BigDecimal decimalValue1, BigDecimal decimalValue2, BigDecimal decimalValue3);

    /**
     * 配置int字段
     * @param configType 配置类型
     * @param configKey 配置key
     * @param intValue1 值1
     * @param intValue2 值2
     * @param intValue3 值3
     */
    void modifyInt(String configType, String configKey, Integer intValue1, Integer intValue2, Integer intValue3);

    /**
     * 配置varchar字段
     * @param configType 配置类型
     * @param configKey 配置key
     * @param varcharValue1 值1
     * @param varcharValue2 值2
     * @param varcharValue3 值3
     */
    void modifyVarchar(String configType, String configKey, String varcharValue1, String varcharValue2, String varcharValue3);
}
