package com.yxhl.platform.common.service.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.yxhl.platform.common.exception.SystemException;

/**
 * 数据源配置
 */
@Configuration
public class DatasourceConfig {

    /**
     * Write data source druid data source.
     *
     * @return the druid data source
     */
    @Primary
    @Bean(name = "writeDataSource")
    @ConfigurationProperties("spring.datasource.write")
    public DruidDataSource writeDataSource() {
        return new DruidDataSource();
    }

    /**
     * Read data source druid data source.
     *
     * @return the druid data source
     */
    @Bean(name = "readDataSource")
    @ConfigurationProperties("spring.datasource.read")
    public DruidDataSource readDataSource() {
        return new DruidDataSource();
    }

    /**
     * Dynamic data source data source.
     *
     * @return the data source
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setWriteDataSource(writeDataSource());
        dynamicDataSource.setReadDataSource(readDataSource());

        return dynamicDataSource;
    }

    /**
     * Dynamic transaction manager data source transaction manager.
     *
     * @param dynamicDataSource the dynamic data source
     * @return the data source transaction manager
     */
    @Bean(name = "dynamicTransactionManager")
    public DataSourceTransactionManager dynamicTransactionManager(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return new DynamicDataSourceTransactionManager(dynamicDataSource);
    }

    /**
     * Dynamic sql session factory sql session factory.
     *
     * @param dynamicDataSource the dynamic data source
     * @param properties        the properties
     * @return the sql session factory
     */
    @Bean
    @ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
    public SqlSessionFactory dynamicSqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource,
            MybatisProperties properties) {

//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dynamicDataSource);
//        sessionFactory.setVfs(SpringBootVFS.class);
//        sessionFactory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
//        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(properties.getConfigLocation()));
//        sessionFactory.setMapperLocations(properties.resolveMapperLocations());

        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
            sessionFactory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasText(properties.getConfigLocation())) {
            sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(properties.getConfigLocation()));
        }
        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            sessionFactory.setMapperLocations(properties.resolveMapperLocations());
        }

        //翻页插件
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType(DBType.MYSQL.getDb());
        paginationInterceptor.setLocalPage(true);
        sessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor
        });
        //全局配置
        GlobalConfiguration globalConfig = new GlobalConfiguration();
        globalConfig.setDbType(DBType.MYSQL.getDb());
        globalConfig.setIdType(IdType.INPUT.getKey());
        globalConfig.setDbColumnUnderline(true);
        //热加载
        //globalConfig.setRefresh(true);
        //自动填充
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        sessionFactory.setGlobalConfig(globalConfig);

        try {
            return sessionFactory.getObject();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
