package org.wildfly.plugins.demo.jaxrs;


import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;


@Path("/hello")
public class HelloWorldEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet() {
        Person p =new Person();
        p.setName("prai");
        return Response.ok(p,MediaType.APPLICATION_JSON).build();
    }
}
