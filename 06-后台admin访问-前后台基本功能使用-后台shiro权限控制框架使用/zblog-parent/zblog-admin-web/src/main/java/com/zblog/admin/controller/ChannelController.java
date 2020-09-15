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
import com.zblog.post.service.ChannelService;
import com.zblog.sharedb.entity.MtoChannel;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author langhsu
 *
 */
@Controller("adminChannelController")
@RequestMapping("/admin/channel")
public class ChannelController extends BaseController {
	@Reference
	private ChannelService channelService;
	@Autowired
	private ContextStartup contextStartup;
	
	@RequestMapping("/list")
	@LoginRequired
//	@RequiresPermissions("channel:list")
	public String list(ModelMap model) {
		model.put("list", channelService.findAll(Consts.IGNORE));
		return "/admin/channel/list";
	}
	
	@RequestMapping("/view")
	@LoginRequired
	public String view(Integer id, ModelMap model) {
		if (id != null) {
			MtoChannel view = channelService.getById(id);
			model.put("view", view);
		}
		return "/admin/channel/view";
	}
	
	@RequestMapping("/update")
	@LoginRequired
//	@RequiresPermissions("channel:update")
	public String update(MtoChannel view) {
		if (view != null) {
			channelService.update(view);

			contextStartup.resetChannels();
		}
		return "redirect:/admin/channel/list";
	}

	@RequestMapping("/weight")
	@ResponseBody
	@LoginRequired
	public Result weight(@RequestParam Integer id, HttpServletRequest request) {
		int weight = ServletRequestUtils.getIntParameter(request, "weight", Consts.FEATURED_ACTIVE);
		channelService.updateWeight(id, weight);
		contextStartup.resetChannels();
		return Result.success();
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	@LoginRequired
//	@RequiresPermissions("channel:delete")
	public Result delete(Integer id) {
		Result data = Result.failure("操作失败");
		if (id != null) {
			try {
				channelService.delete(id);
				data = Result.success();

				contextStartup.resetChannels();
			} catch (Exception e) {
				data = Result.failure(e.getMessage());
			}
		}
		return data;
	}
	
}
