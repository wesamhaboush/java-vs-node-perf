package net.codebreeze.rest.server.services;

import net.codebreeze.rest.server.model.Greeting;
import org.springframework.stereotype.Service;


@Service
public class HelloService
{
    public Greeting create( final String name )
    {
        return new Greeting( name );
    }
}
