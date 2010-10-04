package jsst.testng.client.aspects;

import jsst.core.client.aspects.HTTPDispatcherAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
@Aspect
public class TestNGHTTPDispatcherAspect extends HTTPDispatcherAspect {

    @Pointcut(value = "execution(@org.testng.annotations.* * *(..))")
    @Override
    public void method() {}
}
