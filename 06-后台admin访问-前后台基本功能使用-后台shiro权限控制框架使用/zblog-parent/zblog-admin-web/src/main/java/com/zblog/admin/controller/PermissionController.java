package com.zblog.admin.controller;


import com.zblog.basics.service.PermissionService;
import com.zblog.basics.vo.PermissionTree;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author - langhsu
 * @create - 2018/5/18
 */
@RestController
@RequestMapping("/admin/permission")
public class PermissionController extends BaseController {
    @Reference
    private PermissionService permissionService;

    @GetMapping("/tree")
    @LoginRequired
    public List<PermissionTree> tree() {
        return permissionService.tree();
    }
}
