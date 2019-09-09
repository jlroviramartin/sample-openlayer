package es.test.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import es.test.DataSourceLocator;

@Path("/testresource")
public class TestResource {

    // @Inject
    private DataSourceLocator dataSourceLocator;

    @Inject
    public TestResource(DataSourceLocator dataSourceLocator) {
        this.dataSourceLocator = dataSourceLocator;
    }

    @GET
    public String getHello() {
        return "Hello " + dataSourceLocator + "!";
    }
}
