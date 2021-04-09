package vip.itchen.support.sequence.generator;

import vip.itchen.service.com.IComUniqueKeyService;
import vip.itchen.support.SpringContextUtil;
import vip.itchen.support.exceptions.DevMsgException;
import vip.itchen.dict.com.UniqueType;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据库自增长序列号
 * @author lhb
 * @date 2020/3/22 1:25
 */
public class UniqueKeyGenerator implements IGenerator {

    private static IComUniqueKeyService comUniqueKeyService;
    private UniqueType uniqueType;

    public UniqueKeyGenerator(UniqueType uniqueType) {
        if (null == uniqueType) {
            throw new DevMsgException("数据库序列号生成，业务类型不能为空");
        }

        this.uniqueType = uniqueType;
        if (null == comUniqueKeyService) {
            comUniqueKeyService = SpringContextUtil.getBean(IComUniqueKeyService.class);
        }
    }

    @Override
    public Long nextId() {
        return comUniqueKeyService.get(uniqueType);
    }

    @Override
    public String nextIdString() {

        if (null == uniqueType.getPadLeftLength() || uniqueType.getPadLeftLength() < 0) {
            throw new DevMsgException("数据库序列号生成，左补0的位数不能为空");
        }

        String prefixStr = StringUtils.trimToEmpty(uniqueType.getPrefix());

        Long seqValue = nextId();
        if (null == seqValue) {
            throw new RuntimeException("Unique Key Sequence get null");
        }
        return prefixStr.concat(StringUtils.leftPad(seqValue.toString(), uniqueType.getPadLeftLength(), '0'));
    }
}
