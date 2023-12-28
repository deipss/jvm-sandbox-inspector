package me.deipss.jvm.sandbox.inspector.agent.core.test.plugin;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class HttpServletImpl extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        log.info("http ,parameter={}",req.getParameter("test"));
    }

}
