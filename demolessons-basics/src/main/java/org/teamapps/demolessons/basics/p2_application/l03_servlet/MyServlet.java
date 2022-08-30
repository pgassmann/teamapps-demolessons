package org.teamapps.demolessons.basics.p2_application.l03_servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

// Servlets can be used for communication with a browser, for example for using external services or web sites
public class MyServlet extends HttpServlet {
    private final ServletNotificationManager myNotificationManager;

    public MyServlet(ServletNotificationManager notificationManager) {
        myNotificationManager = notificationManager;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // for testing:
        //    either:    type in the console (curl is a command line tool for Linux and can be used for sending HTTP requests):
        //                  curl localhost:8080/api/fooget?param=get
        //    or:         put the URL "localhost:8080/api/fooget?param=get" in an extra tab of your browser

        PrintWriter writer = response.getWriter();

        System.out.println("api call: GET " + request.getPathInfo());

        writer.println("Hello Servlet!");
        myNotificationManager.postNotification("api call: GET " + request.getPathInfo());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // for testing:
        //    type in the console (curl is a command line tool for Linux and can be used for sending HTTP requests):
        //       curl -X POST -d "@/tmp/parameter-testfile.txt" localhost:8080/api/barpost?param1=get

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
