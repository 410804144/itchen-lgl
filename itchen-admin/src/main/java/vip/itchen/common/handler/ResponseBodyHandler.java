package vip.itchen.common.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import vip.itchen.common.properties.CommonProperties;

import javax.annotation.Resource;

/**
 * @author ckh
 * @date 2021-04-09
 */
@ControllerAdvice
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {

    private static final String[] swaggerPath = {"swagger-resources", "/v2/api-docs"};

    @Resource
    private CommonProperties commonProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (body instanceof RespModel) {
            return body;
        }
        if (null != methodParameter.getMethod() && null != methodParameter.getMethod().getAnnotation(NormalResponse.class)) {
            return body;
        }
        if (body instanceof String) {
            return JSON.toJSONString(RespModel.success(body));
        }
        if (!mediaType.includes(MediaType.APPLICATION_JSON)) {
            return body;
        }
        if (commonProperties.getSwaggerOpen() && isSwagger(serverHttpRequest)) {
            return body;
        }
        return RespModel.success(body);
    }

    private Boolean isSwagger(ServerHttpRequest request) {
        for (String path : swaggerPath) {
            if (request.getURI().getPath().contains(path)) {
                return true;
            }
        }
        return false;
    }
}
