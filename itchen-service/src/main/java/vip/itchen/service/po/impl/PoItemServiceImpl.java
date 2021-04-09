package vip.itchen.service.po.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.itchen.dict.com.CommonStatus;
import vip.itchen.domain.po.PoItem;
import vip.itchen.mapper.po.PoItemMapper;
import vip.itchen.service.po.IPoItemService;
import vip.itchen.support.sequence.SeqConfigure;
import vip.itchen.support.sequence.dict.RedisSeqBizType;
import vip.itchen.support.sequence.dict.SeqType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * PO.商品信息 服务实现类
 * </p>
 *
 * @author ckh
 * @since 2021-04-09
 */
@Service
public class PoItemServiceImpl extends ServiceImpl<PoItemMapper, PoItem> implements IPoItemService {

    @Override
    public PoItem getByItemName(String itemName) {
        return lambdaQuery()
                .eq(PoItem::getItemName, itemName)
                .last("limit 1")
                .orderByAsc(PoItem::getId)
                .one();
    }

    @Override
    public List<PoItem> list(CommonStatus itemStatus, List<String> itemNameList) {
        LambdaQueryChainWrapper<PoItem> lambda = lambdaQuery();
        if (null != itemStatus) {
            lambda.eq(PoItem::getItemStatus, itemStatus);
        }
        if (CollUtil.isNotEmpty(itemNameList)) {
            for (String itemName : itemNameList) {
                lambda.like(PoItem::getItemName, itemName);
            }
        }
        return lambda
                .orderByAsc(PoItem::getItemName)
                .list();
    }

    @Override
    public void create(String itemName, BigDecimal price, String unit) {
        PoItem item = new PoItem();
        item.setItemCode(
                SeqConfigure.builder()
                        .seqType(SeqType.REDIS)
                        .redisSeqBizType(RedisSeqBizType.PO_ITEM_CODE)
                        .build()
                        .getStringId()
        );
        item.setItemName(itemName);
        item.setPrice(price);
        item.setUnit(StrUtil.nullToEmpty(unit));
        item.setItemStatus(CommonStatus.ENABLE.name());
        item.setCreateTime(new Date());
        item.setModifyTime(new Date());
        baseMapper.insert(item);
    }
}
