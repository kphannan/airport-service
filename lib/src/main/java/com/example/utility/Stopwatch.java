/* (C)2025 */

package com.example.utility;


import lombok.extern.log4j.Log4j2;



/**
 * A simple way to capture elapsed time.
 */
@Log4j2
public class Stopwatch implements AutoCloseable
{
    private long startTime;
    private long stopTime;

    private final String service;
    private final String method;
    private final String context;

    /**
     * Create a new timer and automatically starts timing.
     *
     * @param service typically it is the name of the REST/SOAP service.
     * @param method  the method/function that is being timed.
     * @param context a unique
     */
    public Stopwatch( final String service, final String method, final String context )
    {
        this( service, method, context, true );
    }



    /**
     * Create a new timer and optionally start it.
     *
     * @param service   typically it is the name of the REST/SOAP service.
     * @param method    the method/function that is being timed.
     * @param context   a unique
     * @param autoStart true it will start the timer on instantiation.
     */
    public Stopwatch( final String service, final String method, final String context, final boolean autoStart )
    {
        startTime = -1;
        stopTime  = -1;

        this.service = service;
        this.method  = method;
        this.context = context;

        if ( autoStart )
        {
            start();
        }
    }



    /**
     * Start the timer. Do nothing if the timer is already running.
     */
    public final void start()
    {
        if ( startTime == -1 )
        {
            startTime = System.currentTimeMillis();
        }
    }



    /**
     * Stop the timer. Do nothing if the timer has previously run and is stopped.
     */
    public final void stop()
    {
        if ( stopTime == -1 )
        {
            stopTime = System.currentTimeMillis();
        }
    }



    @Override
    public void close()
    {
        stop();
        debug();
    }



    /**
     * Return the running state.
     *
     * @return true if the timer is presently running.
     */
    public boolean isRunning()
    {
        return startTime != -1 && stopTime == -1;
    }



    /**
     * Enter a log message with the elapsed time of the timer.
     *
     * @param text the text to write to the log.
     */
    public void logRunningTime( final String text )
    {
        logX( System.currentTimeMillis(), text );
    }



    private void debug()
    {
        logX( stopTime, null );
    }



    private void logX( final long endTime,
                       final String text )
    {
        // log.error( "Kilroy was here" );
        // System.out.println( String.format( "logg: %s, %s, %s: '%s' - elapsed time: %d ms", context, service, method,
        // text == null ? "" : text, endTime - startTime ) );
        log.debug( () -> String.format( "%s, %s, %s: '%s' - elapsed time: %d ms", context, service, method,
                                        text == null ? "" : text, endTime - startTime ) );
        log.error( () -> String.format( "%s, %s, %s: '%s' - elapsed time: %d ms", context, service, method,
        text == null ? "" : text, endTime - startTime ) );
    }

}
