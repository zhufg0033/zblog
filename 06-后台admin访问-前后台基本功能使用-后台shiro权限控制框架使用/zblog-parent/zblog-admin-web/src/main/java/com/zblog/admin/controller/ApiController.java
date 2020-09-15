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


import com.zblog.basics.service.UserService;
import com.zblog.post.service.CommentService;
import com.zblog.post.service.PostService;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.CommentVO;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.util.tool.MD5;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.BeanMapUtils;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 侧边栏数据加载
 *
 * @author langhsu
 */
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
    @Reference
    private PostService postService;
    @Reference
    private CommentService commentService;
    @Reference
    private UserService userService;


    @PostMapping(value = "/login")
    @LoginRequired
    public Result login(HttpServletRequest request, HttpServletResponse response, String username, String password, ModelMap model) {
        Result<AccountProfile> result = userService.login(username, MD5.md5(password));
        AfterLoginHandler(request,response,result,model);
        return result;
    }

    @RequestMapping("/posts")
    @LoginRequired
    public Page<PostVO> posts(HttpServletRequest request) {
        String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
        int channelId = ServletRequestUtils.getIntParameter(request, "channelId", 0);
        DataPageVO<PostVO> paging = postService.paging(wrapPageable(PostBeanMapUtils.postOrderStr(Sort.Direction.DESC.name(), order)), channelId, null);
        return PageUtils.transformPage(paging);
    }

    @RequestMapping(value = "/latest_comments")
    @LoginRequired
    public List<CommentVO> latestComments(@RequestParam(name = "size", defaultValue = "6") Integer size) {
        return commentService.findLatestComments(size);
    }
}
