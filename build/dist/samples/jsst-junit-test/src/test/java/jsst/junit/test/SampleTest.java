package jsst.junit.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author shyiko
 * @since Oct 4, 2010
 */
public class SampleTest {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @Test
    public void testMethod() throws Exception {
        assertEquals("value", System.getProperty("SYSTEM.PROPERTY"));
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }
}
