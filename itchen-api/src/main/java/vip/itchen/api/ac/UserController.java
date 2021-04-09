package vip.itchen.api.ac;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.itchen.manager.ac.IApiUserManager;
import vip.itchen.model.req.ac.LoginReq;
import vip.itchen.model.req.ac.RegisterReq;
import vip.itchen.model.resp.ac.LoginResp;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author ckh
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/web_api/user")
@Api(value = "/web_api/user", tags = "user")
public class UserController {

    @Resource
    private IApiUserManager apiUserManager;

    @ApiOperation("注册")
    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterReq req) {
        apiUserManager.register(req);
    }

    @ApiOperation("登陆")
    @PostMapping("/login")
    public LoginResp login(@RequestBody @Valid LoginReq req) {
        return apiUserManager.login(req);
    }

}
