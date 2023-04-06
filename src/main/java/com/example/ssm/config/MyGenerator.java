package com.example.ssm.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyGenerator {
    public static void main(String[] args) {
        AutoGenerator autoGenerator = new AutoGenerator();

        DataSourceConfig datasource =new DataSourceConfig();
        datasource.setDriverName("com.mysql.cj.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/message_management_system_db");
        datasource.setUsername("root");
        datasource.setPassword("zxc123...");
        autoGenerator.setDataSource(datasource);


//        --------

        //设置全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir")+"/src/main/java/");
        //设置生成完毕后是否打开生成代码所在的目录
        globalConfig.setOpen(false);
        globalConfig.setAuthor("aliang");
//设置作者
//设置是否覆盖原始生成的文件
        globalConfig.setFileOverride(true);
//设置数据层接口名，%s为占位符，指代模块名称
        globalConfig.setMapperName("%sMapper");
        globalConfig.setIdType(IdType.ASSIGN_ID);
        autoGenerator.setGlobalConfig(globalConfig);
//设置Id生成策略
        PackageConfig packageInfo = new PackageConfig();
        packageInfo.setParent("com.example.ssm");
//设置生成的包
//设置实体类包名
//        packageInfo.setEntity("domain");
//
//        packageInfo.setMapper("dao");
        autoGenerator.setPackageInfo(packageInfo);
        StrategyConfig strategyConfig = new StrategyConfig();

        strategyConfig.setNaming(NamingStrategy.underline_to_camel); //决定 类名是否转成驼峰进行
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);

//        strategyConfig.setInclude("admin_tab");  //设置生成几个表
        strategyConfig.setRestControllerStyle(true);
    autoGenerator.setStrategy(strategyConfig);



//        -------
        autoGenerator.execute();
    }
}
