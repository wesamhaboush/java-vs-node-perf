package net.codebreeze.rest.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

public class JettyDriver extends AbstractDriver
{
    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";

    public static void main( final String... args ) throws Exception
    {
        final ServiceConfiguration serviceConfiguration = parseParamsWithJCommander( args );
        startJetty( serviceConfiguration.port );
    }

    private static void startJetty( int port ) throws Exception
    {
        Server server = new Server( port );
        server.setHandler( getServletContextHandler( getContext() ) );
        server.start();
        server.join();
    }

    private static ServletContextHandler getServletContextHandler( WebApplicationContext context ) throws IOException
    {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler( null );
        contextHandler.setContextPath( CONTEXT_PATH );
        contextHandler.addServlet( new ServletHolder( new DispatcherServlet( context ) ), MAPPING_URL );
        contextHandler.addEventListener( new ContextLoaderListener( context ) );
//        contextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());
        return contextHandler;
    }
}
