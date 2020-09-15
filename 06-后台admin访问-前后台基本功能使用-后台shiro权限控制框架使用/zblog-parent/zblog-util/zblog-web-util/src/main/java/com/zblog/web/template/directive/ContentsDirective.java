/**
 *
 */
package com.zblog.web.template.directive;

import com.zblog.post.service.ChannelService;
import com.zblog.post.service.PostService;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.entity.MtoChannel;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.lang.Consts;
import com.zblog.web.template.DirectiveHandler;
import com.zblog.web.template.TemplateDirective;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文章内容查询
 * <p>
 * 示例：
 * 请求：http://mtons.com/index?order=newest&pn=2
 * 使用：@contents group=x pn=pn order=order
 *
 * @author langhsu
 */
@Component
public class ContentsDirective extends TemplateDirective {
    @Reference
    private PostService postService;
    @Reference
    private ChannelService channelService;

    @Override
    public String getName() {
        return "contents";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer channelId = handler.getInteger("channelId", 0);
        String order = handler.getString("order", Consts.order.NEWEST);

        Set<Integer> excludeChannelIds = new HashSet<>();

        if (channelId <= 0) {
            List<MtoChannel> channels = channelService.findAll(Consts.STATUS_CLOSED);
            if (channels != null) {
                channels.forEach((c) -> excludeChannelIds.add(c.getId()));
            }
        }

        PageModuls pageModuls = wrapPageModuls(handler, PostBeanMapUtils.postOrderStr(Sort.Direction.DESC.name(),order));
        DataPageVO<PostVO> result = postService.paging(pageModuls, channelId, excludeChannelIds);

        //返回给前端的的page对象已经写死，这里强转一下
        Page<PostVO> resultPage = PageUtils.transformPage(result);
        handler.put(RESULTS, resultPage).render();
    }
}
