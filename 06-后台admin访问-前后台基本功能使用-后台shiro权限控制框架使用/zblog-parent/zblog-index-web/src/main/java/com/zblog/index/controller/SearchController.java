/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.index.controller;


import com.zblog.post.service.PostSearchService;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 文章搜索
 * @author langhsu
 *
 */
@Controller
public class SearchController extends BaseController {
	@Reference
	private PostSearchService postSearchService;

	@RequestMapping("/search")
	public String search(String kw, ModelMap model) {
		PageModuls pageModuls = wrapPageable();
		try {
			if (StringUtils.isNotEmpty(kw)) {
				DataPageVO<PostVO> page = postSearchService.search(pageModuls, kw);
				model.put("results", PageUtils.transformPage(page));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("kw", kw);
		return view(Views.SEARCH);
	}
	
}
