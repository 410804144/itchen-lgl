package vip.itchen.domain.ac;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * AC.员工信息
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AcStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * SID
     */
    private Long sid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 员工状态
     */
    private String staffStatus;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
