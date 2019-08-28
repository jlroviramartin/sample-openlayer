package webapp;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.MediaTypes;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import javax.ws.rs.core.Application;

import org.junit.Assert;
import org.junit.Test;

public class EmployeeResourceTest extends JerseyTest {

    public EmployeeResourceTest() throws Exception {
        super(new WebAppDescriptor.Builder("es.test")
                .contextPath("webapp")
                .build());
    }

    @Override
    protected AppDescriptor configure() {
        return new ResourceConfig(ItemRestService.class);
    }

    @Test
    public void testGetEmployee() throws Exception {
        WebResource webResource = resource();
        String responseMsg = webResource
        		.path("resources")
        		.path("employees")
        		.path("getEmployee")
        		.path("10")
        		.get(String.class);
        Assert.assertEquals("Hello World", responseMsg);
    }

    @Test
    public void testApplicationWadl() {
        WebResource webResource = resource();
        String serviceWadl = webResource.path("application.wadl").
                accept(MediaTypes.WADL).get(String.class);

        Assert.assertTrue(serviceWadl.length() > 0);
    }
}
