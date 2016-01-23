package net.codebreeze.rest.server;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.servlet.DispatcherServlet;

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
        Tomcat.addServlet( rootCtx, "springServlet", new DispatcherServlet( getContext() ) );
        rootCtx.addServletMapping( "/*", "springServlet" );
        tomcat.start();
        tomcat.getServer().await();
    }
}
