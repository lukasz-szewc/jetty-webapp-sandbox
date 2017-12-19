package org.luksze;

import com.codahale.metrics.MetricRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.luksze.WholeAppListener.METRIC_REGISTRY;

public class HelloWorldServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MetricRegistry metricRegistry = (MetricRegistry) request.getServletContext().getAttribute(METRIC_REGISTRY);
        metricRegistry.histogram("histogram").update(1);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello World</h1>");
        out.close();
    }

}