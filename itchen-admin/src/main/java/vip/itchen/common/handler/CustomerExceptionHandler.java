package vip.itchen.common.handler;

import vip.itchen.common.util.I18nUtil;
import vip.itchen.support.exceptions.BizMsgException;
import vip.itchen.support.exceptions.DevMsgException;
import vip.itchen.support.exceptions.UnAuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author ckh
 * @date 2021-04-09
 */
@ControllerAdvice
@Slf4j
public class CustomerExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RespModel badRequest(HttpRequestMethodNotSupportedException e) {
        return RespModel.error("common.00003");
    }

    @ExceptionHandler(UnAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public RespModel unAuth(UnAuthException e) {
        if (StringUtils.isNotBlank(e.getMsgCode())) {
            return RespModel.error(e.getMsgCode());
        }
        return RespModel.error("common.00004");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RespModel illegalArgument(IllegalArgumentException e) {
        return RespModel.error("common.00005");
    }

    @ExceptionHandler(BizMsgException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RespModel biz(BizMsgException e) {
        return RespModel.error(e.getMsgCode(), e.getMsgArgs());
    }

    @ExceptionHandler(DevMsgException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RespModel dev(DevMsgException e) {
        return RespModel.error("dev error", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RespModel paramsIgnore(MethodArgumentNotValidException e) {
        String errorMsg = "common.00006";
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            if (StringUtils.isNotBlank(error.getDefaultMessage())) {
                errorMsg = error.getDefaultMessage();
                break;
            }
        }
        return RespModel.error(errorMsg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RespModel paramError(HttpMessageNotReadableException e) {
        return RespModel.error(e.getMessage(), I18nUtil.getMessage("common.00005"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RespModel other(Exception e) {
        return RespModel.error(e.getMessage(), I18nUtil.getMessage("common.00007"));
    }
}
