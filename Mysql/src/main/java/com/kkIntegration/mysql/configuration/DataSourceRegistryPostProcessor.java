package com.kkIntegration.mysql.configuration;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.kkIntegration.mysql.datasource.DataSourceContextHolder;
import com.kkIntegration.mysql.datasource.DynamicDataSource;
import com.kkIntegration.mysql.mybatis.CustomSqlSessionTemplate;
import com.kkIntegration.mysql.mybatis.MyMapperScannerRegistrar;
import com.kkIntegration.mysql.mybatis.MyScanner;
import com.kkIntegration.mysql.properties.DataSourceEntity;
import com.kkIntegration.mysql.properties.DataSourceProperties;
import com.kkIntegration.mysql.properties.XAType;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 数据源 自定义 自动注入
 * author: ckk
 * create: 2019-12-19 12:20
 */
//@Configuration
//@Order(Integer.MAX_VALUE)
@Component
public class DataSourceRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware {

    @Bean(name = "xatx")
    @Primary
    public JtaTransactionManager regTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        UserTransaction userTransaction = new UserTransactionImp();
        try {
            userTransaction.setTransactionTimeout(3000);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        JtaTransactionManager springTransactionManager = new JtaTransactionManager();
        springTransactionManager.setTransactionManager(userTransactionManager);
        springTransactionManager.setUserTransaction(userTransaction);
        springTransactionManager.setAllowCustomIsolationLevels(true);
        return springTransactionManager;

    }


    // 自定义数据源
    //@Autowired
    //DataSourceProperties dataSourceProperties;

    // spring 上下文
    //@Autowired
    //ApplicationContext applicationContext;

    // https://www.cnblogs.com/hello-yz/p/10938780.html
    private ResourceLoader resourceLoader;

