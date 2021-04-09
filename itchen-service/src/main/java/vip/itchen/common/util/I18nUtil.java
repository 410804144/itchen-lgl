package vip.itchen.common.util;

import vip.itchen.support.SpringContextUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author ckh
 * @date 2021-04-09
 */
public class I18nUtil {
    private static MessageSource MESSAGES = null;

    private static MessageSource getInstance() {
        if (null == MESSAGES) {
            MESSAGES = SpringContextUtil.getBean(MessageSource.class);
        }
        return MESSAGES;
    }

    public static String getMessage(String code, Object...args) {
        return getInstance().getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
