# springboot-dubbox
Springboot+dubbox+redis+rabbitMq

# springboot-dubbox
基于spring-boot dubbox搭建的java分布式系统
- 统一异常管理
- spring-data-redis整合
- 整合swagger-ui接口文档，访问地址 http://localhost:8187/platform-web/swagger-ui.html
- 定时任务
- 爬虫
- 邮件发送
- rabbitmq示例
- 多profile管理
- maven项目管理
- Mybatis多数据源
- logback记录日志
- 接口访问次数限制(待完善)
- druid sql监控

---

- 项目依赖Mysql、Zookeeper、Redis
- platform-web为管理平台接口
- platform-mobile为移动客户端接口
- platform-system为服务提供者
- 在application.yml中配置数据库连接、Redis连接及web访问端口
- 执行db-script中的数据库初始化脚本
- web-start.sh、provider-start.sh、mobile-start.sh为启动脚本


#手动安装jar包
mvn install:install-file -DgroupId=com.alibaba -DartifactId=dubbo -Dversion=2.8.4 -Dpackaging=jar -Dfile=dubbo-2.8.4.jar
mvn install:install-file -DgroupId=com.yxhl -DartifactId=jbarcode -Dversion=1.0.0 -Dpackaging=jar -Dfile=jbarcode-0.2.8.jar

