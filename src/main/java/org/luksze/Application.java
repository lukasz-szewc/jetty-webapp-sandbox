package org.luksze;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

public class Application {

    private static final String WEBAPP_RESOURCES_LOCATION = "webapp";
    private static final int DEFAULT_PORT_STOP = 8090;
    private static final int DEFAULT_PORT_START = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final int startPort;
    private final int stopPort;

    private Application() {
        this(DEFAULT_PORT_START, DEFAULT_PORT_STOP);
    }

    private Application(int startPort, int stopPort) {
        this.startPort = startPort;
        this.stopPort = stopPort;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            new Application(Integer.valueOf(args[0]), Integer.valueOf(args[1])).start();
        } else {
            new Application().start();
        }
    }

    private void start() throws Exception {
        Server server = new Server(startPort);
        server.setHandler(constructWebAppContext());
        server.start();
        LOGGER.info("Jetty server started");
        LOGGER.debug("Jetty web server port: {}", startPort);
        LOGGER.debug("Port to stop Jetty with the 'stop' operation: {}", stopPort);
        server.join();
        LOGGER.info("Jetty server exited");
    }

    private WebAppContext constructWebAppContext() throws URISyntaxException {
        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setDescriptor(WEBAPP_RESOURCES_LOCATION + "/WEB-INF/web.xml");
        root.setResourceBase(webDir().toURI().toString());
        root.setParentLoaderPriority(true);
        return root;
    }

    private URL webDir() {
        URL webAppDir = currentThread().getContextClassLoader().getResource(WEBAPP_RESOURCES_LOCATION);
        if (webAppDir == null) {
            String message = format("No %s directory was found into the JAR file", WEBAPP_RESOURCES_LOCATION);
            throw new RuntimeException(message);
        }
        return webAppDir;
    }

}
