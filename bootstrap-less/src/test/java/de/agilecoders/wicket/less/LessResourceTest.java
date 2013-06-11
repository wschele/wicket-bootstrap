package de.agilecoders.wicket.less;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class LessResourceTest extends Assert {

    WicketTester tester;

    protected WebApplication createApplication() {
        return new TestApplication() {
            @Override
            public void init() {
                super.init();

                getResourceSettings().setCachingStrategy(NoOpResourceCachingStrategy.INSTANCE);
            }
        };
    }

    @Before
    public void before() {
        tester = new WicketTester(new TestApplication());
    }

    @After
    public void after() {
        tester.destroy();
        tester = null;
    }

    /**
     * Tests the compilation of Less resources to Css.
     * Makes a request to Less resource and asserts that an expected Css
     * content is being returned.
     * The Less resource imports another one via "@import 'some.less'"
     */
    @Test
    public void request() throws IOException {

        tester.startResourceReference(new LessResourceReference(HomePage.class, "resources/root.less"));
        String cssContent = tester.getLastResponseAsString();

        InputStream expectedInputStream = LessResourceTest.class.getResourceAsStream("resources/expected.css");
        String expected = IOUtils.toString(expectedInputStream);
        assertEquals(expected, cssContent);
    }

    /**
     * Issue #189
     */
    @Test
    public void resourceReferenceDependency() {
        tester.startPage(HomePage.class);

        System.err.println("doc: \n" + tester.getLastResponseAsString());

        tester.executeUrl("./wicket/resource/de.agilecoders.wicket.less.HomePage/resources/root.less");

        System.err.println("\n\nres: \n" + tester.getLastResponseAsString());
    }
}
