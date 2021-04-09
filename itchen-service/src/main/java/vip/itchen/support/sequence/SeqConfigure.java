package vip.itchen.support.sequence;

import vip.itchen.support.exceptions.DevMsgException;
import vip.itchen.support.sequence.dict.HashIdsBizType;
import vip.itchen.support.sequence.dict.RedisSeqBizType;
import vip.itchen.support.sequence.dict.SeqType;
import vip.itchen.dict.com.UniqueType;
import vip.itchen.support.sequence.generator.*;
import lombok.Builder;

/**
 * @author lhb
 * @date 2020/3/12 17:04
 */
@Builder
public class SeqConfigure {

    private final SeqType seqType;

    private final RedisSeqBizType redisSeqBizType;

    private final HashIdsBizType hashIdsBizType;
    private final Long hashIdsInputValue;

    private final UniqueType uniqueType;

    public Long getNumberId() {
        IGenerator generator = getGenerator();
        return generator.nextId();
    }

    public String getStringId() {
        return getGenerator().nextIdString();
    }

    private IGenerator getGenerator() {
        IGenerator generator;
        switch (seqType) {
            case UUID:
                generator = new UuidGenerator();
                break;
            case REDIS:
                generator = new RedisGenerator(redisSeqBizType);
                break;
            case SNOWFLAKE:
                generator = new SnowFlakeGenerator();
                break;
            case HASH_IDS:
                generator = new HashIdsGenerator(hashIdsBizType, hashIdsInputValue);
                break;
            case UNIQUE_KEY:
                generator = new UniqueKeyGenerator(uniqueType);
                break;
            default:
                throw new DevMsgException("不支持的序列号生成类型:".concat(seqType.name()));
        }
        return generator;
    }
}
