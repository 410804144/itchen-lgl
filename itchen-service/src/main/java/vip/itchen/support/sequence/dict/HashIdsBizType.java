package vip.itchen.support.sequence.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hashids.Hashids;

/**
 * HashIds算法属性配置
 * @author lhb
 * @date 2020/3/13 23:09
 */
@AllArgsConstructor
@Getter
public enum HashIdsBizType {
    UID_CODE("UID编码", new Hashids("ITCHEN_UID_SECRET", 7, "1234567890ABCDEF")),
    SID_CODE("UID编码", new Hashids("ITCHEN_SID_SECRET", 7, "1234567890ABCDEF")),
    ;

    private final String desc;
    /**
     * hashIds生成器对象
     */
    private final Hashids hashids;
}
