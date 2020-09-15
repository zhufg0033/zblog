package com.zblog.util.sharedb;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HelloWoodes
 */
@Configuration
public class DataSourceProxyConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean("originblog2019")
    @ConfigurationProperties(prefix = "spring.datasource.zblog2019")
    public DataSource dataSourceMaster() {
        System.out.println("spring.datasource.zblog201--------------------------------------------------------------");
        return new DruidDataSource();
    }

    @Bean("originblog2020")
    @ConfigurationProperties(prefix = "spring.datasource.zblog2020")
    public DataSource dataSourceStorage() {
        return new DruidDataSource();
    }



    @Bean(name = "zblog2019")
    public DataSourceProxy masterDataSourceProxy(@Qualifier("originblog2019") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "zblog2020")
    public DataSourceProxy storageDataSourceProxy(@Qualifier("originblog2020") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }


    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("zblog2019") DataSource dataSourceZblog2019,
                                        @Qualifier("zblog2020") DataSource dataSourceZblog2020) {

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put(DataSourceKey.zblog2019.name(), dataSourceZblog2019);
        dataSourceMap.put(DataSourceKey.zblog2020.name(), dataSourceZblog2020);

        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSourceZblog2019);
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        DynamicDataSourceContextHolder.getDataSourceKeys().addAll(dataSourceMap.keySet());

        return dynamicRoutingDataSource;
    }

    @Bean("sqlSessionFactoryBean")
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean("sqlSessionFactoryDefault")
    @ConfigurationProperties(prefix = "masterful")
    public SqlSessionFactoryBean sqlSessionFactoryDefault(@Qualifier("zblog2019") DataSource dataSourceZblog2019) {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSourceZblog2019);
        return sqlSessionFactory;
    }

}