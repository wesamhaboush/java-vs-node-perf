package net.codebreeze.rest.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

public class JettyDriver extends AbstractDriver
{
    public static void main( final String... args ) throws Exception
    {
        final ServiceConfiguration serviceConfiguration = parseParamsWithJCommander( args );
        final Server server = new Server( serviceConfiguration.port );
        final ServletHolder servletHolder = new ServletHolder( new DispatcherServlet( getContext() ) );
        final ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath( "/" );
        servletContextHandler.addServlet( servletHolder, "/*" );
        servletContextHandler.addEventListener( new ContextLoaderListener() );
//        servletContextHandler.setInitParameter( "contextClass", AnnotationConfigWebApplicationContext.class.getName() );
//        servletContextHandler.setInitParameter( "contextConfigLocation", AppConfig.class.getCanonicalName() );
        server.setHandler( servletContextHandler );
        server.start();
        server.join();
    }
}
