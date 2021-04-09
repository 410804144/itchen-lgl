package vip.itchen.model.req.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Data
@ApiModel("获取商品列表参数")
public class ListItemReq {

    @ApiModelProperty(value = "商品名称(多个参数使用逗号隔开）")
    private String itemNames;
}
