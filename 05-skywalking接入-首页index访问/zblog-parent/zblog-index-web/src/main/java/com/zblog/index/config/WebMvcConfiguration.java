package com.zblog.index.config;

import com.zblog.index.interceptors.AuthInterceptor;
import com.zblog.util.conf.SiteOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    AuthInterceptor authInterceptor;

    @Autowired
    private SiteOptions siteOptions;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //加载登录拦截过滤器
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/dist/**", "/store/**", "/static/**", "/theme/*/dist/**");
//        //加在
//        super.addInterceptors(registry);
    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //加载静态资源
        String location = "file:///" + siteOptions.getLocation();
        registry.addResourceHandler("/dist/**")
                .addResourceLocations("classpath:/static/dist/");
        registry.addResourceHandler("/theme/*/dist/**")
                .addResourceLocations("classpath:/templates/")
                .addResourceLocations(location + "/storage/templates/");
        registry.addResourceHandler("/storage/avatars/**")
                .addResourceLocations(location + "/storage/avatars/");
        registry.addResourceHandler("/storage/thumbnails/**")
                .addResourceLocations(location + "/storage/thumbnails/");
    }
}
