package jsst.server.config;

/**
 * @author shyiko
 * @since Oct 3, 2010
 */
public class TestConfiguration {

    private String className;
    private String methodName;

    public TestConfiguration(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}
