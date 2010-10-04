package jsst.server.result;

import jsst.core.client.handler.Status;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public class FailTestResult extends TestResult {

    private Throwable t;

    public FailTestResult(Throwable t) {
        this.t = t;
    }

    public Throwable getThrowable() {
        return t;
    }

    @Override
    public Status getStatus() {
        return Status.FAIL;
    }
}
