package vip.itchen.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.itchen.domain.ac.AcStaff;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Data
@ApiModel("登陆应答")
public class AdminLoginResp {

    @ApiModelProperty("sid")
    private Long sid;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("员工姓名")
    private String staffName;

    @ApiModelProperty("token")
    private String token;

    public AdminLoginResp(AcStaff staff, String token) {
        this.sid = staff.getSid();
        this.nickname = staff.getNickname();
        this.staffName = staff.getStaffName();
        this.token = token;
    }
}
