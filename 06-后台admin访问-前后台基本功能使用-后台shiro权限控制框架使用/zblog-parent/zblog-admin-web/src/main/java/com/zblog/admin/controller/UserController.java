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


import com.zblog.basics.service.RoleService;
import com.zblog.basics.service.UserRoleService;
import com.zblog.basics.service.UserService;
import com.zblog.sharedb.entity.ShiroRole;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author langhsu
 *
 */
@Controller("adminUserController")
@RequestMapping("/admin/user")
public class UserController extends BaseController {
	@Reference
	private UserService userService;

	@Reference
	private RoleService roleService;

	@Reference
	private UserRoleService userRoleService;

	@RequestMapping("/list")
	public String list(String name, ModelMap model) {
		PageModuls pageModuls = wrapPageable();
		DataPageVO<UserVO> page = userService.paging(pageModuls, name);

		List<UserVO> users = page.getList();
		List<Long> userIds = new ArrayList<>();

		users.forEach(item -> {
			userIds.add(item.getId());
		});

		Map<Long, List<ShiroRole>> map = userRoleService.findMapByUserIds(userIds);
		users.forEach(item -> {
			item.setRoles(map.get(item.getId()));
		});

		model.put("name", name);
		model.put("page", PageUtils.transformPage(page));
		return "/admin/user/list";
	}

	@RequestMapping("/view")
	public String view(Long id, ModelMap model) {
		UserVO view = userService.get(id);
		view.setRoles(userRoleService.listRoles(view.getId()));
		model.put("view", view);
		model.put("roles", roleService.list());
		return "/admin/user/view";
	}

	@PostMapping("/update_role")
//	@RequiresPermissions("user:role")
	public String postAuthc(Long id, @RequestParam(value = "roleIds", required=false) Set<Long> roleIds, ModelMap model) {
		userRoleService.updateRole(id, roleIds);
		model.put("data", Result.success());
		return "redirect:/admin/user/list";
	}

	@RequestMapping(value = "/pwd", method = RequestMethod.GET)
//	@RequiresPermissions("user:pwd")
	public String pwsView(Long id, ModelMap model) {
		UserVO ret = userService.get(id);
		model.put("view", ret);
		return "/admin/user/pwd";
	}

	@RequestMapping(value = "/pwd", method = RequestMethod.POST)
//	@RequiresPermissions("user:pwd")
	public String pwd(Long id, String newPassword, ModelMap model) {
		UserVO ret = userService.get(id);
		model.put("view", ret);

		try {
			userService.updatePassword(id, newPassword);
			model.put("data", Result.successMessage("修改成功"));
		} catch (IllegalArgumentException e) {
			model.put("data", Result.failure(e.getMessage()));
		}
		return "/admin/user/pwd";
	}

	@RequestMapping("/open")
//	@RequiresPermissions("user:open")
	@ResponseBody
	public Result open(Long id) {
		userService.updateStatus(id, Consts.STATUS_NORMAL);
		return Result.success();
	}

	@RequestMapping("/close")
//	@RequiresPermissions("user:close")
	@ResponseBody
	public Result close(Long id) {
		userService.updateStatus(id, Consts.STATUS_CLOSED);
		return Result.success();
	}
}
