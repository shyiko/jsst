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
package jsst.core.client.dispatcher.impl;

import jsst.core.client.dispatcher.ServerResponse;

/**
 * @author stanley.shyiko@gmail.com
 * @version 2010-11-08
 */
public class HTTPServerResponse implements ServerResponse<String> {

    private String url;
    private String message;
    private int statusCode;

    public HTTPServerResponse(String url, String message, int statusCode) {
        this.url = url;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
