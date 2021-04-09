package vip.itchen.service.com.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vip.itchen.domain.com.ComUniqueKey;
import vip.itchen.mapper.com.ComUniqueKeyMapper;
import vip.itchen.service.com.IComUniqueKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.itchen.dict.com.UniqueType;

/**
 * <p>
 * COM.自增序列号 服务实现类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Service
public class ComUniqueKeyServiceImpl extends ServiceImpl<ComUniqueKeyMapper, ComUniqueKey> implements IComUniqueKeyService {

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Long get(UniqueType uniqueType) {
        ComUniqueKey uniqueKey = baseMapper.lockByType(uniqueType.name());
        if (null == uniqueKey) {
            uniqueKey = new ComUniqueKey();
            uniqueKey.setUniqueType(uniqueType.name());
            uniqueKey.setUniqueValue(1L);
            baseMapper.insert(uniqueKey);
        } else {
            uniqueKey.setUniqueValue(uniqueKey.getUniqueValue() + 1);
            baseMapper.updateById(uniqueKey);
        }
        return uniqueKey.getUniqueValue();
    }
}
