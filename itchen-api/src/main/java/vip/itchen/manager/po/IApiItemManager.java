package vip.itchen.manager.po;

import vip.itchen.model.req.po.CreateItemReq;
import vip.itchen.model.req.po.ListItemReq;
import vip.itchen.model.req.po.ModifyItemReq;
import vip.itchen.model.resp.po.ListItemResp;

import java.util.List;

/**
 * @author ckh
 * @date 2021-04-09
 */
public interface IApiItemManager {

    /**
     * 获取商品列表
     * @param uid uid
     * @param req 参数
     * @return 商品列表
     */
    List<ListItemResp> list(Long uid, ListItemReq req);

    /**
     * 新增商品
     * @param req 参数
     */
    void create(CreateItemReq req);

    /**
     * 修改商品
     * @param req 参数
     */
    void modify(ModifyItemReq req);

    /**
     * 显示商品
     * @param itemId 商品id
     */
    void enable(Integer itemId);

    /**
     * 隐藏商品
     * @param itemId 商品id
     */
    void disable(Integer itemId);

    /**
     * 删除商品
     * @param itemId 商品id
     */
    void del(Integer itemId);
}
