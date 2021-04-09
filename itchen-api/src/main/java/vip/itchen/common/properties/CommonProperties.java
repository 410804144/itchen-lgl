package vip.itchen.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Component
@Data
public class CommonProperties {

    @Value("${common.swagger-open}")
    private Boolean swaggerOpen = false;
}
