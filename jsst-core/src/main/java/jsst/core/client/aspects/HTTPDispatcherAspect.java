package jsst.core.client.aspects;

import jsst.core.client.dispatcher.Dispatcher;
import jsst.core.client.dispatcher.impl.HTTPDispatcher;
import jsst.core.client.handler.Handler;
import jsst.core.client.handler.impl.HTTPHandler;
import org.aspectj.lang.annotation.Aspect;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author shyiko
 * @since Oct 4, 2010
 */
@Aspect
public abstract class HTTPDispatcherAspect extends DispatcherAspect {

    private Handler handler;
    private Dispatcher dispatcher;

    public HTTPDispatcherAspect() {
        String webServerUrl;
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("jsst.properties");
        if (resource == null)
            throw new AssertionError("Cannot find file jsst.properties on classpath");
        InputStream inputStream;
        try {
            inputStream = resource.openStream();
            try {
                Properties properties = new Properties();
                properties.load(inputStream);
                webServerUrl = properties.getProperty("web.server.url");
                if (webServerUrl == null || webServerUrl.isEmpty())
                    throw new AssertionError("Property web.server.url should not be null or empty");
                if (!webServerUrl.endsWith("/"))
                    webServerUrl += "/";
                webServerUrl += "FrontController";
            } finally {
                inputStream.close();
            }
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        dispatcher = new HTTPDispatcher(webServerUrl);
        handler = new HTTPHandler();
    }

    @Override
    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }
}