package vip.itchen.support.sequence.generator;

import vip.itchen.support.ToolUtil;
import vip.itchen.support.exceptions.DevMsgException;

/**
 * UUID序列号
 * @author lhb
 * @date 2020/3/12 18:49
 */
public class UuidGenerator implements IGenerator {

    @Override
    public Long nextId() {
        throw new DevMsgException("Uuid不能生成Long类型的序列号");
    }

    @Override
    public String nextIdString() {
        return ToolUtil.getUuidString();
    }
}
