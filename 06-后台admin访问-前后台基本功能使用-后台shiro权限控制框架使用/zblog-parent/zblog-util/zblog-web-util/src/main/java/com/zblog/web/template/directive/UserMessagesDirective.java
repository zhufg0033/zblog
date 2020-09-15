/**
 *
 */
package com.zblog.web.template.directive;

import com.zblog.post.service.MessageService;
import com.zblog.post.vo.MessageVO;
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
 * 查询用户消息列表
 *
 * @author landy
 * @since 3.0
 */
@Component
public class UserMessagesDirective extends TemplateDirective {
    @Reference
	private MessageService messageService;

	@Override
	public String getName() {
		return "user_messages";
	}

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        long userId = handler.getInteger("userId", 0);
        PageModuls pageModuls = wrapPageModuls(handler);

        DataPageVO<MessageVO> messageVODataPageVO = messageService.pagingByUserId(pageModuls, userId);
        Page<MessageVO> result = PageUtils.transformPage(messageVODataPageVO);
        handler.put(RESULTS, result).render();
    }

}
