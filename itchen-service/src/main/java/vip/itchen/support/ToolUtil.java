package vip.itchen.support;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by lhb on 2019/6/22
 */
public class ToolUtil {

    /**
     * IP地址请求头
     */
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 获取随机位数的字符串
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<length; i++) {
            sb.append(base.charAt(ThreadLocalRandom.current().nextInt(base.length())));
        }
        return sb.toString();
    }

    /**
     * 获取32位UUID字符串
     */
    public static String getUuidString() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 获取请求IP
     * @return ip地址
     */
    public static String getClientIpAddress() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (String header: IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }

        String ip = request.getRemoteAddr();
        return StringUtils.isBlank(ip) ? "0.0.0.0" : ip;
    }

    /**
     * 参数 MD5签名
     */
    public static String signParams(TreeMap<String, Object> params, String secret) {
        String signStr = formatParams(params).concat("&secret=").concat(secret);
        return DigestUtils.md5Hex(signStr);
    }

    /**
     * 格式化参数，生成签名字符串
     */
    private static String formatParams(TreeMap<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> it : map.entrySet()) {
            sb.append(it.getKey()).append("=").append(it.getValue().toString()).append("&");
        }
        return StringUtils.removeEnd(sb.toString(), "&");
    }
}
