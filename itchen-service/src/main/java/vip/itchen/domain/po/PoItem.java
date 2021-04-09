package vip.itchen.domain.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * PO.商品信息
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号
     */
    private String itemCode;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品状态
     */
    private String itemStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
