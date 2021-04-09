package vip.itchen.service.po;

import vip.itchen.dict.com.CommonStatus;
import vip.itchen.domain.po.PoItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * PO.商品信息 服务类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
public interface IPoItemService extends IService<PoItem> {

    /**
     * 根据商品名称获取商品信息
     * @param itemName 商品名称
     * @return 商品信息
     */
    PoItem getByItemName(String itemName);

    /**
     * 获取商品列表
     * @param itemStatus 商品状态
     * @param itemNameList 商品名称
     * @return 商品列表
     */
    List<PoItem> list(CommonStatus itemStatus, List<String> itemNameList);

    /**
     * 创建商品
     * @param itemName 商品名称
     * @param price 商品价格
     * @param unit 商品单位
     */
    void create(String itemName, BigDecimal price, String unit);
}
