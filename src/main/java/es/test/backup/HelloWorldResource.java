package es.test.backup;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import es.test.DataSourceLocator;

//@Path("/hello")
public class HelloWorldResource {

    @Inject
    public DataSourceLocator locator;

    @GET
    @Path("/test/{param}")
    public Response getTest(@PathParam("param") String msg) {
        String output = "Jersey say : " + msg;
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {
        String output = "Jersey say : " + msg;
        return Response.status(200).entity(output).build();
    }
}