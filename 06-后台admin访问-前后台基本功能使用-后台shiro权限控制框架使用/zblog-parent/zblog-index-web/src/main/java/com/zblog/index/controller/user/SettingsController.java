package com.zblog.index.controller.user;


import com.zblog.basics.service.UserService;
import com.zblog.index.controller.Views;
import com.zblog.index.controller.post.UploadController;
import com.zblog.post.service.SecurityCodeService;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.util.tool.FileKit;
import com.zblog.util.tool.FilePathUtils;
import com.zblog.util.tool.ImageUtils;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : landy
 * @version : 1.0
 */
@Controller
@RequestMapping("/settings")
public class SettingsController extends BaseController {
    @Reference
    private UserService userService;
    @Reference
    private SecurityCodeService securityCodeService;

    @GetMapping(value = "/profile")
    @LoginRequired
    public String view(ModelMap model, HttpServletRequest request) {
        AccountProfile profile = getProfile(request);
        UserVO view = userService.get(profile.getId());
        model.put("view", view);
        return view(Views.SETTINGS_PROFILE);
    }

    @GetMapping(value = "/email")
    @LoginRequired
    public String email() {
        return view(Views.SETTINGS_EMAIL);
    }

    @GetMapping(value = "/avatar")
    @LoginRequired
    public String avatar() {
        return view(Views.SETTINGS_AVATAR);
    }

    @GetMapping(value = "/password")
    @LoginRequired
    public String password() {
        return view(Views.SETTINGS_PASSWORD);
    }

    @PostMapping(value = "/profile")
    @LoginRequired
    public String updateProfile(String name, String signature, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        Result data;
        AccountProfile profile = getProfile(request);

        try {
            UserVO user = new UserVO();
            user.setId(profile.getId());
            user.setName(name);
            user.setSignature(signature);

            AfterLoginHandler(request,response,Result.success(userService.update(user)),model);

            // put 最新信息
            UserVO view = userService.get(profile.getId());
            model.put("view", view);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_PROFILE);
    }

    @PostMapping(value = "/email")
    @LoginRequired
    public String updateEmail(String email, String code, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        Result data;
        AccountProfile profile = getProfile(request);
        try {
            Assert.hasLength(email, "请输入邮箱地址");
            Assert.hasLength(code, "请输入验证码");

            securityCodeService.verify(String.valueOf(profile.getId()), Consts.CODE_BIND, code);
            // 先执行修改，判断邮箱是否更改，或邮箱是否被人使用
            AccountProfile p = userService.updateEmail(profile.getId(), email);
            AfterLoginHandler(request,response,Result.success(p),model);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_EMAIL);
    }

    @PostMapping(value = "/password")
    @LoginRequired
    public String updatePassword(String oldPassword, String password, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        Result data;
        try {
            AccountProfile profile = getProfile(request);
            userService.updatePassword(profile.getId(), oldPassword, password);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_PASSWORD);
    }

    @PostMapping("/avatar")
    @LoginRequired
    @ResponseBody
    public UploadController.UploadResult updateAvatar(@RequestParam(value = "file", required = false) MultipartFile file
                                                      ,ModelMap model
            , HttpServletRequest request, HttpServletResponse response) throws IOException {
        UploadController.UploadResult result = new UploadController.UploadResult();
        AccountProfile profile = getProfile(request);

        // 检查空
        if (null == file || file.isEmpty()) {
            return result.error(UploadController.errorInfo.get("NOFILE"));
        }

        String fileName = file.getOriginalFilename();

        // 检查类型
        if (!FileKit.checkFileType(fileName)) {
            return result.error(UploadController.errorInfo.get("TYPE"));
        }

        // 保存图片
        try {
            String ava100 = Consts.avatarPath + getAvaPath(profile.getId(), 240);
            byte[] bytes = ImageUtils.screenshot(file, 240, 240);
            String path = storageFactory.get().writeToStore(bytes, ava100);

            AccountProfile user = userService.updateAvatar(profile.getId(), path);
            AfterLoginHandler(request,response,Result.success(user),model);

            result.ok(UploadController.errorInfo.get("SUCCESS"));
            result.setName(fileName);
            result.setPath(path);
            result.setSize(file.getSize());
        } catch (Exception e) {
            result.error(UploadController.errorInfo.get("UNKNOWN"));
        }
        return result;
    }

    private String getAvaPath(long uid, int size) {
        String base = FilePathUtils.getAvatar(uid);
        return String.format("/%s_%d.jpg", base, size);
    }
}
