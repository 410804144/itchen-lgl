package vip.itchen.service.ac.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.itchen.dict.com.CommonStatus;
import vip.itchen.domain.ac.AcUser;
import vip.itchen.mapper.ac.AcUserMapper;
import vip.itchen.service.ac.IAcUserService;
import vip.itchen.support.sequence.SeqConfigure;
import vip.itchen.support.sequence.dict.HashIdsBizType;
import vip.itchen.support.sequence.dict.SeqType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * AC.用户信息 服务实现类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Service
public class AcUserServiceImpl extends ServiceImpl<AcUserMapper, AcUser> implements IAcUserService {

    @Override
    public AcUser getByUid(Long uid) {
        if (Objects.equals(uid, 0L)) {
            return null;
        }
        return lambdaQuery()
                .eq(AcUser::getUid, uid)
                .last("limit 1")
                .orderByAsc(AcUser::getId)
                .one();
    }

    @Override
    public AcUser getByUsername(String username) {
        return lambdaQuery()
                .eq(AcUser::getUsername, username)
                .last("limit 1")
                .orderByAsc(AcUser::getId)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcUser create(String username, String nickname) {
        AcUser user = new AcUser();
        user.setUid(0L);
        user.setUsername(username);
        user.setNickname(nickname);
        user.setUserStatus(CommonStatus.ENABLE.name());
        user.setCreateTime(new Date());
        user.setModifyTime(new Date());
        baseMapper.insert(user);

        Long uid = createUid(user.getId());
        // 生成uid
        lambdaUpdate()
                .eq(AcUser::getId, user.getId())
                .set(AcUser::getUid, uid)
                .set(AcUser::getModifyTime, new Date())
                .update();

        return getByUid(uid);
    }

    /**
     * 生成uid
     * @param id id
     * @return uid
     */
    private static Long createUid(Integer id) {
        String uidStr = SeqConfigure.builder()
                .seqType(SeqType.HASH_IDS)
                .hashIdsBizType(HashIdsBizType.UID_CODE)
                .hashIdsInputValue(id.longValue())
                .build()
                .getStringId();

        long uid = new BigInteger(uidStr, 16).longValue();
        return uid ^ Integer.MAX_VALUE - 2048;
    }
}
