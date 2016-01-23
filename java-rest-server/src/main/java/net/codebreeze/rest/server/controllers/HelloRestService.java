package net.codebreeze.rest.server.controllers;

import net.codebreeze.rest.server.model.Greeting;
import net.codebreeze.rest.server.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path( "hello" )
public class HelloRestService
{
    @Autowired
    private HelloService helloService;

    @GET
    @Path( "/{name}" )
    @Produces( {MediaType.APPLICATION_JSON} )
    public Greeting hello( @PathParam( "name" ) String name )
    {
        return helloService.create( name );
    }
}
