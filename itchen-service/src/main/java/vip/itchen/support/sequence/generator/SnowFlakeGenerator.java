package vip.itchen.support.sequence.generator;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import lombok.extern.slf4j.Slf4j;

/**
 * 雪花算法序列号
 * @author lhb
 * @date 2020/3/12 18:53
 */
@Slf4j
public class SnowFlakeGenerator implements IGenerator {

    private static final Sequence SEQUENCE = new Sequence();

    @Override
    public Long nextId() {
        return SEQUENCE.nextId();
    }

    @Override
    public String nextIdString() {
        return String.valueOf(SEQUENCE.nextId());
    }
}
