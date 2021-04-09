package vip.itchen.model.resp.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.itchen.dict.com.CommonStatus;
import vip.itchen.domain.po.PoItem;

import java.math.BigDecimal;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Data
@ApiModel("获取商品列表应答")
public class ListItemResp {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("商品名称")
    private String itemName;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("商品状态")
    private CommonStatus itemStatus;

    public ListItemResp(PoItem item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.unit = item.getUnit();
        this.itemStatus = CommonStatus.valueOf(item.getItemStatus());
    }
}
