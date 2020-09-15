package com.zblog.index.controller.user;


import com.zblog.post.service.PostService;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.Consts;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.jboss.netty.channel.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author langhsu
 */
@RestController
@RequestMapping("/user")
public class FavorController extends BaseController {
    @Reference
    private PostService postService;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 收藏文章
     * @param id
     * @return
     */
    @RequestMapping("/favor")
    @LoginRequired
    public Result favor(Long id, HttpServletRequest request) {
        Result data = Result.failure("操作失败");
        if (id != null) {
            try {
                AccountProfile up = getProfile(request);
                postService.favor(up.getId(), id);
                sendMessage(up.getId(), id);
                data = Result.success();
            } catch (Exception e) {
                data = Result.failure(e.getMessage());
            }
        }
        return data;
    }

    /**
     * 取消收藏
     * @param id
     * @return
     */
    @RequestMapping("/unfavor")
    @LoginRequired
    public Result unfavor(Long id, HttpServletRequest request) {
        Result data = Result.failure("操作失败");
        if (id != null) {
            try {
                AccountProfile up = getProfile(request);
                postService.unfavor(up.getId(), id);
                data = Result.success();
            } catch (Exception e) {
                data = Result.failure(e.getMessage());
            }
        }
        return data;
    }

    /**
     * 发送通知
     * @param userId
     * @param postId
     */
    private void sendMessage(long userId, long postId) {
        //TODO 转成MQ发送事件
//        MessageEvent event = new MessageEvent("MessageEvent" + System.currentTimeMillis());
//        event.setFromUserId(userId);
//        event.setEvent(Consts.MESSAGE_EVENT_FAVOR_POST);
//        // 此处不知道文章作者, 让通知事件系统补全
//        event.setPostId(postId);
//        applicationContext.publishEvent(event);
    }
}
