package com.zblog.post.vo;

import com.zblog.sharedb.entity.MtoPostTag;

import java.io.Serializable;

/**
 * @author : langhsu
 */

public class PostTagVO extends MtoPostTag implements Serializable {
    private static final long serialVersionUID = 73354108587481371L;

    private PostVO post;

    public PostVO getPost() {
        return post;
    }

    public void setPost(PostVO post) {
        this.post = post;
    }


}
