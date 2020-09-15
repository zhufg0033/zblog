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

import java.util.List;

import com.zblog.post.service.CommentService;
import com.zblog.post.vo.CommentVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.conf.Result;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author langhsu
 *
 */
@Controller("adminCommentController")
@RequestMapping("/admin/comment")
public class CommentController extends BaseController {
	@Reference
	private CommentService commentService;
	
	@RequestMapping("/list")
	@LoginRequired
	public String list(ModelMap model) {
		PageModuls pageable = wrapPageable();
		Page<CommentVO> page = PageUtils.transformPage(commentService.paging4Admin(pageable));
		model.put("page", page);
		return "/admin/comment/list";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	@LoginRequired
	public Result delete(@RequestParam("id") List<Long> id) {
		Result data = Result.failure("操作失败");
		if (id != null) {
			try {
				commentService.delete(id);
				data = Result.success();
			} catch (Exception e) {
				data = Result.failure(e.getMessage());
			}
		}
		return data;
	}
}
