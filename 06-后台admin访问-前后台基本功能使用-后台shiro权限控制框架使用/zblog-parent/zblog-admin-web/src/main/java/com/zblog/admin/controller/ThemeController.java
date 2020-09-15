package com.zblog.admin.controller;

import com.zblog.admin.config.ContextStartup;
import com.zblog.basics.service.OptionsService;
import com.zblog.util.conf.Result;
import com.zblog.util.tool.FileKit;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.BlogUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author : landy
 */
@Controller
@RequestMapping("/admin/theme")
public class ThemeController extends BaseController {
    @Reference
    private OptionsService optionsService;
    @Autowired
    private ContextStartup contextStartup;

    @RequestMapping("/index")
    @LoginRequired
    public String index(ModelMap model) {
        model.put("themes", BlogUtils.getThemes());
        return "/admin/theme/index";
    }

    @RequestMapping("/active")
    @ResponseBody
    @LoginRequired
    public Result update(@RequestParam Map<String, String> body) {
        optionsService.update(body);
        contextStartup.reloadOptions(false);
        return Result.success();
    }

    @PostMapping("/upload")
    @ResponseBody
    @LoginRequired
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (null == file || file.isEmpty()) {
            return Result.failure("文件不能为空");
        } else {
            String suffix = FileKit.getSuffix(Objects.requireNonNull(file.getOriginalFilename()));
            if (!".zip".equalsIgnoreCase(suffix)) {
                return Result.failure("请上传zip文件");
            }
            try {
                return BlogUtils.uploadTheme(file);
            } catch (IOException e) {
                return Result.failure(e.getMessage());
            }
        }
    }
}
