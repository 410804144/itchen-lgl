package vip.itchen.dict.com;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 由数据库存储序列号，通过行锁的功能实现序列号自增长
 * @author ckh
 * @date 2021-04-09
 */
@Getter
@AllArgsConstructor
public enum UniqueType {
    ;

    /**
     * 业务类型描述
     */
    private final String desc;

    /**
     * 左补0的位数（仅返回字符串时有效）
     */
    private final Integer padLeftLength;

    /**
     * 序列号前缀（仅返回字符串时有效）
     */
    private final String prefix;
}
