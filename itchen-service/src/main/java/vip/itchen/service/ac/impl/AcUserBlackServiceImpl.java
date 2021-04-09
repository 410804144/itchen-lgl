package vip.itchen.service.ac.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vip.itchen.dict.ac.BlackType;
import vip.itchen.domain.ac.AcUserBlack;
import vip.itchen.mapper.ac.AcUserBlackMapper;
import vip.itchen.service.ac.IAcUserBlackService;

import java.util.Date;

/**
 * <p>
 * AC.黑名单 服务实现类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Service
public class AcUserBlackServiceImpl extends ServiceImpl<AcUserBlackMapper, AcUserBlack> implements IAcUserBlackService {

    @Override
    public AcUserBlack getByUid(Long uid, BlackType blackType) {
        return lambdaQuery()
                .eq(AcUserBlack::getUid, uid)
                .eq(AcUserBlack::getBlackType, blackType)
                .last("limit 1")
                .orderByAsc(AcUserBlack::getId)
                .one();
    }

    @Override
    @Cacheable(cacheNames = "ac:black:", key = "#root.caches[0].name + #uid + ':' + #blackType")
    public boolean isBlack(Long uid, BlackType blackType) {
        AcUserBlack userBlack = getByUid(uid, blackType);
        String currentDate = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        return null != userBlack && currentDate.compareTo(userBlack.getExpireDate()) <= 0;
    }
}
