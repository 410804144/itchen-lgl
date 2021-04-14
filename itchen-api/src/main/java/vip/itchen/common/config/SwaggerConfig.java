package vip.itchen.common.config;

import vip.itchen.common.constant.Const;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "common", name = "swagger-open", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .tags(new Tag("user", "用户相关", 1), getTags())
                .select()
                .apis(RequestHandlerSelectors.basePackage("vip.itchen.api"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getHeaderToken());
    }

    private List<Parameter> getHeaderToken() {
        List<Parameter> pars = new ArrayList<>();

        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name(Const.COMMON_TOKEN_KEY).description("认证token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        ParameterBuilder messagePar = new ParameterBuilder();
        messagePar.name(Const.COMMON_LANGUAGE_KEY).description("语言").modelRef(new ModelRef("string")).parameterType("header").defaultValue("zh-CN").required(false).build();
        pars.add(messagePar.build());
        return pars;
    }

    private Tag[] getTags() {
        return new Tag[] {
                new Tag("com", "共通相关", 2),
                new Tag("item", "商品相关", 3),
        };
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Itchen Api")
                .description("该文档的接口使用jwt方式校验权限 \n" +
                        "权限认证方式 header中添加" + Const.COMMON_TOKEN_KEY + "消息头，内容为登录返回的" + Const.COMMON_TOKEN_KEY + "字段 \n" +
                        "多语言切换方式 header中添加" + Const.COMMON_LANGUAGE_KEY + "消息头，语言支持:zh-CN/zh-TW/en-US")
                .version("1.0-SNAPSHOT")
                .build();
    }
}
