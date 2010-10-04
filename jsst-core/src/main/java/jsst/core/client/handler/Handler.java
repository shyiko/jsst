package jsst.core.client.handler;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public interface Handler<T> {

    void handle(T response) throws HandlerException;
}
