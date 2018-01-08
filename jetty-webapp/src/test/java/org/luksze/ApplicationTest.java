package org.luksze;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ApplicationTest {

    Application application = new Application();

    @Before
    public void setUp() throws Exception {
        application.startWithoutBlocking();
    }

    @Test
    public void testName() throws Exception {
        Assert.assertTrue(true);

    }

    @After
    public void shutdown() throws Exception {
        application.stop();

    }
}