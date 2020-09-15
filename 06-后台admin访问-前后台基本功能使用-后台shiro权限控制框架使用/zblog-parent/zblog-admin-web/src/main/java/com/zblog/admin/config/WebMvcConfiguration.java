package com.zblog.admin.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zblog.interceptors.AuthInterceptor;
import com.zblog.interceptors.BaseInterceptor;
import com.zblog.util.conf.SiteOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    AuthInterceptor authInterceptor;
    @Autowired
    BaseInterceptor baseInterceptor;
    @Autowired
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter;
    @Autowired
    private SiteOptions siteOptions;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //加载登录拦截过滤器
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/dist/**", "/error/**", "/store/**", "/static/**", "/theme/*/dist/**");
        //base信息
        registry.addInterceptor(baseInterceptor).addPathPatterns("/**").excludePathPatterns("/dist/**", "/error/**", "/store/**", "/static/**", "/theme/*/dist/**");


    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //加载静态资源
        String location = "file:///" + siteOptions.getLocation();
        registry.addResourceHandler("/dist/**")
                .addResourceLocations("classpath:/static/dist/");
        registry.addResourceHandler("/error/**")
                .addResourceLocations("classpath:/static/error/");
        registry.addResourceHandler("/theme/*/dist/**")
                .addResourceLocations("classpath:/templates/")
                .addResourceLocations(location + "/storage/templates/");
        registry.addResourceHandler("/storage/avatars/**")
                .addResourceLocations(location + "/storage/avatars/");
        registry.addResourceHandler("/storage/thumbnails/**")
                .addResourceLocations(location + "/storage/thumbnails/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter);
    }
}
