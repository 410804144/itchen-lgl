package vip.itchen.model.resp.ac;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.itchen.domain.ac.AcUser;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Data
@ApiModel("登陆应答")
public class LoginResp {

    @ApiModelProperty("uid")
    private Long uid;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("token")
    private String token;

    public LoginResp() {

    }

    public LoginResp(AcUser user, String token) {
        this.uid = user.getUid();
        this.nickname = user.getNickname();
        this.token = token;
    }
}
