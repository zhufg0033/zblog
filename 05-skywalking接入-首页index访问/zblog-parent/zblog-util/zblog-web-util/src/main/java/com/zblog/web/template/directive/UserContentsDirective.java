/**
 *
 */
package com.zblog.web.template.directive;

import com.zblog.post.service.PostService;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.web.template.DirectiveHandler;
import com.zblog.web.template.TemplateDirective;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 根据作者取文章列表
 *
 * @author langhsu
 *
 */
@Component
public class UserContentsDirective extends TemplateDirective {
    @Reference
	private PostService postService;

	@Override
	public String getName() {
		return "user_contents";
	}

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        long userId = handler.getInteger("userId", 0);
        PageModuls pageModuls = wrapPageModuls(handler);

        DataPageVO<PostVO> postVODataPageVO = postService.pagingByAuthorId(pageModuls, userId);

        Page<PostVO> result = PageUtils.transformPage(postVODataPageVO);
        handler.put(RESULTS, result).render();
    }

}
