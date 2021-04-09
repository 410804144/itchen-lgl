package vip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ckh
 * @date 2021-04-09
 */
@SpringBootApplication
@ComponentScan(value = "vip.itchen")
@Slf4j
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class);
        log.info("{} is success!", AdminApplication.class.getName());
    }
}
