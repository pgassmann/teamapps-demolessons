package org.teamapps.demolessons.basics.p2_application.l03_servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class MyServlet extends HttpServlet {
    private final ServletNotificationManager myNotificationManager;

    public MyServlet(ServletNotificationManager notificationManager) {
        myNotificationManager = notificationManager;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // curl example:
        // curl http://localhost:8080/api/get-foo?param=get

        PrintWriter writer = response.getWriter();

        System.out.println("api call: GET " + request.getPathInfo());

        writer.println("Hello Servlet!");
        myNotificationManager.postNotification("api call: GET " + request.getPathInfo());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // curl example:
        // curl -d "param1=value1&param2=value2" http://localhost:8080/api/post-bar?param0=get
        // curl -X POST -d "@/tmp/testfile" http://localhost:8080/api/post-bar?param0=get

        String postPathInfo = request.getRequestURI();
        PrintWriter respWriter = response.getWriter();

        Map<String, String[]> parameterMap = request.getParameterMap();
        System.out.println("api call: POST " + postPathInfo);
        myNotificationManager.postNotification("api call: POST " + postPathInfo);
        respWriter.println("Request URI: " + postPathInfo);
        respWriter.println("Params ");
        parameterMap.forEach((param, values) -> {
            respWriter.println("Parameter: " + param);

            for (String value : values) {
                respWriter.println("Value: " + value);
            }
        });

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        respWriter.println("POST on '" + postPathInfo +"' successful");

    }
}
