package org.luksze;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncServlet extends HttpServlet {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync(request, response);
        executorService.submit(() -> {
            long computation = GaussianServlet.computation();
            try {
                asyncContext.getResponse().getWriter().print(computation);
            } catch (IOException e) {
                Thread.currentThread().interrupt();
            }
            asyncContext.complete();
        });
    }
}
