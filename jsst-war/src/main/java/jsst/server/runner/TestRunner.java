package jsst.server.runner;

import jsst.server.config.TestConfiguration;
import jsst.server.result.TestResult;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public interface TestRunner {

    TestResult run(TestConfiguration configuration); 
}
