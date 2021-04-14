package vip.itchen.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ckh
 * @date 2021-04-14
 */
@Data
@Component
@ConfigurationProperties(prefix = "api-config")
public class ApiConfigProperties {

    /**
     * 上传路径
     */
    private String uploadPath;
}
