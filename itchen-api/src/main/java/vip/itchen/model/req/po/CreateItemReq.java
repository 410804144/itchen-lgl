package vip.itchen.model.req.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Data
@ApiModel("新增商品参数")
public class CreateItemReq {

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "V.100007")
    @Length(max = 32, message = "V.100008")
    private String itemName;

    @ApiModelProperty(value = "商品价格", required = true)
    @NotNull(message = "V.100009")
    @DecimalMin(value = "0", message = "V.100010")
    private BigDecimal price;

    @ApiModelProperty(value = "商品单位")
    @Length(max = 32, message = "V.100011")
    private String unit;
}
