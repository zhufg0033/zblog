/**
 *
 */
package com.zblog.index.controller;


import com.zblog.post.service.TagService;
import com.zblog.post.vo.PostTagVO;
import com.zblog.post.vo.TagVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 标签
 * @author langhsu
 *
 */
@Controller
public class TagController extends BaseController {
    @Reference
    private TagService tagService;

    @RequestMapping("/tags")
    public String index(ModelMap model) {
        PageModuls pageModuls = wrapPageable("updated desc");
        DataPageVO<TagVO> page = tagService.pagingQueryTags(pageModuls);
        model.put("results", PageUtils.transformPage(page));
        return view(Views.TAG_INDEX);
    }

    @RequestMapping("/tag/{name}")
    public String tag(@PathVariable String name, ModelMap model) {
        PageModuls pageModuls = wrapPageable("weight desc");
        DataPageVO<PostTagVO> page = tagService.pagingQueryPosts(pageModuls, name);
        model.put("results", PageUtils.transformPage(page));

        model.put("name", name);
        return view(Views.TAG_VIEW);
    }

}
