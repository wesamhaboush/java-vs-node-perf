package net.codebreeze.rest.server.controllers;

import net.codebreeze.rest.server.model.Greeting;
import net.codebreeze.rest.server.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping(value = "/")
public class HelloRestService
{
    @Autowired
    private HelloService helloService;

    @RequestMapping(
        value = "hello/{name}",
        method = RequestMethod.GET
                 ,
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
    )
    public @ResponseBody
    Greeting hello( @PathVariable( "name" ) String name )
    {
        System.out.println( name );
        return helloService.create( name );
    }
}
