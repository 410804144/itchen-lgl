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
@ApiModel("获取用户信息应答")
public class UserResp {

    @ApiModelProperty("uid")
    private Long uid;

    @ApiModelProperty("昵称")
    private String nickname;

    public UserResp(AcUser user) {
        this.uid = user.getUid();
        this.nickname = user.getNickname();
    }
}
