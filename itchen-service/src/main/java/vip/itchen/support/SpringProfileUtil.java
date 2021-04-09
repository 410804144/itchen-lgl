package vip.itchen.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author alabimofa
 * @date 2020/8/6
 */
@Component
public class SpringProfileUtil {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    private final static String PROD_ENV = "prod";

    /**
     * 验证是否为正式环境
     */
    public boolean isProdEnv() {
        return StringUtils.containsIgnoreCase(activeProfile, PROD_ENV);
    }

    /**
     * 验证是否为测试环境
     */
    public boolean isTestEnv() {
        return !isProdEnv();
    }
}
