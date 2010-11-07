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
package jsst.server.runner.impl;

import jsst.server.config.TestConfiguration;
import jsst.server.result.FailTestResult;
import jsst.server.result.SuccessTestResult;
import jsst.server.result.TestResult;
import jsst.server.runner.TestRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author stanley.shyiko@gmail.com
 * @version Oct 3, 2010
 */
public class ReflectionTestRunner implements TestRunner {

    public Map<String, Object> tests = Collections.synchronizedMap(new HashMap<String, Object>());

    @Override
    public TestResult run(TestConfiguration configuration) {
        try {
            Object object = getTestObject(configuration.getClassName());
            Method method = object.getClass().getMethod(configuration.getMethodName());
            method.invoke(object);
        } catch (InvocationTargetException e) {
            return new FailTestResult(e.getCause());
        } catch (Throwable t) {
            return new FailTestResult(t);
        }
        return new SuccessTestResult();
    }

    public Object getTestObject(String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object result = tests.get(className);
        if (result == null) {
            Class testClass = Class.forName(className);
            result = testClass.newInstance();
            tests.put(className, result);
        }
        return result;
    }
}
