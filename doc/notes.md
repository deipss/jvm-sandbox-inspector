# 1. 兼容jvm-sandbox1.4.0版本

下图是1.4.0和1.3.3的版本对比，截取了与类加载器相关的片段：
![jvm-sandbx-140.png](img%2Fjvm-sandbx-140.png)
因为jvm-sandbox1.4.0版本将类加载器完全隔离了，所以使用Dubbo的RpcContext进行跨机器的链路上下文传递时，需要
将线程上下文的类加载器切换，结束后再切换回来：

```java

ClassLoader loader=Thread.currentThread().getContextClassLoader();
        RpcContext.set("","");
        ····
        Thread.currentThread().setContextClassLoader(loader);

```

# 2. uk唯一性

uk（union key）：调用一次的标识，全局唯一，实现如下：

```java
 getLocalIp()+"_"+System.currentTimeMillis()+"_"+invokeId;
```

使用当前机器的IP，拼接当前毫秒，最后加一个AtomicInteger类型的自增 invokeId，在jvm-sandbox中的源代码如下：

```java
private final AtomicInteger invokeIdSequencer=new AtomicInteger(1000);
        ····
final int invokeId=invokeIdSequencer.getAndIncrement();

```

# 3. 取消http header 参数传递

类似Okhttp等http通信组件，调用一些关键接口，如微信支付，不能传递链路信息，存在拉起支付失败的风险


# 4. IO流的复制


对于IO流增强时，注意提前将IO复制，不然会再现NPE问题


# 5. 调试

```shell
nohup  java -Xms512m -Xmx512m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5008 -jar jvm-sandbox-inspector-web-1.0.1-SNAPSHOT.jar > jvm-sandbox-inspector-web-nohup.log 2>&1 &
nohup java -Xms512m -Xmx512m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5007 -jar jvm-sandbox-inspector-debug-provider-1.0.1-SNAPSHOT.jar > jvm-sandbox-inspector-provider-nohup.log 2>&1 &
nohup java -Xms512m -Xmx512m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5006 -jar jvm-sandbox-inspector-debug-consumer-1.0.1-SNAPSHOT.jar  > jvm-sandbox-inspector-consumer-nohup.log 2>&1 &

```