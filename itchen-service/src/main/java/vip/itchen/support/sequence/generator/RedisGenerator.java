package vip.itchen.support.sequence.generator;

import cn.hutool.core.util.StrUtil;
import vip.itchen.conts.RedisConst;
import vip.itchen.support.DateUtil;
import vip.itchen.support.SpringContextUtil;
import vip.itchen.support.exceptions.DevMsgException;
import vip.itchen.support.sequence.dict.RedisSeqBizType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis生成序列号
 * @author lhb
 * @date 2020/3/12 19:38
 */
public class RedisGenerator implements IGenerator {

    private static StringRedisTemplate stringRedisTemplate;

    private final String key;
    private final Long expireSeconds;
    private final String dateFormat;
    private final Integer padLeftLength;

    public RedisGenerator(RedisSeqBizType type) {
        if (null == type) {
            throw new DevMsgException("Redis序列号生成，业务类型不能为空");
        }

        if (StringUtils.isBlank(type.getFormat().getFormat())) {
            throw new DevMsgException("Redis序列号生成，时间格式不能为空");
        }

        if (null == type.getFormat().getExpireSeconds() || type.getFormat().getExpireSeconds() <= 0) {
            throw new DevMsgException("Redis序列号生成，失效秒数不能为空");
        }

        if (null == type.getPadLeftLength() || type.getPadLeftLength() < 0) {
            throw new DevMsgException("Redis序列号生成，左补0的位数不能为空");
        }

        this.key = RedisConst.SEQ_UNIQUE_KEY.concat(StrUtil.toString(type.ordinal())).concat(StrUtil.COLON);
        this.expireSeconds = type.getFormat().getExpireSeconds();
        this.dateFormat = type.getFormat().getFormat();
        this.padLeftLength = type.getPadLeftLength();

        if (null == stringRedisTemplate) {
            stringRedisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
        }
    }

    @Override
    public Long nextId() {
        String seqStr = nextIdString();
        long seq = NumberUtils.toLong(seqStr);
        if (seq <= 0) {
            throw new RuntimeException("Redis Sequence Long value get null");
        }
        return seq;
    }

    @Override
    public String nextIdString() {
        String date = DateUtil.getDateFormat(new Date(), dateFormat);
        Long seq = stringRedisTemplate.opsForValue().increment(key.concat(date), 1L);
        stringRedisTemplate.expire(key.concat(date), expireSeconds, TimeUnit.SECONDS);
        if (null == seq) {
            throw new RuntimeException("Redis Sequence get null");
        }
        return date.concat(StringUtils.leftPad(seq.toString(), padLeftLength, '0'));
    }
}
