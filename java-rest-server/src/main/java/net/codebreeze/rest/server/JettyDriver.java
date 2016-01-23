package net.codebreeze.rest.server;

import net.codebreeze.rest.server.config.AppConfig;
import net.codebreeze.rest.server.controllers.HelloRestService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class JettyDriver extends AbstractDriver
{
    public static void main( final String... args ) throws Exception
    {
        final ServiceConfiguration serviceConfiguration = parseParamsWithJCommander( args );
        final Server server = new Server( serviceConfiguration.port );

        final ServletHolder servletHolder = new ServletHolder( new ServletContainer() );
        servletHolder.setInitParameter( "javax.ws.rs.Application", HelloRestService.class.getCanonicalName() );
        final ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath( "/" );
        servletContextHandler.addServlet( servletHolder, "/*" );
        servletContextHandler.addEventListener( new ContextLoaderListener() );
        servletContextHandler.setInitParameter( "contextClass", AnnotationConfigWebApplicationContext.class.getName() );
        servletContextHandler.setInitParameter( "contextConfigLocation", AppConfig.class.getCanonicalName() );
        server.setHandler( servletContextHandler );
        server.start();
        server.join();
    }
}
