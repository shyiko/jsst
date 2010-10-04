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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public class FrontController extends HttpServlet {

    private static TestRunner testRunner;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        testRunner = new ReflectionTestRunner();
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
