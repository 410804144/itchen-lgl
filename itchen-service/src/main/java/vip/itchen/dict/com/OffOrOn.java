package vip.itchen.dict.com;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 开关
 * @author ckh
 * @date 2021-04-09
 */
@Getter
@AllArgsConstructor
public enum OffOrOn {
    OFF("关闭"),
    ON("开启"),
    ;

    private final String desc;
}
