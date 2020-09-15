package com.zblog.post.vo;

import com.zblog.sharedb.entity.MtoMessage;
import com.zblog.sharedb.vo.UserVO;

/**
 * @author langhsu on 2015/8/31.
 */
public class MessageVO extends MtoMessage {
    // extend
    private UserVO from;
    private PostVO post;

    public UserVO getFrom() {
        return from;
    }

    public void setFrom(UserVO from) {
        this.from = from;
    }

    public PostVO getPost() {
        return post;
    }

    public void setPost(PostVO post) {
        this.post = post;
    }
}
