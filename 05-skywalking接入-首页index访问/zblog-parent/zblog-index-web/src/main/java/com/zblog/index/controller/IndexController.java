package com.zblog.index.controller;

import com.zblog.basics.service.UserService;
import com.zblog.post.service.PostService;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.lang.Consts;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.MD5;
import com.zblog.util.tool.TokenUtil;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class IndexController extends BaseController {

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Reference
    UserService userService;

    @Reference
    PostService postService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    private ServletContext servletContext;


    @Value("${loginMaxAge:3600}")
    int loginMaxAge;

    @LoginRequired
    @RequestMapping("getUsername")
    @ResponseBody
    public String getUsername(HttpServletRequest request) throws Exception {


//        String token = CookieUtil.getCookie(request, TokenUtil.token).getValue();
//        String apstr = (String) redisTemplate.opsForHash().get(CacheConsts.token, token);
//        System.out.println(apstr);
//        AccountProfile accountProfile = JSONUtil.parseObject(apstr,AccountProfile.class);
//        String signDb = accountProfile.getSignDb();
//
//        String userName = userService.getUserName(accountProfile.getUsername(),signDb);
//        System.out.println("userService.getUserName():"+userName);


        Integer channelId = 1;
        String order = "order";

        Set<Integer> excludeChannelIds = new HashSet<>();

        if (channelId <= 0) {
//            List<MtoChannel> channels = channelService.findAll(Consts.STATUS_CLOSED);
//            if (channels != null) {
//                channels.forEach((c) -> excludeChannelIds.add(c.getId()));
//            }
        }


        Pageable pageable = wrapPageable();
        long count = postService.count();

        Set<Long> excludeChannelIds2 = new HashSet<>();
        excludeChannelIds2.add(2l);
        excludeChannelIds2.add(3l);
        postService.findMapByIds(excludeChannelIds2);

        PageModuls pm = new PageModuls();
        DataPageVO<PostVO> postVODataPageVO = postService.paging0(pm, channelId, excludeChannelIds);

        DataPageVO<PostVO> postVODataPageVO1 = postService.pagingByAuthorId(new PageModuls(), 3);
        Page<PostVO> postVOS = PageUtils.transformPage(postVODataPageVO1);
        System.out.println(""+postVOS);

        DataPageVO<PostVO> paging = postService.paging(new PageModuls(), channelId, excludeChannelIds);
        Page<PostVO> result = PageUtils.transformPage(paging);

        return ""+count;
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(String username,
                        String password,
                        @RequestParam(value = "rememberMe",defaultValue = "0") Boolean rememberMe,
                        ModelMap model,
                        HttpServletRequest request, HttpServletResponse response) {
        AccountProfile accountProfile = userService.login(username, MD5.md5(password));

        AfterLoginHandler(request,response,accountProfile);
        return null;
    }

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public String register(UserVO post, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        post.setAvatar(Consts.AVATAR);
        userService.register(post);
        AccountProfile accountProfile = userService.login(post.getUsername(), MD5.md5(post.getPassword()));
        AfterLoginHandler(request,response,accountProfile);

        return "success";
    }

    private String AfterLoginHandler(HttpServletRequest request, HttpServletResponse response,AccountProfile accountProfile){
        //缓存中记录用户信息  设置cookie
        String token = TokenUtil.createToken();
        redisTemplate.opsForHash().put(CacheConsts.token,token, JSONUtil.toJSONString(accountProfile));

        CookieUtil.setCookie(request,response,TokenUtil.token,token,loginMaxAge);
        return null;
    }

    @RequestMapping(value= {"/", "/index"})
    public String root(ModelMap model, HttpServletRequest request) {
        String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
        int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
        model.put("order", order);
        model.put("pageNo", pageNo);

        return view(Views.INDEX);
    }


}
