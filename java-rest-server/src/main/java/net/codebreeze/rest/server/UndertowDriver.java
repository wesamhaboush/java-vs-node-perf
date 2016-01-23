package net.codebreeze.rest.server;

import com.google.common.base.Throwables;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;


public class UndertowDriver extends AbstractDriver
{

    private static final Logger LOG = LoggerFactory.getLogger( UndertowDriver.class );

    public static void main( final String... args )
    {
        try
        {
            final ServiceConfiguration serviceConfiguration = parseParamsWithJCommander( args );
            final DeploymentInfo servletBuilder = deployment()
                                                  .setClassLoader( UndertowDriver.class.getClassLoader() )
                                                  .setContextPath( "/" )
                                                  .setDeploymentName( "testing performance" )
                                                  .addServlets(
                                                          Servlets.servlet( "MyServlet", DispatcherServlet.class,
                                                                  new DispatcherServletInstanceFactory( getContext() ) )
                                                          .setLoadOnStartup( 0 )
                                                          .addMapping( "/*" )
                                                          .setAsyncSupported( true ) );
            final DeploymentManager manager = defaultContainer().addDeployment( servletBuilder );
            manager.deploy();
            final HttpHandler servletHandler = manager.start();
            final PathHandler path = Handlers.path( Handlers.redirect( "/" ) )
                                     .addPrefixPath( "/", servletHandler );
            final String hostAddress = "0.0.0.0";
            LOG.info( "Server {} started at port {}", hostAddress, serviceConfiguration.port );
            LOG.info( "serviceConfiguration.noOfIoThreads :" + serviceConfiguration.noOfIoThreads );
            LOG.info( "serviceConfiguration.noOfWorkerThreads :" + serviceConfiguration.noOfWorkerThreads );
            final Undertow server = Undertow.builder()
                                    .addHttpListener( serviceConfiguration.port, hostAddress )
                                    .setHandler( path )
                                    .setIoThreads( serviceConfiguration.noOfIoThreads ) // default 8
                                    .setWorkerThreads( serviceConfiguration.noOfWorkerThreads ) // default 64
                                    .build();
            server.start();
        }
        catch ( final ServletException e )
        {
            throw Throwables.propagate( e );
        }
    }

    private static class DispatcherServletInstanceFactory implements InstanceFactory<Servlet>
    {

        private final WebApplicationContext wac;

        public DispatcherServletInstanceFactory( WebApplicationContext wac )
        {
            this.wac = wac;
        }

        @Override
        public InstanceHandle<Servlet> createInstance() throws InstantiationException
        {
            return new InstanceHandle<Servlet>()
            {
                @Override
                public Servlet getInstance()
                {
                    return new DispatcherServlet( wac );
                }
                @Override
                public void release()
                {
                }
            };
        }
    }
}
