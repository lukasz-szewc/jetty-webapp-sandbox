package org.luksze;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class GaussianServlet extends HttpServlet {

    private final Random random = new Random();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serveIt(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serveIt(request, response);
    }

    private void serveIt(HttpServletRequest request, HttpServletResponse response) throws IOException {
        double v = random.nextGaussian() * 100 + 1000;
        long round = Math.round(v);
        try {
            Thread.sleep(round);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        response.getWriter().print(round);
    }
}
