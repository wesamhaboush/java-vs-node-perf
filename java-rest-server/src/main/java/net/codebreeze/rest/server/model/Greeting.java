package net.codebreeze.rest.server.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect( fieldVisibility = JsonAutoDetect.Visibility.ANY )
public class Greeting
{
    private final String greeted;

    public Greeting( String greeted )
    {
        this.greeted = greeted;
    }
}
