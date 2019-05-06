package com.wechat.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/12
 */

@Configuration
@EnableTransactionManagement
@MapperScan(value = "com.wechat.dao.**.mapper")
public class DataBaseConfiguration implements EnvironmentAware {


    private Logger logger= LoggerFactory.getLogger(DataBaseConfiguration.class);

    @Resource
    private Environment env;

    private RelaxedPropertyResolver resolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.env=environment;
        this.resolver = new RelaxedPropertyResolver(environment,"spring.jdbc.");

    }

    //注册dataSource
    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource() throws SQLException {
        /*if (StringUtils.isEmpty(resolver.getProperty("url"))) {
            logger.error("Your database connection pool configuration is incorrect!"
                    + " Please check your Spring profile, current profiles are:"
                    + Arrays.toString(env.getActiveProfiles()));
            throw new ApplicationContextException(
                    "Database connection pool is not configured correctly");
        }*/
        DruidDataSource druidDataSource = new DruidDataSource();
        /*druidDataSource.setDriverClassName(resolver.getProperty("driverClassName"));
        druidDataSource.setUrl(resolver.getProperty("url"));
        druidDataSource.setUsername(resolver.getProperty("username"));
        druidDataSource.setPassword(resolver.getProperty("password"));*/
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://47.98.101.128:3306/wechat?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&useSSL=true");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
        druidDataSource.setFilters("stat,slf4j,wall");
        /*druidDataSource.setInitialSize(Integer.parseInt(resolver.getProperty("initialSize")));
        druidDataSource.setMinIdle(Integer.parseInt(resolver.getProperty("minIdle")));
        druidDataSource.setMaxActive(Integer.parseInt(resolver.getProperty("maxActive")));
        druidDataSource.setMaxWait(Integer.parseInt(resolver.getProperty("maxWait")));
        druidDataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(resolver.getProperty("timeBetweenEvictionRunsMillis")));
        druidDataSource.setMinEvictableIdleTimeMillis(Long.parseLong(resolver.getProperty("minEvictableIdleTimeMillis")));
        druidDataSource.setValidationQuery(resolver.getProperty("validationQuery"));
        druidDataSource.setTestWhileIdle(Boolean.parseBoolean(resolver.getProperty("testWhileIdle")));
        druidDataSource.setTestOnBorrow(Boolean.parseBoolean(resolver.getProperty("testOnBorrow")));
        druidDataSource.setTestOnReturn(Boolean.parseBoolean(resolver.getProperty("testOnReturn")));
        druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(resolver.getProperty("poolPreparedStatements")));
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(resolver.getProperty("maxPoolPreparedStatementPerConnectionSize")));*/
        //druidDataSource.setFilters(resolver.getProperty("filters"));
        return druidDataSource;
    }



    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        //mybatis分页
        PageHelper pageHelper = new PageHelper();
        Properties props = new Properties();
        props.setProperty("dialect", "mysql");
        props.setProperty("reasonable", "true");
        props.setProperty("supportMethodsArguments", "true");
        props.setProperty("returnPageInfo", "check");
        props.setProperty("params", "count=countSql");
        pageHelper.setProperties(props); //添加插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});

//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }


    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }


}
