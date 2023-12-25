# 1. 简介

![Static Badge](https://img.shields.io/badge/jvm_sandbox-1.4.0-blue)
![Static Badge](https://img.shields.io/badge/http--yellow)
![Static Badge](https://img.shields.io/badge/dubbo-3.0-red)
![Static Badge](https://img.shields.io/badge/jdbc--green)
![Static Badge](https://img.shields.io/badge/rocket--mq--#CC0033)
![Static Badge](https://img.shields.io/badge/TTL-2.10.2-%23660066)


下图为一个是一个简要的用户下单流程

- 使用手机通过Http请求到交易线下单，交易会查询用户的信息以及门店的营业状态，再防重验证后，创建订单。
- 拿到交易订单号，去拉起支付的支付界面。

![flow.png](doc%2Fimg%2Fflow.png)

## 1.1. 需求

- 微服务架构下，除了自已的业务线，外部的业务不了解，对接时，参数不对时，都要去找日志，或是arthas watch 一下方法的调用
- user-core用户，常用的测试账号过期了，如何快速`MOCK接口`结果是检查通过，而不是过期
- 发现bug，提交bug报告时，需要手工编写入参、出参、异常信息等，能否`快速生成`
- SQL语句错误、分表分库策略失效导致全表扫描等问题主体发现

……

[jvm-sandbox-repeater](https://github.com/alibaba/jvm-sandbox-repeater)实现了流量的录制回放

# 2. 源代码模块介绍

| module                               | 用途                                                                                                                   |
|--------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| jvm-sandbox-inspector-agent          | 使用jvm-sandbox，实现一个JavaAgent模块，具体部署方法见[jvm-sandbox快速开始](https://github.com/alibaba/jvm-sandbox/wiki/USER-QUICK-START) |
| jvm-sandbox-inspector-data           | 数据同步模块（暂未实现），一般用于消费kafka的消息体（消息体中包含接口参数、响应等信息）保存到存储引擎中（ElasticSearch）。`不采样的话，数据量很大`                                  |
| jvm-sandbox-inspector-web            | 用户操作界面模块，查看链路，数据回放，上报配置等                                                                                             |
| jvm-sandbox-inspector-debug-consumer | 服务调试模块，服务消费者（rocketmq consumer，dubbo consumer，okhttp）                                                                |
| jvm-sandbox-inspector-debug-provider | 服务调试模块，服务提供者（rocketmq sent，dubbo provider，restful 接口）                                                                |

# 3. 分布式链路

论文地址如下：

- [ Dapper, a Large-Scale Distributed Systems Tracing Infrastructure](https://storage.googleapis.com/gweb-research2023-media/pubtools/pdf/36356.pdf  )

# 4. 阅读

- [代码细节](doc%2Fnotes.md)

- [开发指南](doc%2Fguide.md)