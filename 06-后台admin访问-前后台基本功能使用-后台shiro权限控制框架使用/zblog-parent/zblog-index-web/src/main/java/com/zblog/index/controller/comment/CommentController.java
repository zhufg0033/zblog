/**
 *
 */
package com.zblog.index.controller.comment;


import com.zblog.post.service.CommentService;
import com.zblog.post.vo.CommentVO;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.jboss.netty.channel.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author langhsu
 */
@RestController
@RequestMapping("/comment")
@ConditionalOnProperty(name = "site.controls.comment", havingValue = "true", matchIfMissing = true)
public class CommentController extends BaseController {
    @Reference
    private CommentService commentService;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/list/{toId}")
    public Page<CommentVO> view(@PathVariable Long toId) {
        PageModuls pageModuls = new PageModuls("id desc");
        return PageUtils.transformPage(commentService.pagingByPostId(pageModuls, toId));
    }

    @RequestMapping("/submit")
    @LoginRequired
    public Result post(Long toId, String text, HttpServletRequest request) {

        long pid = ServletRequestUtils.getLongParameter(request, "pid", 0);

        if (toId <= 0 || StringUtils.isBlank(text)) {
            return Result.failure("操作失败");
        }

        AccountProfile profile = getProfile(request);

        CommentVO c = new CommentVO();
        c.setPostId(toId);
        c.setContent(HtmlUtils.htmlEscape(text));
        c.setAuthorId(profile.getId());

        c.setPid(pid);

        commentService.post(c);

        sendMessage(profile.getId(), toId, pid);

        return Result.successMessage("发表成功");
    }

    @RequestMapping("/delete")
    @LoginRequired
    public Result delete(@RequestParam(name = "id") Long id, HttpServletRequest request) {
        Result data;
        try {
            commentService.delete(id, getProfile(request).getId());
            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        return data;
    }

    /**
     * 发送通知
     *
     * @param userId
     * @param postId
     */
    private void sendMessage(long userId, long postId, long pid) {
        //todo  时间通知  等接入mq再处理
//        MessageEvent event = new MessageEvent("MessageEvent");
//        event.setFromUserId(userId);
//
//        if (pid > 0) {
//            event.setEvent(Consts.MESSAGE_EVENT_COMMENT_REPLY);
//            event.setCommentParentId(pid);
//        } else {
//            event.setEvent(Consts.MESSAGE_EVENT_COMMENT);
//        }
//        // 此处不知道文章作者, 让通知事件系统补全
//        event.setPostId(postId);
//        applicationContext.publishEvent(event);
    }
}