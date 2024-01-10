# 问题记录

## http 没有上报

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

## http response 获取

- https://stackoverflow.com/questions/8933054/how-to-read-and-copy-the-http-servlet-response-output-stream-content-for-logging

## dubbo response 获取

- invocation.setResponse(MethodUtils.invokeMethod(event.object, "getValue"));
