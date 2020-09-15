package com.zblog.web.template.directive;


import com.zblog.util.conf.SiteOptions;
import com.zblog.util.lang.Consts;
import com.zblog.web.template.DirectiveHandler;
import com.zblog.web.template.TemplateDirective;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 控制器
 * created by langhsu
 * on 2019/1/18
 */
@Component
public class ControlsDirective extends TemplateDirective {
    @Autowired
    private SiteOptions siteOptions;

    @Override
    public String getName() {
        return "controls";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        String control = handler.getString("name");

        if (StringUtils.isBlank(control)) {
            return;
        }

        String value = BeanUtils.getProperty(siteOptions.getControls(), control);
        if ("true".equalsIgnoreCase(value)) {
            handler.render();
        } else {
            // 当控制器 post 为关闭时, 继续判断角色
            //TODO  注掉了  角色需要再改
//            if ("post".equalsIgnoreCase(control) && SecurityUtils.getSubject() != null && SecurityUtils.getSubject().hasRole(Consts.ROLE_ADMIN)) {
//                handler.render();
//            }



        }
    }
}
