package vip.itchen.common.config;

import cn.hutool.json.JSONUtil;
import vip.itchen.common.handler.RespModel;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 拦截器异常
 * @author ckh
 * @date 2021-04-09
 */
@Controller
public class RestfulErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Resource
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public RespModel error(WebRequest webRequest, HttpServletResponse response) {
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.STACK_TRACE,
                        ErrorAttributeOptions.Include.BINDING_ERRORS));

        String message = attr.get("message").toString();
        if (JSONUtil.isJsonObj(message)) {
            RespModel resp = JSONUtil.toBean(message, RespModel.class, true);
            if (null != resp) {
                return resp;
            }
        }
        return RespModel.error("common.00001", message);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
