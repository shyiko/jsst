package jsst.core.client.handler.impl;

import jsst.core.client.handler.Handler;
import jsst.core.client.handler.HandlerException;
import jsst.core.client.handler.Status;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public class HTTPHandler implements Handler<String> {

    public static final String RESPONSE_STATUS = "status";
    public static final String RESPONSE_THROWABLE = "throwable";

    @Override
    public void handle(String response) throws HandlerException {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String jsonStatus = jsonObject.getString(RESPONSE_STATUS);
            Status status;
            try {
                status = Status.valueOf(jsonStatus);
            } catch (IllegalArgumentException e) {
                throw new HandlerException("Couldn't recognize status \"" + jsonStatus + "\"", e);
            }
            switch (status) {
                case SUCCESS:
                    return;
                case FAIL:
                    String throwable = jsonObject.getString(RESPONSE_THROWABLE);
                    throw new AssertionError(throwable);
                default:
                    throw new UnsupportedOperationException();
            }
        } catch (JSONException e) {
            throw new HandlerException("Exception occurred while parsing server response", e);
        }
    }
}
