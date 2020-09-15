/**
 *
 */
package com.zblog.web.template.directive;

import com.zblog.post.service.CommentService;
import com.zblog.post.vo.CommentVO;
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
 * 根据作者取评论列表
 *
 * @author landy
 * @since 3.0
 */
@Component
public class UserCommentsDirective extends TemplateDirective {
    @Reference
	private CommentService commentService;

	@Override
	public String getName() {
		return "user_comments";
	}

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        long userId = handler.getInteger("userId", 0);
        PageModuls pageModuls = wrapPageModuls(handler);

        DataPageVO<CommentVO> commentVODataPageVO = commentService.pagingByAuthorId(pageModuls, userId);
        Page<CommentVO> result = PageUtils.transformPage(commentVODataPageVO);
        handler.put(RESULTS, result).render();
    }

}
