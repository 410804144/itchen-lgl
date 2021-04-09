package vip.itchen.support.i18n.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * i18n vo
 * @author chenkh
 * @date 2021-01-07
 */
@Data
@AllArgsConstructor
public class I18nVo {
    private boolean isNeedTranslate;
    private String key;
    private String oldValue;
    private String value;

    @Override
    public String toString() {
        if (isNeedTranslate) {
            return key.concat(" = ").concat(value);
        } else {
            return value;
        }
    }
}
