/**
 * 
 */
package com.zblog.index.controller.auth;


import com.zblog.basics.service.UserService;
import com.zblog.index.controller.Views;
import com.zblog.post.service.SecurityCodeService;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.util.tool.MD5;
import com.zblog.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author langhsu
 *
 */
@Controller
@ConditionalOnProperty(name = "site.controls.register", havingValue = "true", matchIfMissing = true)
public class RegisterController extends BaseController {
	@Reference
	private UserService userService;
	@Reference
	private SecurityCodeService securityCodeService;

	@GetMapping("/register")
	public String view(HttpServletRequest request) {
		AccountProfile profile = getProfile(request);
		if (profile != null) {
			return String.format(Views.REDIRECT_USER_HOME, profile.getId());
		}
		return view(Views.REGISTER);
	}
	
	@PostMapping("/register")
	public String register(UserVO post, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String view = view(Views.REGISTER);
		try {
			if (siteOptions.getControls().isRegister_email_validate()) {
				String code = request.getParameter("code");
				Assert.state(StringUtils.isNotBlank(post.getEmail()), "请输入邮箱地址");
				Assert.state(StringUtils.isNotBlank(code), "请输入邮箱验证码");
				securityCodeService.verify(post.getEmail(), Consts.CODE_REGISTER, code);
			}
			post.setAvatar(Consts.AVATAR);
			userService.register(post);
			Result<AccountProfile> result = userService.login(post.getUsername(), MD5.md5(post.getPassword()));

			AfterLoginHandler(request,response,result,model);

		} catch (Exception e) {
            model.addAttribute("post", post);
			model.put("data", Result.failure(e.getMessage()));
		}
		return view;
	}

}