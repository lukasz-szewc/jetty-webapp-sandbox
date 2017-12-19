package org.luksze;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.TimeUnit;

public class WholeAppListener implements ServletContextListener {

    public static final String METRIC_REGISTRY = "metricRegistry";

    public WholeAppListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        MetricRegistry registry = new MetricRegistry();
        sce.getServletContext().setAttribute(METRIC_REGISTRY, registry);
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(registry)
                .outputTo(LoggerFactory.getLogger("com.example.metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(10, TimeUnit.SECONDS);
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

}
