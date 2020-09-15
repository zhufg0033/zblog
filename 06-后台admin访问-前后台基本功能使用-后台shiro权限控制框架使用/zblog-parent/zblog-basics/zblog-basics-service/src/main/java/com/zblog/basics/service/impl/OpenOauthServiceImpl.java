/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.basics.service.impl;


import com.zblog.basics.service.OpenOauthService;
import com.zblog.basics.utils.BasicsBeanMapUtils;
import com.zblog.basics.vo.OpenOauthVO;
import com.zblog.sharedb.dao.subtreasury.MtoUserDao;
import com.zblog.sharedb.dao.subtreasury.MtoUserOauthDao;
import com.zblog.sharedb.entity.MtoUser;
import com.zblog.sharedb.entity.MtoUserOauth;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.tool.MD5;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 第三方登录授权管理
 * @author langhsu on 2015/8/12.
 */
@Service
@Transactional
public class OpenOauthServiceImpl implements OpenOauthService {
    @Autowired
    private MtoUserOauthDao mtoUserOauthDao;
    @Autowired
    private MtoUserDao mtoUserDao;

    @Override
    public UserVO getUserByOauthToken(String oauth_token) {
        MtoUserOauth thirdToken = mtoUserOauthDao.findByAccessToken(oauth_token);
        MtoUser po = mtoUserDao.selectByPrimaryKey(thirdToken.getId());
        return BasicsBeanMapUtils.copy(po);
    }

    @Override
    public OpenOauthVO getOauthByToken(String oauth_token) {
        MtoUserOauth po = mtoUserOauthDao.findByAccessToken(oauth_token);
        OpenOauthVO vo = null;
        if (po != null) {
            vo = new OpenOauthVO();
            BeanUtils.copyProperties(po, vo);
        }
        return vo;
    }

    @Override
    public OpenOauthVO getOauthByUid(long userId) {
        MtoUserOauth po = mtoUserOauthDao.findByUserId(userId);
        OpenOauthVO vo = null;
        if (po != null) {
            vo = new OpenOauthVO();
            BeanUtils.copyProperties(po, vo);
        }
        return vo;
    }

    @Override
    public boolean checkIsOriginalPassword(long userId) {
        MtoUserOauth po = mtoUserOauthDao.findByUserId(userId);
        if (po != null) {
            MtoUser optional = mtoUserDao.selectByPrimaryKey(userId);

            String pwd = MD5.md5(po.getAccessToken());
            // 判断用户密码 和 登录状态
            if (optional != null && pwd.equals(optional.getPassword())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveOauthToken(OpenOauthVO oauth) {
        MtoUserOauth po = new MtoUserOauth();
        BeanUtils.copyProperties(oauth, po);
        mtoUserOauthDao.insert(po);
    }

	@Override
	public OpenOauthVO getOauthByOauthUserId(String oauthUserId) {
		MtoUserOauth po = mtoUserOauthDao.findByOauthUserId(oauthUserId);
        OpenOauthVO vo = null;
        if (po != null) {
            vo = new OpenOauthVO();
            BeanUtils.copyProperties(po, vo);
        }
        return vo;
	}

}
