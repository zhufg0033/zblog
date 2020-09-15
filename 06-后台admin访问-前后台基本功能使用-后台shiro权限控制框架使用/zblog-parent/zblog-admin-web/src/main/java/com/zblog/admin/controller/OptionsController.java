/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.admin.controller;

import com.zblog.admin.config.ContextStartup;
import com.zblog.basics.service.OptionsService;
import com.zblog.post.service.PostSearchService;
import com.zblog.util.conf.Result;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 系统配置
 *
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/admin/options")
public class OptionsController extends BaseController {
	@Reference
	private OptionsService optionsService;
	@Reference
	private PostSearchService postSearchService;
	@Autowired
	private ContextStartup contextStartup;

	@RequestMapping("/index")
	@LoginRequired
	public String index(ModelMap model) {
		return "/admin/options/index";
	}
	
	@RequestMapping("/update")
	@LoginRequired
	public String update(@RequestParam Map<String, String> body, ModelMap model) {
		optionsService.update(body);
		contextStartup.reloadOptions(false);
		model.put("data", Result.success());
		return "/admin/options/index";
	}

	/**
	 * 刷新系统变量
	 * @return
	 */
	@RequestMapping("/reload_options")
	@ResponseBody
	@LoginRequired
	public Result reloadOptions() {
		contextStartup.reloadOptions(false);
		contextStartup.resetChannels();

		return Result.success();
	}

	@RequestMapping("/reset_indexes")
	@ResponseBody
	@LoginRequired
	public Result resetIndexes() {
		postSearchService.resetIndexes();
		return Result.success();
	}
}
