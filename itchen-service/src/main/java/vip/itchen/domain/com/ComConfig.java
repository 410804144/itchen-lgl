package vip.itchen.domain.com;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * COM.配置
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ComConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置类型
     */
    private String configType;

    /**
     * 配置key
     */
    private String configKey;

    /**
     * 说明
     */
    private String configDesc;

    /**
     * 小数值1
     */
    private BigDecimal decimalValue1;

    /**
     * 小数值2
     */
    private BigDecimal decimalValue2;

    /**
     * 小数值3
     */
    private BigDecimal decimalValue3;

    /**
     * 整型值1
     */
    private Integer intValue1;

    /**
     * 整型值2
     */
    private Integer intValue2;

    /**
     * 整型值3
     */
    private Integer intValue3;

    /**
     * 长整型值1
     */
    private Long bigintValue1;

    /**
     * 长整型值2
     */
    private Long bigintValue2;

    /**
     * 长整型值3
     */
    private Long bigintValue3;

    /**
     * 字符值1
     */
    private String varcharValue1;

    /**
     * 字符值2
     */
    private String varcharValue2;

    /**
     * 字符值3
     */
    private String varcharValue3;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
