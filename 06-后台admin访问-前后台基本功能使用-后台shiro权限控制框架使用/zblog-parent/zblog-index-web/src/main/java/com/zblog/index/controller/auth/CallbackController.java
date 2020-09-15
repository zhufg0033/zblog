package com.zblog.index.controller.auth;


import com.zblog.basics.oauth.*;
import com.zblog.basics.oauth.utils.OpenOauthBean;
import com.zblog.basics.oauth.utils.TokenUtil;
import com.zblog.basics.service.OpenOauthService;
import com.zblog.basics.service.UserService;
import com.zblog.basics.vo.OpenOauthVO;
import com.zblog.index.controller.Views;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.conf.Result;
import com.zblog.util.exception.MtonsException;
import com.zblog.util.lang.Consts;
import com.zblog.util.tool.FilePathUtils;
import com.zblog.util.tool.ImageUtils;
import com.zblog.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 第三方登录回调
 *
 * @author langhsu
 */
@Slf4j
@Controller
@RequestMapping("/oauth/callback")
@ConditionalOnProperty(name = "site.controls.register", havingValue = "true", matchIfMissing = true)
public class CallbackController extends BaseController {
    private static final String SESSION_STATE = "_SESSION_STATE_";

    @Reference
    private OpenOauthService openOauthService;
    @Reference
    private UserService userService;

