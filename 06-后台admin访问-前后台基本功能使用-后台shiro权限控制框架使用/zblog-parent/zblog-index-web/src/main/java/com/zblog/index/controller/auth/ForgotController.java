package com.zblog.index.controller.auth;


import com.zblog.basics.service.UserService;
import com.zblog.index.controller.Views;
import com.zblog.post.service.SecurityCodeService;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author langhsu on 2015/8/14.
 */
@Controller
public class ForgotController extends BaseController {
    @Reference
    private UserService userService;
    @Reference
    private SecurityCodeService securityCodeService;

    @GetMapping("/forgot")
    @LoginRequired
    public String view() {
        return view(Views.FORGOT);
    }

    @PostMapping("/forgot")
    @LoginRequired
    public String reset(String email, String code, String password, ModelMap model) {
        String view = view(Views.FORGOT);
        try {
            Assert.hasLength(email, "请输入邮箱地址");
            Assert.hasLength(code, "请输入验证码");
            UserVO user = userService.getByEmail(email);
            Assert.notNull(user, "账户不存在");

            securityCodeService.verify(String.valueOf(user.getId()), Consts.CODE_FORGOT, code);
            userService.updatePassword(user.getId(), password);
            model.put("data", Result.successMessage("恭喜您, 密码重置成功"));
            view = view(Views.LOGIN);
        } catch (Exception e) {
            model.put("data", Result.failure(e.getMessage()));
        }
        return view;
    }
}
