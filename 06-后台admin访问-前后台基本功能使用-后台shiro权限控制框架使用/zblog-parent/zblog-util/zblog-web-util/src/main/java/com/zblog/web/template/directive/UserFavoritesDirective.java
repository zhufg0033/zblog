/**
 *
 */
package com.zblog.web.template.directive;

import com.zblog.post.service.FavoriteService;
import com.zblog.post.vo.FavoriteVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.web.template.DirectiveHandler;
import com.zblog.web.template.TemplateDirective;
import com.zblog.web.utils.PageUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 根据作者取收藏列表
 *
 * @author landy
 * @since 3.0
 */
@Component
public class UserFavoritesDirective extends TemplateDirective {
    @Reference
	private FavoriteService favoriteService;

	@Override
	public String getName() {
		return "user_favorites";
	}

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        long userId = handler.getInteger("userId", 0);
        PageModuls pageModuls = wrapPageModuls(handler);

        DataPageVO<FavoriteVO> favoriteVODataPageVO = favoriteService.pagingByUserId(pageModuls, userId);
        Page<FavoriteVO> result = PageUtils.transformPage(favoriteVODataPageVO);
        handler.put(RESULTS, result).render();
    }

}
