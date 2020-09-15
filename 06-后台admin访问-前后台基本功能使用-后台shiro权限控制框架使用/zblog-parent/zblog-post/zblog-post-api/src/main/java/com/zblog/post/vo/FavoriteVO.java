package com.zblog.post.vo;

import com.zblog.sharedb.entity.MtoFavorite;

/**
 * @author langhsu on 2015/8/31.
 */
public class FavoriteVO extends MtoFavorite {
    // extend
    private PostVO post;

    public PostVO getPost() {
        return post;
    }

    public void setPost(PostVO post) {
        this.post = post;
    }
}
