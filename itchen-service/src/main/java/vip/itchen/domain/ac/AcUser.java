package vip.itchen.domain.ac;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * AC.用户信息
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AcUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * UID
     */
    private Long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账户状态
     */
    private String userStatus;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
