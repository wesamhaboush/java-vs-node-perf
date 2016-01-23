package net.codebreeze.rest.server;

import net.codebreeze.rest.server.config.AppConfig;
import net.codebreeze.rest.server.controllers.HelloRestService;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.File;

public class TomcatDriver extends AbstractDriver
{
    public static void main( final String... args ) throws Exception
    {
        final ServiceConfiguration serviceConfiguration = parseParamsWithJCommander( args );
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort( serviceConfiguration.port );
        final File base = new File( System.getProperty( "java.io.tmpdir" ) );
        final Context rootCtx = tomcat.addContext( "", base.getAbsolutePath() );
        rootCtx.addApplicationListener( ContextLoaderListener.class.getCanonicalName() );
        rootCtx.addParameter( "contextClass", AnnotationConfigWebApplicationContext.class.getName() );
        rootCtx.addParameter( "contextConfigLocation", AppConfig.class.getCanonicalName() );
        Wrapper servlet = Tomcat.addServlet( rootCtx, "REST_API_Servlet", new ServletContainer() );
        servlet.addInitParameter( "javax.ws.rs.Application", HelloRestService.class.getCanonicalName() );
        rootCtx.addServletMapping( "/*", "REST_API_Servlet" );
        tomcat.start();
        tomcat.getServer().await();
    }
}
