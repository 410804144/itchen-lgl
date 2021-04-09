package vip.itchen.common.handler;

import vip.itchen.common.util.I18nUtil;
import vip.itchen.support.exceptions.BizMsgException;
import vip.itchen.support.exceptions.DevMsgException;
import vip.itchen.support.exceptions.UnAuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
        String errorMsg = getErrorMsg(e.getBindingResult());
        return RespModel.error(errorMsg);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RespModel paramsBindIgnore(BindException e) {
        String errorMsg = getErrorMsg(e.getBindingResult());
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
        log.error(e.getMessage(), e);
        return RespModel.error(e.getMessage(), I18nUtil.getMessage("common.00007"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RespModel fileSizeLimit(Exception e) {
        return RespModel.error(e.getMessage(), I18nUtil.getMessage("common.00013"));
    }

    /**
     * 获取参数校验异常内容
     * @param bindingResult 绑定信息
     * @return 异常内容
     */
    private String getErrorMsg(BindingResult bindingResult) {
        String errorMsg = "common.00006";
        List<ObjectError> errors = bindingResult.getAllErrors();
        for (ObjectError error : errors) {
            if (StringUtils.isNotBlank(error.getDefaultMessage())) {
                errorMsg = error.getDefaultMessage();
                break;
            }
        }
        return errorMsg;
    }
}
