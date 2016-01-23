package net.codebreeze.rest.server;

import com.google.common.base.Throwables;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import net.codebreeze.rest.server.config.AppConfig;
import net.codebreeze.rest.server.controllers.HelloRestService;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

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
                                                  .setDeploymentName( "test.war" )
                                                  .addListener( Servlets.listener( ContextLoaderListener.class ) )
                                                  .addInitParameter( "contextClass", AnnotationConfigWebApplicationContext.class.getCanonicalName() )
                                                  .addInitParameter( "contextConfigLocation", AppConfig.class.getCanonicalName() )
                                                  .addServlets(
                                                          Servlets.servlet( "REST_API_Servlet", org.glassfish.jersey.servlet.ServletContainer.class,
                                                                  new ServletInstanceFactory() )
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

    private static class ServletInstanceFactory implements InstanceFactory<Servlet>
    {

        @Override
        public InstanceHandle<Servlet> createInstance() throws InstantiationException
        {
            return new InstanceHandle<Servlet>()
            {
                @Override
                public Servlet getInstance()
                {
                    return new org.glassfish.jersey.servlet.ServletContainer( new ResourceConfig( HelloRestService.class ) );
                }
                @Override
                public void release()
                {
                }
            };
        }
    }
}
