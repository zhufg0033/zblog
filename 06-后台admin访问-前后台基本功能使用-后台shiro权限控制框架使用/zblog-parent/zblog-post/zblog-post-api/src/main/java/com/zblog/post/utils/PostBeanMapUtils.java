/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.post.utils;


import com.zblog.post.vo.*;
import com.zblog.sharedb.entity.*;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.lang.Consts;
import org.springframework.beans.BeanUtils;

/**
 * @author langhsu
 */
public class PostBeanMapUtils {

    private static String[] USER_IGNORE = new String[]{"password", "extend", "roles"};

    public static UserVO copy(MtoUser po) {
        if (po == null) {
            return null;
        }
        UserVO ret = new UserVO();
        BeanUtils.copyProperties(po, ret, USER_IGNORE);
        return ret;
    }

    public static CommentVO copy(MtoComment po) {
        CommentVO ret = new CommentVO();
        BeanUtils.copyProperties(po, ret);
        ret.setCreated(po.getCreated());
        return ret;
    }

    public static PostVO copy(MtoPost po) {
        PostVO d = new PostVO();
        BeanUtils.copyProperties(po, d);
        return d;
    }

    public static MessageVO copy(MtoMessage po) {
        MessageVO ret = new MessageVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static FavoriteVO copy(MtoFavorite po) {
        FavoriteVO ret = new FavoriteVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static PostTagVO copy(MtoPostTag po) {
        PostTagVO ret = new PostTagVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static TagVO copy(MtoTag po) {
        TagVO ret = new TagVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static String[] postOrder(String order) {
        String[] orders;
        switch (order) {
            case Consts.order.HOTTEST:
                orders = new String[]{"comments", "views", "created"};
                break;
            case Consts.order.FAVOR:
                orders = new String[]{"favors", "created"};
                break;
            default:
                orders = new String[]{"weight", "created"};
                break;
        }
        return orders;
    }

    public static String postOrderStr(String orderType,String order) {
        String orderStr;
        switch (order) {
            case Consts.order.HOTTEST:
                orderStr = "comments "+orderType+",views "+orderType+",created "+orderType;
                break;
            case Consts.order.FAVOR:
                orderStr = "favors "+orderType+",created "+orderType;
                break;
            default:
                orderStr = "weight "+orderType+",created "+orderType;
                break;
        }
        return orderStr;
    }

}