    /**
     * 跳转到微博进行授权
     *
     * @param request
     * @param response
     * @author A蛋壳  2015年9月12日 下午3:05:54
     */
    @RequestMapping("/call_weibo")
    public void callWeibo(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            APIConfig.getInstance().setOpenid_sina(siteOptions.getValue(Consts.WEIBO_CLIENT_ID));
            APIConfig.getInstance().setOpenkey_sina(siteOptions.getValue(Consts.WEIBO_CLIENT_SERCRET));
            APIConfig.getInstance().setRedirect_sina(siteOptions.getValue(Consts.WEIBO_CALLBACK));

            String state = TokenUtil.randomState();
            request.getSession().setAttribute(SESSION_STATE, state);
            response.sendRedirect(OauthSina.me().getAuthorizeUrl(state));
        } catch (Exception e) {
            throw new MtonsException("跳转到微博授权接口时发生异常");
        }
    }

    /**
     * 微博回调
     *
     * @param code
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/weibo")
    public String callback4Weibo(String code, String state, HttpServletRequest request, ModelMap model) throws Exception {
        // --
        String session_state = (String) request.getSession().getAttribute(SESSION_STATE);
        // 取消了授权
        if (StringUtils.isBlank(state) || StringUtils.isBlank(session_state) || !state.equals(session_state) || StringUtils.isBlank(code)) {
            throw new MtonsException("缺少必要的参数");
        }
        request.getSession().removeAttribute(SESSION_STATE);
        // --

        OpenOauthBean openOauthBean = null;
        try {
            openOauthBean = OauthSina.me().getUserBeanByCode(code);
        } catch (Exception e) {
            throw new MtonsException("解析信息时发生错误");
        }

        OpenOauthVO openOauth = new OpenOauthVO();
        openOauth.setOauthCode(code);
        openOauth.setAccessToken(openOauthBean.getAccessToken());
        openOauth.setExpireIn(openOauthBean.getNickname());
        openOauth.setOauthUserId(openOauthBean.getOauthUserId());
        openOauth.setOauthType(openOauthBean.getOauthType());
        openOauth.setRefreshToken(openOauthBean.getRefreshToken());
        openOauth.setUsername(openOauthBean.getUsername());
        openOauth.setNickname(openOauthBean.getNickname());
        openOauth.setAvatar(openOauthBean.getAvatar());

        // 判断是否存在绑定此accessToken的用户
        OpenOauthVO thirdToken = openOauthService.getOauthByOauthUserId(openOauth.getOauthUserId());
        if (thirdToken == null) {
            model.put("open", openOauth);
            return view(Views.OAUTH_REGISTER);
        }
        String username = userService.get(thirdToken.getUserId()).getUsername();
        return login(username, thirdToken.getAccessToken());
    }

    /**
     * 跳转到QQ互联授权界面
     *
     * @param request
     * @param response
     * @author A蛋壳  2015年9月12日 下午3:28:21
     */
    @RequestMapping("/call_qq")
    public void callQQ(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            APIConfig.getInstance().setOpenid_qq(siteOptions.getValue(Consts.QQ_APP_ID));
            APIConfig.getInstance().setOpenkey_qq(siteOptions.getValue(Consts.QQ_APP_KEY));
            APIConfig.getInstance().setRedirect_qq(siteOptions.getValue(Consts.QQ_CALLBACK));

            String state = TokenUtil.randomState();
            request.getSession().setAttribute(SESSION_STATE, state);
            response.sendRedirect(OauthQQ.me().getAuthorizeUrl(state));
        } catch (Exception e) {
            throw new MtonsException("跳转到QQ授权接口时发生异常");
        }
    }

    /**
     * QQ回调
     *
     * @param code
     * @param request
     * @return
     */
    @RequestMapping("/qq")
    public String callback4QQ(String code, String state, HttpServletRequest request, ModelMap model) {
        // --
        String session_state = (String) request.getSession().getAttribute(SESSION_STATE);
        // 取消了授权
        if (StringUtils.isBlank(state) || StringUtils.isBlank(session_state) || !state.equals(session_state) || StringUtils.isBlank(code)) {
            throw new MtonsException("缺少必要的参数");
        }
        request.getSession().removeAttribute(SESSION_STATE);
        // --

        OpenOauthBean openOauthBean = null;
        try {
            openOauthBean = OauthQQ.me().getUserBeanByCode(code);
        } catch (Exception e) {
            throw new MtonsException("解析信息时发生错误");
        }

        OpenOauthVO openOauth = new OpenOauthVO();
        openOauth.setOauthCode(code);
        openOauth.setAccessToken(openOauthBean.getAccessToken());
        openOauth.setExpireIn(openOauthBean.getNickname());
        openOauth.setOauthUserId(openOauthBean.getOauthUserId());
        openOauth.setOauthType(openOauthBean.getOauthType());
        openOauth.setUsername(openOauthBean.getUsername());
        openOauth.setNickname(openOauthBean.getNickname());
        openOauth.setAvatar(openOauthBean.getAvatar());

        // 判断是否存在绑定此accessToken的用户
        OpenOauthVO thirdToken = openOauthService.getOauthByOauthUserId(openOauth.getOauthUserId());

        if (thirdToken == null) {
            model.put("open", openOauth);
            return view(Views.OAUTH_REGISTER);
        }
        String username = userService.get(thirdToken.getUserId()).getUsername();
        return login(username, thirdToken.getAccessToken());
    }

    /**
     * 跳转到github授权页面
     *
     * @param request
     * @param response
     */
    @RequestMapping("/call_github")
    public void callGithub(HttpServletRequest request, HttpServletResponse response) {
        //设置github的相关
        APIConfig.getInstance().setOpenid_github(siteOptions.getValue(Consts.GITHUB_CLIENT_ID));
        APIConfig.getInstance().setOpenkey_github(siteOptions.getValue(Consts.GITHUB_SECRET_KEY));
        APIConfig.getInstance().setRedirect_github(siteOptions.getValue(Consts.GITHUB_CALLBACK));

        try {
            response.setContentType("text/html;charset=utf-8");
            String state = TokenUtil.randomState();
            request.getSession().setAttribute(SESSION_STATE, state);
            response.sendRedirect(OauthGithub.me().getAuthorizeUrl(state));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * github回调
     *
     * @param code
     * @param state
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/github")
    public String callback4Github(String code, String state, HttpServletRequest request, ModelMap model) {
        // -- 类似于预防crsf攻击
        String session_state = (String) request.getSession().getAttribute(SESSION_STATE);
        // 取消了授权
        if (StringUtils.isBlank(state) || StringUtils.isBlank(session_state) || !state.equals(session_state) || StringUtils.isBlank(code)) {
            throw new MtonsException("缺少必要的参数");
        }
        request.getSession().removeAttribute(SESSION_STATE);

        OpenOauthBean openOauthBean = null;
        try {
            //通过code获取openid和用户信息
            openOauthBean = OauthGithub.me().getUserBeanByCode(code);
        } catch (Exception e) {
            throw new MtonsException("解析信息时发生错误");
        }

        OpenOauthVO openOauth = new OpenOauthVO();
        openOauth.setOauthCode(code);
        openOauth.setAccessToken(openOauthBean.getAccessToken());
        openOauth.setExpireIn(openOauthBean.getNickname());
        //openid
        openOauth.setOauthUserId(openOauthBean.getOauthUserId());
        openOauth.setOauthType(openOauthBean.getOauthType());
        openOauth.setUsername(openOauthBean.getUsername());
        openOauth.setNickname(openOauthBean.getNickname());
        openOauth.setAvatar(openOauthBean.getAvatar());

        // 判断是否存在绑定此accessToken的用户
        OpenOauthVO thirdToken = openOauthService.getOauthByOauthUserId(openOauth.getOauthUserId());

        if (thirdToken == null) {
            model.put("open", openOauth);
            return view(Views.OAUTH_REGISTER);
        }
        String username = userService.get(thirdToken.getUserId()).getUsername();
        return login(username, thirdToken.getAccessToken());


    }

    /**
     * 跳转到豆瓣授权界面
     *
     * @param request
     * @param response
     * @author A蛋壳  2015年9月12日 下午3:09:39
     */
    @RequestMapping("/call_douban")
    public void callDouban(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            APIConfig.getInstance().setOpenid_douban(siteOptions.getValue(Consts.DOUBAN_API_KEY));
            APIConfig.getInstance().setOpenkey_douban(siteOptions.getValue(Consts.DOUBAN_SECRET_KEY));
            APIConfig.getInstance().setRedirect_douban(siteOptions.getValue(Consts.DOUBAN_CALLBACK));

            String state = TokenUtil.randomState();
            request.getSession().setAttribute(SESSION_STATE, state);
            response.sendRedirect(OauthDouban.me().getAuthorizeUrl(state));
        } catch (Exception e) {
            throw new MtonsException("跳转到豆瓣授权接口时发生异常");
        }
    }

    /**
     * 豆瓣回调
     *
     * @param code
     * @param state
     * @param request
     * @param model
     * @author A蛋壳  2015年9月12日 下午5:32:51
     */
    @RequestMapping("/douban")
    public String callBack4Douban(String code, String state, HttpServletRequest request, ModelMap model) {
        // --
        String session_state = (String) request.getSession().getAttribute(SESSION_STATE);
        // 取消了授权
        if (StringUtils.isBlank(state) || StringUtils.isBlank(session_state) || !state.equals(session_state) || StringUtils.isBlank(code)) {
            throw new MtonsException("缺少必要的参数");
        }
        request.getSession().removeAttribute(SESSION_STATE);
        // --

        OpenOauthBean openOauthBean = null;
        try {
            openOauthBean = OauthDouban.me().getUserBeanByCode(code);
        } catch (Exception e) {
            throw new MtonsException("解析信息时发生错误");
        }

        OpenOauthVO openOauth = new OpenOauthVO();
        openOauth.setOauthCode(code);
        openOauth.setAccessToken(openOauthBean.getAccessToken());
        openOauth.setExpireIn(openOauthBean.getNickname());
        openOauth.setOauthUserId(openOauthBean.getOauthUserId());
        openOauth.setOauthType(openOauthBean.getOauthType());
        openOauth.setUsername(openOauthBean.getUsername());
        openOauth.setNickname(openOauthBean.getNickname());
        openOauth.setAvatar(openOauthBean.getAvatar());

        // 判断是否存在绑定此accessToken的用户
        OpenOauthVO thirdToken = openOauthService.getOauthByOauthUserId(openOauth.getOauthUserId());

        if (thirdToken == null) {
            model.put("open", openOauth);
            return view(Views.OAUTH_REGISTER);
        }
        String username = userService.get(thirdToken.getUserId()).getUsername();
        return login(username, thirdToken.getAccessToken());
    }

    /**
     * 执行第三方注册绑定
     *
     * @param openOauth
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/bind_oauth")
    public String bindOauth(OpenOauthVO openOauth, HttpServletRequest request) throws Exception {
        OpenOauthVO thirdToken = openOauthService.getOauthByOauthUserId(openOauth.getOauthUserId());
        String username = openOauth.getUsername();

        // 已存在：提取用户信息，登录
        if (thirdToken != null) {
            username = userService.get(thirdToken.getUserId()).getUsername();
        } else { // 不存在：注册新用户，并绑定此token，登录
            UserVO user = userService.getByUsername(username);
            if (user == null) {
                UserVO u = userService.register(wrapUser(openOauth));

                // 将远程图片下载到本地
                String ava100 = Consts.avatarPath + getAvaPath(u.getId(), 100);
                byte[] bytes = ImageUtils.download(openOauth.getAvatar());
                String imagePath = storageFactory.get().writeToStore(bytes, ava100);
                userService.updateAvatar(u.getId(), imagePath);

                thirdToken = new OpenOauthVO();
                BeanUtils.copyProperties(openOauth, thirdToken);
                thirdToken.setUserId(u.getId());
                openOauthService.saveOauthToken(thirdToken);
            } else {
                username = user.getUsername();
            }
        }
        return login(username, openOauth.getAccessToken());
    }

    /**
     * 执行登录请求
     *
     * @param username
     * @param accessToken
     * @return
     */
    private String login(String username, String accessToken) {
        //TODO 第三方登录切库等实现
//        String view = view(Views.LOGIN);
//
//        if (StringUtils.isNotBlank(username)) {
//            Result<AccountProfile> result = executeLogin(username, accessToken, false);
//            if (result.isOk()) {
//                view = String.format(Views.REDIRECT_USER_HOME, result.getData().getId());
//            }
//            return view;
//        }
        throw new MtonsException("登录失败");
    }

    private UserVO wrapUser(OpenOauthVO openOauth) {
        UserVO user = new UserVO();
        user.setUsername(openOauth.getUsername());
        user.setName(openOauth.getNickname());
        user.setPassword(openOauth.getAccessToken());

        if (StringUtils.isNotBlank(openOauth.getAvatar())) {
            //FIXME: 这里使用网络路径，前端应做对应处理
            user.setAvatar(openOauth.getAvatar());
        } else {
            user.setAvatar(Consts.AVATAR);
        }
        return user;
    }

    public String getAvaPath(long uid, int size) {
        String base = FilePathUtils.getAvatar(uid);
        return String.format("/%s_%d.jpg", base, size);
    }

}
