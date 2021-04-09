package vip.itchen.common.config.jwt;

import com.alibaba.fastjson.JSONObject;
import vip.itchen.common.handler.RespModel;
import vip.itchen.support.exceptions.UnAuthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        RespModel model;
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (null == exception) {
            model = RespModel.error("common.00011");
        } else if (exception instanceof SignatureException || exception instanceof MalformedJwtException) {
            model = RespModel.error("common.00008");
        } else if (exception instanceof ExpiredJwtException) {
            model = RespModel.error("common.00009");
        } else if (exception instanceof UnAuthException) {
            UnAuthException unAuthException = (UnAuthException) exception;
            model = RespModel.error(unAuthException.getMsgCode());
        } else if (exception instanceof IllegalArgumentException) {
            model = RespModel.error("common.00010");
        } else {
            model = RespModel.error("common.00004");
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, JSONObject.toJSONString(model));
    }
}