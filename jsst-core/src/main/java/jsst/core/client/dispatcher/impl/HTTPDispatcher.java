package jsst.core.client.dispatcher.impl;

import jsst.core.client.dispatcher.Dispatcher;
import jsst.core.client.dispatcher.DispatcherException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public class HTTPDispatcher implements Dispatcher<String> {

    public static final String REQUEST_PARAM_CLASS_NAME = "className";
    public static final String REQUEST_PARAM_METHOD_NAME = "methodName";

    private String url;

    public HTTPDispatcher(String url) {
        this.url = url;
    }

    @Override
    public String dispatch(Method method) throws DispatcherException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.setParameter(REQUEST_PARAM_CLASS_NAME, method.getDeclaringClass().getCanonicalName());
        postMethod.setParameter(REQUEST_PARAM_METHOD_NAME, method.getName());
        try {
            httpClient.executeMethod(postMethod);
            return postMethod.getResponseBodyAsString();
        } catch (IOException e) {
            throw new DispatcherException("Exception caught while sending request to \"" + url + "\"", e);
        } finally {
            postMethod.releaseConnection();
        }
    }
}
