package vip.itchen.dict.com;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 共通状态
 * @author ckh
 * @date 2021-04-09
 */
@Getter
@AllArgsConstructor
public enum CommonStatus {
    ENABLE("启用"),
    DISABLE("禁用"),
    ;

    private final String desc;
}
