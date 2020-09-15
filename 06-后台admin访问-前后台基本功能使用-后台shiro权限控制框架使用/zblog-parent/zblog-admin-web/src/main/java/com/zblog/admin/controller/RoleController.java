/**
 * 
 */
package com.zblog.admin.controller;


import com.zblog.basics.service.PermissionService;
import com.zblog.basics.service.RoleService;
import com.zblog.sharedb.entity.ShiroPermission;
import com.zblog.sharedb.entity.ShiroRole;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.conf.Result;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.List;

/**
 * @author - langhsu on 2018/2/11
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {
	@Reference
    private RoleService roleService;
	@Reference
	private PermissionService permissionService;

	@GetMapping("/list")
	@LoginRequired
	public String paging(String name, ModelMap model) {
		PageModuls pageModuls = new PageModuls();
		pageModuls.setSort("id desc");
		DataPageVO<ShiroRole> shiroRoles = roleService.paging(pageModuls, name);
		model.put("name", name);
		model.put("page", PageUtils.transformPage(shiroRoles));
		return "/admin/role/list";
	}

	@RequestMapping("/view")
	@LoginRequired
	public String view(Long id, ModelMap model) {
		if (id != null && id > 0) {
			ShiroRole role = roleService.get(id);
			model.put("view", role);
		}

        model.put("permissions", permissionService.tree());
        return "/admin/role/view";
	}
	
	@RequestMapping("/update")
	@LoginRequired
	public String update(ShiroRole role, @RequestParam(value = "perms", required=false) List<Long> perms, ModelMap model) {
		Result data = null;
		HashSet<ShiroPermission> permissions = new HashSet<>();
		if(perms != null && perms.size() > 0){

            for(Long pid: perms){
				ShiroPermission p = new ShiroPermission();
                p.setId(pid);
				permissions.add(p);
            }
        }

        if (ShiroRole.ADMIN_ID == role.getId()) {
			data = Result.failure("管理员角色不可编辑");
        } else {
            roleService.update(role, permissions);
            data = Result.success();
        }
        model.put("data", data);
        return "redirect:/admin/role/list";
	}
	
	@RequestMapping("/activate")
	@ResponseBody
	@LoginRequired
	public Result activate(Long id, Boolean active) {
		Result ret = Result.failure("操作失败");
		if (id != null && id != ShiroRole.ADMIN_ID) {
			roleService.activate(id, active);
			ret = Result.success();
		}
		return ret;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	@LoginRequired
	public Result delete(@RequestParam("id") Long id) {
		Result ret;
		if (ShiroRole.ADMIN_ID == id) {
			ret = Result.failure("管理员不能操作");
        }else if(roleService.delete(id)){
        	ret = Result.success();
        }else{
        	ret = Result.failure("该角色不能被操作");
        }
		return ret;

	}
}
