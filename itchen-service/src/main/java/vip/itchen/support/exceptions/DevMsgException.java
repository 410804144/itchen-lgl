package vip.itchen.support.exceptions;

/**
 * 开发中出现的异常错误（没有多语言支持）
 * Created by lhb on 2019/6/22
 */
public class DevMsgException extends RuntimeException {

    public DevMsgException(String message) {
        super(message);
    }
}
