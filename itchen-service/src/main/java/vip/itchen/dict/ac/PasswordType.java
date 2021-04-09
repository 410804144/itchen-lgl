package vip.itchen.dict.ac;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 密码类型
 * @author ckh
 * @date 2021-04-09
 */
@Getter
@AllArgsConstructor
public enum PasswordType {
    LOGIN("登陆"),
    ;

    private final String desc;
}
