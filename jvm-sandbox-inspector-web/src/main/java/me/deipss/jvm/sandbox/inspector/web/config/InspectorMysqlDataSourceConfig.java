package me.deipss.jvm.sandbox.inspector.web.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@ConfigurationProperties(prefix = "inspector.mysql")
@Configuration
@MapperScan(value = "me.deipss.jvm.sandbox.inspector.web.dal.mapper", sqlSessionTemplateRef = "inspectorSqlSessionTemplate")
public class InspectorMysqlDataSourceConfig {

    public static final String INSPECTOR_TRANSACTION_TEMPLATE = "inspectorTransactionTemplate";
    public static final String INSPECTOR_TRANSACTION_MANAGER = "inspectorTransactionManager";
    public static final String INSPECTOR_SQL_SESSION_TEMPLATE = "inspectorSqlSessionTemplate";
    public static final String INSPECTOR_DATA_SOURCE = "inspectorDataSource";
    @Value("${inspector.mysql.username}")
    private String username;
    @Value("${inspector.mysql.password}")
    private String password;
    @Value("${inspector.mysql.url}")
    private String url;

    @Bean("inspectorSqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("inspectorDataSource") DataSource ds) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        // mybatis配置
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        // sql打印
        mybatisConfiguration.setLogImpl(StdOutImpl.class);
        factoryBean.setConfiguration(mybatisConfiguration);
        // mybatis handle 指定
        factoryBean.setTypeHandlersPackage("edu.java.deipss.sql.dal.handler");
        // 分页插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        factoryBean.setPlugins(interceptor);
        factoryBean.setDataSource(ds);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml")
        );
        return factoryBean.getObject();
    }

    @Bean(INSPECTOR_TRANSACTION_TEMPLATE)
    public TransactionTemplate transactionTemplate(@Qualifier("inspectorTransactionManager") PlatformTransactionManager tx) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(tx);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setTimeout(30000);
        return transactionTemplate;
    }


    @Bean(INSPECTOR_TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(@Qualifier("inspectorDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean(INSPECTOR_SQL_SESSION_TEMPLATE)
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("inspectorSqlSessionFactory") SqlSessionFactory factory) {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(factory);
        return sqlSessionTemplate;
    }

    @Bean(INSPECTOR_DATA_SOURCE)
    public DataSource dataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setPassword(password);
        mysqlDataSource.setUser(username);
        mysqlDataSource.setUrl(url);
        return mysqlDataSource;
    }

}
