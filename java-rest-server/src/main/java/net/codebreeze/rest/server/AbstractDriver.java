package net.codebreeze.rest.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import net.codebreeze.rest.server.config.AppConfig;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public abstract class AbstractDriver
{

    protected static WebApplicationContext getContext( String... args )
    {
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation( AppConfig.class.getName() );
        final CommandLinePropertySource clps = new SimpleCommandLinePropertySource( args );
        context.getEnvironment().getPropertySources().addFirst( clps );
        context.registerShutdownHook();
        return context;
    }

    protected static ServiceConfiguration parseParamsWithJCommander( final String... args )
    {
        final ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
        new JCommander( serviceConfiguration, args );
        return serviceConfiguration;
    }

    @Parameters( separators = "= " )
    protected static class ServiceConfiguration
    {
        @Parameter(
            names = {"--http-port"},
            arity = 1,
            description = "the port number on which the rest service will be listening"
        )
        protected Integer port = 8080;

        @Parameter(
            names = {"--io-thread-count"},
            arity = 1,
            description = "number of io threads"
        )
        //undertow recommends 1 per CPU core
        protected Integer noOfIoThreads = 8;

        @Parameter(
            names = {"--worker-thread-count"},
            arity = 1,
            description = "number of worker threads"
        )
        //undertow recommends 10 per CPU core
        protected Integer noOfWorkerThreads = 80;
    }
}
