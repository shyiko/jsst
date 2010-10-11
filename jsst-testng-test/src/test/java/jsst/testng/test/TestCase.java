package jsst.testng.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author shyiko
 * @since Oct 4, 2010
 */
public class TestCase {

    @BeforeClass
    public void beforeClass() {
        System.out.println("beforeClass");
    }

    @Test
    public void testMethod() throws Exception {
        assertEquals("value", System.getProperty("SYSTEM.PROPERTY"));
    }

    @AfterClass
    public void afterClass() {
        System.out.println("afterClass");
    }

}
