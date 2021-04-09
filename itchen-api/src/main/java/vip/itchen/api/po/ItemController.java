package vip.itchen.api.po;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import vip.itchen.common.config.jwt.JwtHolder;
import vip.itchen.manager.po.IApiItemManager;
import vip.itchen.model.req.po.CreateItemReq;
import vip.itchen.model.req.po.ListItemReq;
import vip.itchen.model.req.po.ModifyItemReq;
import vip.itchen.model.resp.po.ListItemResp;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ckh
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/web_api/item")
@Api(value = "/web_api/item", tags = "item")
public class ItemController {

    @Resource
    private IApiItemManager apiItemManager;

    @GetMapping("/list")
    @ApiOperation("获取商品列表")
    public List<ListItemResp> list(@ModelAttribute @Valid ListItemReq req) {
        return apiItemManager.list(JwtHolder.currentUid(), req);
    }

    @PostMapping("/create")
    @ApiOperation("新增商品")
    public void create(@RequestBody @Valid CreateItemReq req) {
        apiItemManager.create(req);
    }

    @PostMapping("/modify")
    @ApiOperation("修改商品")
    public void modify(@RequestBody @Valid ModifyItemReq req) {
        apiItemManager.modify(req);
    }

    @PostMapping("/enable/{itemId}")
    @ApiOperation("显示商品")
    public void enable(@PathVariable Integer itemId) {
        apiItemManager.enable(itemId);
    }

    @PostMapping("/disable/{itemId}")
    @ApiOperation("隐藏商品")
    public void disable(@PathVariable Integer itemId) {
        apiItemManager.disable(itemId);
    }

    @PostMapping("/del/{itemId}")
    @ApiOperation("删除商品")
    public void del(@PathVariable Integer itemId) {
        apiItemManager.del(itemId);
    }
}
