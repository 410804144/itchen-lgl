package vip.itchen.manager.po.impl;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import vip.itchen.dict.com.CommonStatus;
import vip.itchen.domain.po.PoItem;
import vip.itchen.manager.po.IApiItemManager;
import vip.itchen.model.req.po.CreateItemReq;
import vip.itchen.model.req.po.ListItemReq;
import vip.itchen.model.req.po.ModifyItemReq;
import vip.itchen.model.resp.po.ListItemResp;
import vip.itchen.service.po.IPoItemService;
import vip.itchen.support.exceptions.BizMsgException;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ckh
 * @date 2021-04-09
 */
@Service
public class ApiItemManagerImpl implements IApiItemManager {

    @Resource
    private IPoItemService poItemService;

    @Override
    public List<ListItemResp> list(Long uid, ListItemReq req) {
        List<String> itemNameList = null;
        if (StrUtil.isNotBlank(req.getItemNames())) {
            itemNameList = Arrays.stream(req.getItemNames().split(StrUtil.SPACE))
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toList());
        }

        // 登陆后可以查询所有数据
        CommonStatus itemStatus = null == uid ? CommonStatus.ENABLE : null;

        return poItemService.list(itemStatus, itemNameList)
                .stream()
                .map(ListItemResp::new)
                .collect(Collectors.toList());
    }

    @Override
    public void create(CreateItemReq req) {
        PoItem item = poItemService.getByItemName(req.getItemName());
        if (null != item) {
            // 商品名称已经存在
            throw new BizMsgException("E.100006");
        }
        poItemService.create(req.getItemName(), req.getPrice(), req.getUnit(), req.getItemImage());
    }

    @Override
    public void modify(ModifyItemReq req) {
        PoItem item = checkAndGet(req.getItemId());
        PoItem oldItem = poItemService.getByItemName(req.getItemName());
        if (null != oldItem && !req.getItemId().equals(oldItem.getId())) {
            // 商品名称已经存在
            throw new BizMsgException("E.100006");
        }

        item.setItemName(req.getItemName());
        item.setPrice(req.getPrice());
        item.setUnit(StrUtil.nullToEmpty(req.getUnit()));
        item.setItemImage(StrUtil.nullToEmpty(req.getItemImage()));
        item.setModifyTime(new Date());
        poItemService.updateById(item);
    }

    @Override
    public void enable(Integer itemId) {
        PoItem item = checkAndGet(itemId);
        item.setItemStatus(CommonStatus.ENABLE.name());
        item.setModifyTime(new Date());
        poItemService.updateById(item);
    }

    @Override
    public void disable(Integer itemId) {
        PoItem item = checkAndGet(itemId);
        item.setItemStatus(CommonStatus.DISABLE.name());
        item.setModifyTime(new Date());
        poItemService.updateById(item);
    }

    @Override
    public void del(Integer itemId) {
        PoItem item = checkAndGet(itemId);
        poItemService.removeById(item.getId());
    }

    /**
     * 判断商品是否存在，如果存在，则返回商品信息
     * @param itemId 商品id
     * @return 商品信息
     */
    private PoItem checkAndGet(Integer itemId) {
        PoItem item = poItemService.getById(itemId);
        if (null == item) {
            // 商品信息不存在
            throw new BizMsgException("E.100007");
        }
        return item;
    }
}
