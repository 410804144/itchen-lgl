package vip.itchen.api.ac;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.itchen.manager.IAdminUserManager;
import vip.itchen.model.req.ac.AdminLoginReq;
import vip.itchen.model.req.ac.AdminRegisterReq;
import vip.itchen.model.resp.AdminLoginResp;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author ckh
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/admin_api/staff")
@Api(value = "/admin_api/staff", tags = "staff")
public class StaffController {

    @Resource
    private IAdminUserManager apiUserManager;

    @ApiOperation("注册")
    @PostMapping("/register")
    public void register(@RequestBody @Valid AdminRegisterReq req) {
        apiUserManager.register(req);
    }

    @ApiOperation("登陆")
    @PostMapping("/login")
    public AdminLoginResp login(@RequestBody @Valid AdminLoginReq req) {
        return apiUserManager.login(req);
    }

}
