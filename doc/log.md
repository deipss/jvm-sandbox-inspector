# 问题记录

## http 没有上报
因为tomcat的RequestFacade在**instanceof HttpServletRequest**判断时，返回是空。这点很迷惑，因为在定义增强的类时，
使用的是javax.servlet.http.HttpServlet类，而RequestFacade确实是他的子类

## http response 获取


## dubbo response 获取