package org.luksze;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
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
    private Server server;

    Application() {
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

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    public void startWithoutBlocking() throws Exception {
        startIt();
    }

    private void start() throws Exception {
        Server server = startIt();
        server.join();
        LOGGER.info("Jetty server exited");
    }

    private Server startIt() throws Exception {
        QueuedThreadPool pool = new QueuedThreadPool(6, 1);
        pool.setName("http-worker");
        Server server = new Server(pool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(startPort);
        server.addConnector(connector);
        server.setHandler(constructWebAppContext());
        server.start();
        LOGGER.info("Jetty server started");
        LOGGER.debug("Jetty web server port: {}", startPort);
        LOGGER.debug("Port to stop Jetty with the 'stop' operation: {}", stopPort);
        return server;
    }

    private WebAppContext constructWebAppContext() throws URISyntaxException {
        WebAppContext root = new WebAppContext();
        String webDir = webDir().toURI().toString();
        LOGGER.info("Web dir {}", webDir);
        String resourceBase = webDirStringRepresentation(webDir);
        String descriptor = resourceBase + "/WEB-INF/web.xml";
        root.setContextPath("/");
        root.setDescriptor(descriptor);
        root.setResourceBase(resourceBase);
        root.setParentLoaderPriority(true);
        LOGGER.info("Descriptor: {}, resource base: {}", descriptor, resourceBase);
        return root;
    }

    private String webDirStringRepresentation(String webDir) throws URISyntaxException {
        int i = nrOfExclamationMarkInside(webDir);
        LOGGER.info("Nr of exclamation sign {}", i);
        if (i == 2) {
            return withoutLastExclamationMark(webDir);
        }
        return webDir;
    }

    private String withoutLastExclamationMark(String webDir) {
        int i = webDir.lastIndexOf('!');
        return webDir.substring(0, i) + webDir.substring(i + 1);
    }

    static int nrOfExclamationMarkInside(String webDir) {
        int value = 0;
        String worker = webDir;
        int position;
        while ((position = worker.indexOf('!')) != -1) {
            worker = worker.substring(position + 1);
            value++;
        }
        return value;
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
