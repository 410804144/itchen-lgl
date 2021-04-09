package vip.itchen.support.sequence.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis生成序列号的订单类型
 * 根据该类型分开采集序列号
 * @author lhb
 */
@Getter
@AllArgsConstructor
public enum RedisSeqBizType {
    PO_ITEM_CODE( "商品编号", RedisSeqFormat.yyMMddHHmm, 5),
    ;

    private final String desc;

    /**
     * 时间格式
     */
    private final RedisSeqFormat format;

    /**
     * 统一左补0的位数
     */
    private final Integer padLeftLength;
}
