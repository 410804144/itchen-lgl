package vip.itchen.common.config.jwt;


import vip.itchen.common.constant.Const;
import vip.itchen.conts.RedisConst;
import vip.itchen.model.resp.AdminLoginResp;
import vip.itchen.support.JwtTokenUtil;
import vip.itchen.support.RedisToolUtil;
import vip.itchen.support.exceptions.UnAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt-secret}")
    private String jwtSecret;

    @Resource
    private RedisToolUtil redisToolUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(Const.COMMON_TOKEN_KEY);
        Claims claims = null;

        if (StringUtils.isNotBlank(requestTokenHeader)) {
            if (!requestTokenHeader.startsWith(Const.COMMON_TOKEN_PREFIX)) {
                throw new UnAuthException("common.00010");
            }
            String jwtToken = StringUtils.removeStart(requestTokenHeader, Const.COMMON_TOKEN_PREFIX);
            try {
                claims = JwtTokenUtil.getAllClaimsFromToken(jwtToken, jwtSecret);
            } catch (SignatureException | MalformedJwtException e) {
                throw new UnAuthException("common.00008");
            } catch (ExpiredJwtException e) {
                throw new UnAuthException("common.00009");
            } catch (Exception e) {
                throw new UnAuthException("common.00001");
            }
        }

        // Once we get the token validate it.
        if (claims != null && claims.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            final Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new UnAuthException("common.00009");
            }

            AdminLoginResp loginResp = redisToolUtil.get(RedisConst.ADMIN_LOGIN_TOKEN_KEY.concat(claims.getSubject()), AdminLoginResp.class);
            if (null == loginResp) {
                throw new UnAuthException("common.00009");
            }
            if (!StringUtils.equals(loginResp.getToken(), requestTokenHeader)) {
                throw new UnAuthException("common.00012");
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginResp, null, authorities);
            // After setting the Authentication in the context, we specify
            // that the current user is authenticated. So it passes the
            // Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(request, response);
    }
}