package jsst.core.client.dispatcher;

import java.lang.reflect.Method;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public interface Dispatcher<T> {

    T dispatch(Method method) throws DispatcherException;
}
