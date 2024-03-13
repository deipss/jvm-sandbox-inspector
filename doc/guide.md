# 1. 开发一个插件



# 2. 启用或停用插件


# 3. 问题排查及日志查看
与jvm-sandbox的日志隔离，利用其logback的配置，日志从以下目录中查询

${user.home}/logs/sandbox/inspector.log

# 4. 关键配置
global_config.json配置文件
- heartUrl 心跳上报的URL
- dataSendUrl 数据上报的URL
- enablePlugins 启用的插件
- pluginsPath 插件所有jar包地址
```json

{
  "heartUrl": "http://192.168.0.2:8088/api/inspector/heartBeat",
  "dataSendUrl": "http://192.168.0.2:8088/api/inspector/dataSync",
  "enablePlugins": [
    "http"
  ],
  "pluginsPath": "/opt/sandbox/sandbox-module/inspector/inspector-plugins.jar"
}
```