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
public class JobApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class);
        log.info("{} Job Application is success!", JobApplication.class.getName());
    }
}
