/* (C) 2025 */

package com.example.utility;


import lombok.extern.log4j.Log4j2;


/**
 * A simple way to capture elapsed time.
 */
@Log4j2
public class Stopwatch implements AutoCloseable
{
    private static final long NOT_STARTED = -1L;

    private long startNanos = NOT_STARTED;
    private long stopNanos  = NOT_STARTED;

    private final String service;
    private final String method;
    private final String context;

    /**
     * Create a new timer and automatically starts timing.
     *
     * @param service typically it is the name of the REST/SOAP service.
     * @param method  the method/function that is being timed.
     * @param context a unique identifier for the execution context
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
     * @param context   a unique identifier for the execution context
     * @param autoStart true, it will start the timer on instantiation.
     */
    public Stopwatch( final String service, final String method, final String context, final boolean autoStart )
    {
        startNanos = NOT_STARTED;
        stopNanos  = NOT_STARTED;

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
        if ( startNanos == NOT_STARTED )
        {
            startNanos = System.nanoTime();
        }
    }


    /**
     * Stop the timer. Do nothing if the timer has previously run and is stopped.
     */
    public final void stop()
    {
        if ( stopNanos == NOT_STARTED )
        {
            stopNanos = System.nanoTime();
        }
    }


    @Override
    public void close()
    {
        stop();
        logElapsed( null );
    }


    /**
     * Return the running state.
     *
     * @return true if the timer is presently running.
     */
    public boolean isRunning()
    {
        return startNanos != NOT_STARTED && stopNanos == NOT_STARTED;
    }


    /** Log elapsed time with an optional message.
     *
     * @param text the text to write to the log.
     */
    public void logRunningTime( final String text )
    {
        logElapsed( text );
    }


    /**
     * Enter a log message with the elapsed time of the timer.
     *
     * @param text the text to write to the log.
     */
    private void logElapsed( final String text )
    {
        if ( startNanos == NOT_STARTED )
        {
            return; // Nothing to measure
        }

        final long endNanos = (stopNanos == NOT_STARTED) ? System.nanoTime() : stopNanos;
        final long elapsedMs = (endNanos - startNanos) / 1_000_000;

        log.error(() -> String.format( "%s, %s, %s: '%s' - elapsed: %d ms",
                                      context, service, method, text == null ? "" : text, elapsedMs ) );
    }
}
