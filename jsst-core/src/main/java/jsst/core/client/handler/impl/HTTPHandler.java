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
package jsst.core.client.handler.impl;

import jsst.core.client.dispatcher.impl.HTTPServerResponse;
import jsst.core.client.handler.Handler;
import jsst.core.client.handler.HandlerException;
import jsst.core.client.handler.Status;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author stanley.shyiko@gmail.com
 * @version Oct 3, 2010
 */
public class HTTPHandler implements Handler<HTTPServerResponse> {

    public static final String RESPONSE_STATUS = "status";
    public static final String RESPONSE_THROWABLE = "throwable";

    @Override
    public void handle(HTTPServerResponse response) throws HandlerException {
        if (response.getStatusCode() == 404)
            throw new HandlerException("Page \"" + response.getUrl() + "\" is not available. " +
                    "Seems like war with tests is either not deployed or " +
                    "is deployed under different context-path from the one in jsst.properties");
        try {
            JSONObject jsonObject = new JSONObject(response.getMessage());
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
