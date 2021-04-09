package vip.itchen.service.ac.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.itchen.dict.com.CommonStatus;
import vip.itchen.domain.ac.AcStaff;
import vip.itchen.mapper.ac.AcStaffMapper;
import vip.itchen.service.ac.IAcStaffService;
import vip.itchen.support.sequence.SeqConfigure;
import vip.itchen.support.sequence.dict.HashIdsBizType;
import vip.itchen.support.sequence.dict.SeqType;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * AC.员工信息 服务实现类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Service
public class AcStaffServiceImpl extends ServiceImpl<AcStaffMapper, AcStaff> implements IAcStaffService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public AcStaff getBySid(Long sid) {
        if (Objects.equals(sid, 0L)) {
            return null;
        }
        return lambdaQuery()
                .eq(AcStaff::getSid, sid)
                .last("limit 1")
                .orderByAsc(AcStaff::getId)
                .one();
    }

    @Override
    public AcStaff getByUsername(String username) {
        return lambdaQuery()
                .eq(AcStaff::getUsername, username)
                .last("limit 1")
                .orderByAsc(AcStaff::getId)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(String username, String password, String nickname, String staffName) {
        AcStaff staff = new AcStaff();
        staff.setSid(0L);
        staff.setUsername(username);
        staff.setPassword(passwordEncoder.encode(password));
        staff.setNickname(nickname);
        staff.setStaffName(StrUtil.nullToEmpty(staffName));
        staff.setStaffStatus(CommonStatus.ENABLE.name());
        staff.setCreateTime(new Date());
        staff.setModifyTime(new Date());
        baseMapper.insert(staff);

        Long sid = createSid(staff.getId());
        // 生成uid
        lambdaUpdate()
                .eq(AcStaff::getId, staff.getId())
                .set(AcStaff::getSid, sid)
                .set(AcStaff::getModifyTime, new Date())
                .update();
    }

    /**
     * 生成sid
     * @param id id
     * @return sid
     */
    private static Long createSid(Integer id) {
        String uidStr = SeqConfigure.builder()
                .seqType(SeqType.HASH_IDS)
                .hashIdsBizType(HashIdsBizType.SID_CODE)
                .hashIdsInputValue(id.longValue())
                .build()
                .getStringId();

        long uid = new BigInteger(uidStr, 16).longValue();
        return uid ^ Integer.MAX_VALUE - 2048;
    }
}
