/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the GNU Lesser General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * @author stanley.shyiko@gmail.com
 * @version Oct 4, 2010
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