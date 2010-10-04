package jsst.junit.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author shyiko
 * @since Oct 4, 2010
 */
public class TestCase {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @Test
    public void testMethod() throws Exception {
        fail("testMethod");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }
}
