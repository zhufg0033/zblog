package com.zblog.post.vo;

import com.zblog.sharedb.entity.MtoTag;

import java.io.Serializable;

/**
 * @author : langhsu
 */
public class TagVO extends MtoTag implements Serializable {
    private static final long serialVersionUID = -7787865229252467418L;

    private PostVO post;

    public PostVO getPost() {
        return post;
    }

    public void setPost(PostVO post) {
        this.post = post;
    }
}
