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
package jsst.junit.client.aspects;

import jsst.core.client.aspects.HTTPDispatcherAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author stanley.shyiko@gmail.com
 * @version Oct 3, 2010
 */
@Aspect
public class JUnitHTTPDispatcherAspect extends HTTPDispatcherAspect {

    @Pointcut(value = "execution(@org.junit.* * *(..))")
    @Override
    public void method() {}
}
