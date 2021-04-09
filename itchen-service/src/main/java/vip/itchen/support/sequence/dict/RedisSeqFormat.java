package vip.itchen.support.sequence.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis生成序列号的格式及失效日期
 * 不推荐使用只有年月的格式（因为失效时间将会很长，不符合redis的特性）
 * @author alabimofa
 */
@Getter
@AllArgsConstructor
public enum RedisSeqFormat {

    yyyyMMddHHmmss("yyyyMMddHHmmss", 1L),
    yyyyMMddHHmm("yyyyMMddHHmm", 60L),
    yyyyMMddHH("yyyyMMddHH", 3600L),
    yyyyMMdd("yyyyMMdd", 24 * 3600L),
    yyMMddHHmmss("yyMMddHHmmss", 1L),
    yyMMddHHmm("yyMMddHHmm", 60L),
    yyMMddHH("yyMMddHH", 3600L),
    yyMMdd("yyMMdd", 24 * 3600L),
    ;

    private final String format;
    private final Long expireSeconds;
}
