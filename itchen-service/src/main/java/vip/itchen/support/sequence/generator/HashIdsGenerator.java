package vip.itchen.support.sequence.generator;

import vip.itchen.support.exceptions.DevMsgException;
import vip.itchen.support.sequence.dict.HashIdsBizType;

/**
 * @author lhb
 * @date 2020/3/13 23:16
 */
public class HashIdsGenerator implements IGenerator {

    private HashIdsBizType type;
    private Long inputValue;

    public HashIdsGenerator(HashIdsBizType type, Long inputValue) {
        if (null == type) {
            throw new DevMsgException("HashIds算法必须指定类型");
        }
        this.type = type;
        this.inputValue = inputValue;
    }

    @Override
    public Long nextId() {
        throw new DevMsgException("HashIds算法不支持生成数值型ID");
    }

    @Override
    public String nextIdString() {
        return type.getHashids().encode(inputValue);
    }
}
