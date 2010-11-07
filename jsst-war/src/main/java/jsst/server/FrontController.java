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
package jsst.server;

import jsst.core.client.dispatcher.impl.HTTPDispatcher;
import jsst.core.client.handler.Status;
import jsst.core.client.handler.impl.HTTPHandler;
import jsst.server.config.TestConfiguration;
import jsst.server.result.FailTestResult;
import jsst.server.result.TestResult;
import jsst.server.runner.TestRunner;
import jsst.server.runner.impl.ReflectionTestRunner;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author stanley.shyiko@gmail.com
 * @version Oct 3, 2010
 */
public class FrontController extends HttpServlet {

    private static final String ENVIRONMENT_PROPERTIES_FILE = "jsst-env.properties";
    private static TestRunner testRunner;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initSystemProperties();
        testRunner = new ReflectionTestRunner();
    }

    private void initSystemProperties() throws ServletException {
        URL resource = FrontController.class.getClassLoader().getResource(ENVIRONMENT_PROPERTIES_FILE);
        if (resource != null) {
            Properties properties = new Properties();
            try {
                InputStream inputStream = resource.openStream();
                try {
                    properties.load(inputStream);
                    Set<Map.Entry<Object,Object>> entries = properties.entrySet();
                    for (Map.Entry<Object, Object> entry : entries) {
                        System.setProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                } finally {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new ServletException("Unable to load system properties from " +
                        ENVIRONMENT_PROPERTIES_FILE + " file", e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String className = request.getParameter(HTTPDispatcher.REQUEST_PARAM_CLASS_NAME);
        String methodName = request.getParameter(HTTPDispatcher.REQUEST_PARAM_METHOD_NAME);
        TestConfiguration testConfiguration = new TestConfiguration(className, methodName);
        TestResult testResult = testRunner.run(testConfiguration);
        makeResponse(response, testResult);
    }

    private void makeResponse(HttpServletResponse response, TestResult testResult) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        try {
            JSONObject jsonObject = convertToJSON(testResult);
            jsonObject.write(writer);
        } catch (JSONException e) {
            throw new ServletException(e);
        } finally {
            writer.close();
        }
    }

    private JSONObject convertToJSON(TestResult testResult) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Status status = testResult.getStatus();
        jsonObject.put(HTTPHandler.RESPONSE_STATUS, status.name());
        switch (status) {
            case SUCCESS:
                break;
            case FAIL:
                Throwable throwable = ((FailTestResult) testResult).getThrowable();
                jsonObject.put(HTTPHandler.RESPONSE_THROWABLE, convertToString(throwable));
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return jsonObject;
    }

    private String convertToString(Throwable throwable) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            PrintStream printStream = new PrintStream(stream);
            throwable.printStackTrace(printStream);
            return stream.toString();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {}
        }
    }
}
