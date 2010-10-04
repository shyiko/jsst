package jsst.server.result;

import jsst.core.client.handler.Status;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public class SuccessTestResult extends TestResult {

    @Override
    public Status getStatus() {
        return Status.SUCCESS;
    }
}