    //@Autowired
    //DefaultListableBeanFactory defaultListableBeanFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        initPostDataSource(registry);
    }


    //@PostConstruct
    public void initPostDataSource(BeanDefinitionRegistry registry) {
        // 转换bean 注册工厂
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) registry;
        // 获取 上下文
        AnnotationConfigServletWebServerApplicationContext context = (AnnotationConfigServletWebServerApplicationContext) resourceLoader;
        // 获取环境
        StandardServletEnvironment environment = (StandardServletEnvironment) context.getEnvironment();
        // 获取 dataSource 配置
        DataSourceProperties dataSourceProperties = DataSourceProperties.loadEnvironment((OriginTrackedMapPropertySource) environment.getPropertySources().get("applicationConfig: [classpath:/application-xa.yml]"));
        int factoryType = 0;


        // 循环 数据源配置
        for (DataSourceEntity dataSourceEntity : dataSourceProperties.getDatasourceList()) {
            // DruidXA 类型
            if (dataSourceProperties.getXaType() == XAType.DruidXA) {
                // 手动注册一个配置好的bean
                initDruidXABean(dataSourceEntity, defaultListableBeanFactory);

            } else if (dataSourceProperties.getXaType() == XAType.MysqlXA) {
                // MysqlXA 类型
                // 手动注册一个配置好的bean
                try {
                    initMysqlXABean(dataSourceEntity, defaultListableBeanFactory);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (dataSourceProperties.getXaType() == XAType.DruidXA ||
                    dataSourceProperties.getXaType() == XAType.MysqlXA) {

                initDynamic(dataSourceEntity, defaultListableBeanFactory, factoryType);

                try {
                    // 初始化 SqlSessionFactoryBean
                    initFactory(dataSourceEntity, defaultListableBeanFactory);
                    // 初始化 SqlSessionTemplate
                    initTemplate(dataSourceEntity, defaultListableBeanFactory);

                    //initTransaction(dataSourceEntity,defaultListableBeanFactory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 模仿 @MapperScan 中 MapperScannerRegistrar 动作
                MyScanner myScanner = new MyScanner();
                myScanner.setBasePackages(dataSourceEntity.getDaoPackages().split(","));
                myScanner.setSqlSessionFactoryRef(dataSourceEntity.getBeanName() + "Factory");
                myScanner.setSqlSessionTemplateRef(dataSourceEntity.getBeanName() + "Template");
                //myScanner.setSqlSessionTemplateRef("customSqlSessionTemplate");
                // mybatis 扫描注入
                MyMapperScannerRegistrar.registerBeanDefinitions(registry, resourceLoader, myScanner);
            }
            factoryType++;

        }


        // 将上部分 MyScanner 和 initTemplate 注释掉
        // 打开以下两部分注释，可进行多数据库切换


        // 初始化 SqlSessionTemplate
/*
        try {
            initTemplate(dataSourceProperties, defaultListableBeanFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        // 循环 数据源配置
        /*for (DataSourceEntity dataSourceEntity : dataSourceProperties.getDatasourceList()) {

            if (dataSourceProperties.getXaType() == XAType.DruidXA ||
                    dataSourceProperties.getXaType() == XAType.MysqlXA) {


                // 模仿 @MapperScan 中 MapperScannerRegistrar 动作
                MyScanner myScanner = new MyScanner();
                myScanner.setBasePackages(dataSourceEntity.getDaoPackages().split(","));
                myScanner.setSqlSessionFactoryRef(dataSourceEntity.getBeanName() + "Factory");
                //myScanner.setSqlSessionTemplateRef(dataSourceEntity.getBeanName() + "Template");
                myScanner.setSqlSessionTemplateRef("customSqlSessionTemplate");
                // mybatis 扫描注入
                MyMapperScannerRegistrar.registerBeanDefinitions(registry, resourceLoader, myScanner);

            }

        }*/

    }

    private void initTransaction(DataSourceEntity dataSourceEntity, DefaultListableBeanFactory defaultListableBeanFactory) {
        DataSource dataSource = (DataSource) defaultListableBeanFactory.getBean(dataSourceEntity.getBeanName() + "Dynamic");

        if (defaultListableBeanFactory.containsBeanDefinition("transactionManager")) {

            defaultListableBeanFactory.removeBeanDefinition("transactionManager");
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
        // 构造参数
        beanDefinitionBuilder.addConstructorArgValue(dataSource);

        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition("transactionManager", beanDefinitionBuilder.getBeanDefinition());
    }

    private void initDynamic(DataSourceEntity dataSourceEntity, DefaultListableBeanFactory defaultListableBeanFactory, int factoryType) {

        Map<Object, Object> targetDataSources = new HashMap<>();

        AtomikosDataSourceBean defaultTargetDataSource = null;

        // 用于多数据源的切换名
        String[] sourceNames = new String[dataSourceEntity.getXaProperties().getUrls().size()];

        for(int i = 0 ,len = dataSourceEntity.getXaProperties().getUrls().size() ; i < len ; i++){
            String name = dataSourceEntity.getXaProperties().getUrls().get(i).getSourceName();
            // 获取第一个设置为默认源
            if(defaultTargetDataSource == null){
                defaultTargetDataSource = (AtomikosDataSourceBean) defaultListableBeanFactory.getBean(name);
            }
            targetDataSources.put(name, defaultListableBeanFactory.getBean(name));
            sourceNames[i] = name;
        }


        // 使用多数据源切换
        DataSourceContextHolder databaseContextHolder = new DataSourceContextHolder(sourceNames, dataSourceEntity.getXaProperties().getUrls().size(), factoryType);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
        // 构造参数
        beanDefinitionBuilder.addConstructorArgValue(databaseContextHolder);
        beanDefinitionBuilder.addPropertyValue("targetDataSources", targetDataSources);
        beanDefinitionBuilder.addPropertyValue("defaultTargetDataSource", defaultTargetDataSource);

        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(dataSourceEntity.getBeanName() + "Dynamic", beanDefinitionBuilder.getBeanDefinition());

    }


    // 初始化  DruidXA 数据源
    private void initDruidXABean(DataSourceEntity dataSourceEntity, DefaultListableBeanFactory defaultListableBeanFactory) {

        for(int i = 0 ,len = dataSourceEntity.getXaProperties().getUrls().size() ; i < len ; i++){
            //创建bean信息.
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(AtomikosDataSourceBean.class);
            beanDefinitionBuilder.addPropertyValue("xaDataSourceClassName", dataSourceEntity.getXaDataSourceClassName());
            beanDefinitionBuilder.addPropertyValue("beanName", dataSourceEntity.getXaProperties().getUrls().get(i).getSourceName());
            beanDefinitionBuilder.addPropertyValue("uniqueResourceName", dataSourceEntity.getXaProperties().getUrls().get(i).getSourceName());

            beanDefinitionBuilder.addPropertyValue("minPoolSize", dataSourceEntity.getMinPoolSize());
            beanDefinitionBuilder.addPropertyValue("maxPoolSize", dataSourceEntity.getMaxPoolSize());
            beanDefinitionBuilder.addPropertyValue("maxLifetime", dataSourceEntity.getMaxLifetime());
            beanDefinitionBuilder.addPropertyValue("borrowConnectionTimeout", dataSourceEntity.getBorrowConnectionTimeout());
            beanDefinitionBuilder.addPropertyValue("xaProperties", dataSourceEntity.getXaProperties().getAllXaProperties(i));

            //动态注册bean.
            defaultListableBeanFactory.registerBeanDefinition(dataSourceEntity.getXaProperties().getUrls().get(i).getSourceName(), beanDefinitionBuilder.getBeanDefinition());
        }

    }

    // 初始化  MysqlXA 数据源
    private void initMysqlXABean(DataSourceEntity dataSourceEntity, DefaultListableBeanFactory defaultListableBeanFactory) throws SQLException {

        for(int i = 0 ,len = dataSourceEntity.getXaProperties().getUrls().size() ; i < len ; i++){

            MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
            mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
            mysqlXADataSource.setUser(dataSourceEntity.getXaProperties().getUsername());
            mysqlXADataSource.setPassword(dataSourceEntity.getXaProperties().getPassword());

            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(AtomikosDataSourceBean.class);
            beanDefinitionBuilder.addPropertyValue("xaDataSource", mysqlXADataSource);
            beanDefinitionBuilder.addPropertyValue("uniqueResourceName", dataSourceEntity.getXaProperties().getUrls().get(i).getSourceName());
            beanDefinitionBuilder.addPropertyValue("minPoolSize", dataSourceEntity.getMinPoolSize());
            beanDefinitionBuilder.addPropertyValue("maxPoolSize", dataSourceEntity.getMaxPoolSize());
            beanDefinitionBuilder.addPropertyValue("borrowConnectionTimeout", dataSourceEntity.getBorrowConnectionTimeout());
            beanDefinitionBuilder.addPropertyValue("loginTimeout", dataSourceEntity.getLoginTimeout());
            beanDefinitionBuilder.addPropertyValue("maintenanceInterval", dataSourceEntity.getMaintenanceInterval());
            beanDefinitionBuilder.addPropertyValue("maxIdleTime", dataSourceEntity.getMaxIdleTime());
            beanDefinitionBuilder.addPropertyValue("testQuery", dataSourceEntity.getTestQuery());

            //动态注册bean.
            defaultListableBeanFactory.registerBeanDefinition(dataSourceEntity.getXaProperties().getUrls().get(i).getSourceName(), beanDefinitionBuilder.getBeanDefinition());
        }


    }


    // 初始化 sql Template
    private void initTemplate(DataSourceEntity dataSourceEntity, DefaultListableBeanFactory defaultListableBeanFactory) throws Exception {
        // 获取当前 Factory
        //SqlSessionFactoryBean sqlSessionFactoryBean = (SqlSessionFactoryBean) defaultListableBeanFactory.getBean(dataSourceEntity.getBeanName()+"Factory");
        DefaultSqlSessionFactory sqlSessionFactory = (DefaultSqlSessionFactory) defaultListableBeanFactory.getBean(dataSourceEntity.getBeanName() + "Factory");

        // Template
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
        // 构造参数
        beanDefinitionBuilder.addConstructorArgValue(sqlSessionFactory);

        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(dataSourceEntity.getBeanName() + "Template", beanDefinitionBuilder.getBeanDefinition());
    }

    // 初始化 sql Template
    private void initTemplate(DataSourceProperties dataSourceProperties, DefaultListableBeanFactory defaultListableBeanFactory) throws Exception {
        // 获取当前 Factory
        // Template
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(CustomSqlSessionTemplate.class);
        // 构造参数
        beanDefinitionBuilder.addConstructorArgValue((DefaultSqlSessionFactory) defaultListableBeanFactory.getBean(dataSourceProperties.getDatasourceList().get(0).getBeanName() + "Factory"));
        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition("customSqlSessionTemplate", beanDefinitionBuilder.getBeanDefinition());


        // Template
        CustomSqlSessionTemplate customSqlSessionTemplate = (CustomSqlSessionTemplate) defaultListableBeanFactory.getBean("customSqlSessionTemplate");

        Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();

        int factoryType  = 0;
        for (DataSourceEntity dataSourceEntity : dataSourceProperties.getDatasourceList()) {

            DefaultSqlSessionFactory sqlSessionFactory = (DefaultSqlSessionFactory) defaultListableBeanFactory.getBean(dataSourceEntity.getBeanName() + "Factory");
            sqlSessionFactoryMap.put(factoryType, sqlSessionFactory);
            factoryType++;
        }

        customSqlSessionTemplate.setTargetSqlSessionFactorys(sqlSessionFactoryMap);
    }


    // 初始化 Factory
    private void initFactory(DataSourceEntity dataSourceEntity, DefaultListableBeanFactory defaultListableBeanFactory) throws IOException {
        // 获取当前 数据源
        DataSource dataSource = (DataSource) defaultListableBeanFactory.getBean(dataSourceEntity.getBeanName() + "Dynamic");

        // Factory
        //创建bean信息.
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        beanDefinitionBuilder.addPropertyValue("dataSource", dataSource);
        beanDefinitionBuilder.addPropertyValue("mapperLocations",
                new PathMatchingResourcePatternResolver().getResources("classpath*:" + dataSourceEntity.getMapperPath()));


        //动态注册bean.
        defaultListableBeanFactory.registerBeanDefinition(dataSourceEntity.getBeanName() + "Factory", beanDefinitionBuilder.getBeanDefinition());

    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}
