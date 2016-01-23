package net.codebreeze.rest.server.controllers;

import net.codebreeze.rest.server.model.Greeting;
import net.codebreeze.rest.server.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestService
{
    @Autowired
    private HelloService helloService;

    @RequestMapping(
        value = "/hello/{name}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public Greeting hello( @PathVariable( "name" ) String name )
    {
        return helloService.create( name );
    }
}
