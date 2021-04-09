package vip.itchen.model.req.ac;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Data
@ApiModel("注册参数")
public class AdminRegisterReq {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "V.110001")
    @Length(max = 32, message = "V.110002")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "V.110003")
    @Length(min = 8, max = 32, message = "V.110004")
    private String password;

    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "V.110005")
    @Length(max = 32, message = "V.110006")
    private String nickname;

    @ApiModelProperty(value = "员工姓名")
    @Length(max = 32, message = "V.110007")
    private String staffName;
}
