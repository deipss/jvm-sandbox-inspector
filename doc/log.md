# 1. 问题记录

## 1.1. http 没有上报

因为tomcat的RequestFacade在**instanceof HttpServletRequest**判断时，返回是空。这点很迷惑，因为在定义增强的类时，
使用的是javax.servlet.http.HttpServlet类，而RequestFacade确实是他的子类

```java

/**
 * Facade class that wraps a Coyote request object.
 * All methods are delegated to the wrapped request.
 *
 * @author Craig R. McClanahan
 * @author Remy Maucherat
 */
@SuppressWarnings("deprecation")
public class RequestFacade implements HttpServletRequest {

}
```

## 1.2. http response 获取

- https://stackoverflow.com/questions/8933054/how-to-read-and-copy-the-http-servlet-response-output-stream-content-for-logging

## 1.3. dubbo response 获取

- invocation.setResponse(MethodUtils.invokeMethod(event.object, "getValue"));

## 录制了被修改的数据

请求时的数据：
```json
{
  "orderId":"123231231",
  "skuCnt":"10",
  "userName":"Bob"
}
```

DTO的引用在某个方法中，userName字段被修改了

```json
{
    "orderId":"123231231",
    "skuCnt":"10",
    "userName":"Bob Alice"
}
```

录制时，使用的引用赋值，并不是把当前方法调用时所有参数值进行深拷贝，所以在录制的数据发送前，
字段值同时修改了。