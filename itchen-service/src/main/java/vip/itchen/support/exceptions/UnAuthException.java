package vip.itchen.support.exceptions;

import vip.itchen.common.util.I18nUtil;

/**
 * @author alabimofa
 * @date 2019/6/24
 */
public class UnAuthException extends RuntimeException {
    private final String msgCode;

    public UnAuthException() {
        super("");
        this.msgCode = "";
    }

    public UnAuthException(String msgCode) {
        super(I18nUtil.getMessage(msgCode));
        this.msgCode = msgCode;
    }

    public String getMsgCode() {
        return msgCode;
    }
}
