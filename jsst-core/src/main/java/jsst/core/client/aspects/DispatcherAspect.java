package jsst.core.client.aspects;

import jsst.core.client.dispatcher.Dispatcher;
import jsst.core.client.handler.Handler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
@Aspect
public abstract class DispatcherAspect {

    @Pointcut
    public abstract void method();

    public abstract Dispatcher getDispatcher();
    public abstract Handler getHandler();

    @SuppressWarnings({"unchecked"})
    @Around(value = "method()")
    public void dispatch(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object response = getDispatcher().dispatch(methodSignature.getMethod());
        getHandler().handle(response);
    }
}