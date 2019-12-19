package org.teamapps.demo.lessons.l16_servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class MyServlet extends HttpServlet {
    private ServletNotificationManager myNotificationManager;

    public MyServlet(ServletNotificationManager notificationManager) {
        myNotificationManager = notificationManager;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // curl example:
        // curl localhost:8080/api/fooget?param=get

        PrintWriter writer = response.getWriter();

        System.out.println("api call: GET " + request.getPathInfo());

        writer.println("Hello Servlet!");
        myNotificationManager.postNotification("api call: GET " + request.getPathInfo());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // curl example:
        // curl -X POST -d "@/tmp/testfile" localhost:8080/api/barpost?param=get

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
