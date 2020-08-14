# rebuildZblog
重构mblog为分布式zblog

maven架构为：
- zblog-parent  父pom 定义jar包全局版本号和打包信息
    - zblog-sharedb 数据库po/dao
    - zblog-util  util模块
        - zblog-common-util  通用util模块
        - zblog-service-util server-util模块
        - zblog-web-util  网关util模块
    - zblog-basic  系统及用户server模块
        - zblog-basic-api  接口、bean   
        - zblog-basic-service 业务、数据   7071
    - zblog-post   文章server模块
        - zblog-post-api   接口 
        - zblog-post-service 业务、数据    7072
    - zblog-admin-web 后管网关模块    8072
    - zblog-index-web 前台网关模块    8071
    - zblog-tool 工具模块  generator生成mapper通用
    - 后续更新。。。
    


    