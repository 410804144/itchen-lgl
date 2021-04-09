package vip.itchen.support.exceptions;

/**
 * 业务逻辑异常
 * Created by lhb on 2019/6/22
 */
public class BizMsgException extends RuntimeException {

    private String msgCode;
    private Object[] msgArgs;

    public BizMsgException(String msgCode) {
        this.msgCode = msgCode;
        this.msgArgs = null;
    }

    public BizMsgException(String msgCode, Object...msgArgs) {
        this.msgCode = msgCode;
        this.msgArgs = msgArgs;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public Object[] getMsgArgs() {
        return msgArgs;
    }
}
