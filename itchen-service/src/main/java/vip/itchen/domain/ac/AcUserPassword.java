package vip.itchen.domain.ac;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * AC.用户密码
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AcUserPassword implements Serializable {

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
     * 密码类型
     */
    private String passwordType;

    /**
     * 密码值
     */
    private String passwordValue;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
