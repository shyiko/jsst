package jsst.junit.client.aspects;

import jsst.core.client.aspects.HTTPDispatcherAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
@Aspect
public class JUnitHTTPDispatcherAspect extends HTTPDispatcherAspect {

    @Pointcut(value = "execution(@org.junit.* * *(..))")
    @Override
    public void method() {}
}
