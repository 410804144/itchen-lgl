package vip.itchen.service.com.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vip.itchen.domain.com.ComConfig;
import vip.itchen.mapper.com.ComConfigMapper;
import vip.itchen.service.com.IComConfigService;
import vip.itchen.support.exceptions.BizMsgException;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * COM.配置 服务实现类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Service
public class ComConfigServiceImpl extends ServiceImpl<ComConfigMapper, ComConfig> implements IComConfigService {

    @Override
    @Cacheable(cacheNames = "com:config:", key = "#root.caches[0].name + #configType + ':' + #configKey")
    public ComConfig getByConfigType(String configType, String configKey) {
        return baseMapper.selectOne(
                new QueryWrapper<ComConfig>()
                        .lambda()
                        .eq(ComConfig::getConfigType, configType)
                        .eq(ComConfig::getConfigKey, configKey)
                        .orderByAsc(ComConfig::getId)
                        .last("limit 1")
        );
    }

    @Override
    @CacheEvict(cacheNames = "com:config:", key = "#root.caches[0].name + #configType + ':' + #configKey")
    public void modifyBigint(String configType, String configKey, Long bigValue1, Long bigValue2, Long bigValue3) {
        ComConfig config = checkAndGetConfig(configType, configKey);
        lambdaUpdate()
                .eq(ComConfig::getId, config.getId())
                .set(ComConfig::getBigintValue1, null == bigValue1 ? 0L : bigValue1)
                .set(ComConfig::getBigintValue2, null == bigValue2 ? 0L : bigValue2)
                .set(ComConfig::getBigintValue3, null == bigValue3 ? 0L : bigValue3)
                .set(ComConfig::getModifyTime, new Date())
                .update();
    }

    @Override
    @CacheEvict(cacheNames = "com:config:", key = "#root.caches[0].name + #configType + ':' + #configKey")
    public void modifyDecimal(String configType, String configKey, BigDecimal decimalValue1, BigDecimal decimalValue2, BigDecimal decimalValue3) {
        ComConfig config = checkAndGetConfig(configType, configKey);
        lambdaUpdate()
                .eq(ComConfig::getId, config.getId())
                .set(ComConfig::getDecimalValue1, null == decimalValue1 ? BigDecimal.ZERO : decimalValue1)
                .set(ComConfig::getDecimalValue2, null == decimalValue2 ? BigDecimal.ZERO : decimalValue2)
                .set(ComConfig::getDecimalValue3, null == decimalValue3 ? BigDecimal.ZERO : decimalValue3)
                .set(ComConfig::getModifyTime, new Date())
                .update();
    }

    @Override
    @CacheEvict(cacheNames = "com:config:", key = "#root.caches[0].name + #configType + ':' + #configKey")
    public void modifyInt(String configType, String configKey, Integer intValue1, Integer intValue2, Integer intValue3) {
        ComConfig config = checkAndGetConfig(configType, configKey);
        lambdaUpdate()
                .eq(ComConfig::getId, config.getId())
                .set(ComConfig::getIntValue1, null == intValue1 ? 0 : intValue1)
                .set(ComConfig::getIntValue2, null == intValue2 ? 0 : intValue2)
                .set(ComConfig::getIntValue3, null == intValue3 ? 0 : intValue3)
                .set(ComConfig::getModifyTime, new Date())
                .update();
    }

    @Override
    @CacheEvict(cacheNames = "com:config:", key = "#root.caches[0].name + #configType + ':' + #configKey")
    public void modifyVarchar(String configType, String configKey, String varcharValue1, String varcharValue2, String varcharValue3) {
        ComConfig config = checkAndGetConfig(configType, configKey);
        lambdaUpdate()
                .eq(ComConfig::getId, config.getId())
                .set(ComConfig::getVarcharValue1, null == varcharValue1 ? "" : varcharValue1)
                .set(ComConfig::getVarcharValue2, null == varcharValue2 ? "" : varcharValue2)
                .set(ComConfig::getVarcharValue3, null == varcharValue3 ? "" : varcharValue3)
                .set(ComConfig::getModifyTime, new Date())
                .update();
    }

    /**
     * 判断配置是否存在，如果存在，则返回配置
     * @param configType 配置类型
     * @param configKey 配置key
     * @return 配置
     */
    private ComConfig checkAndGetConfig(String configType, String configKey) {
        ComConfig config = lambdaQuery()
                .eq(ComConfig::getConfigType, configType)
                .eq(ComConfig::getConfigKey, configKey)
                .orderByAsc(ComConfig::getId)
                .last("limit 1")
                .one();
        if (null == config) {
            // 配置信息不存在
            throw new BizMsgException("E.410074");
        }
        return config;
    }
}
