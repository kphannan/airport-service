/* (C) 2025 */

package com.example.utility;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



/**
 * Unit tests fro the {@code Stopwatch} timer.
 */
// intentional practice to use literals in tests for clarity instead of chasing
// down constant symbols
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class StopwatchTest
{
    @SuppressWarnings( "initialization.field.uninitialized" )
    private static LogCaptor logCaptor; // will be set in @BeforeAll

    @BeforeAll
    static void setupOnce()
    {
        logCaptor = LogCaptor.forClass( Stopwatch.class );
    }



    @AfterEach
    void clearFixture()
    {
        logCaptor.clearLogs();
    }



    @AfterAll
    public static void tearDown()
    {
        logCaptor.close();
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void constructor_AutoStartFalse_DoesNotCrash()
    {
        final Stopwatch timer = new Stopwatch( "test", "ctor", "autostartFalse", false );

        assertAll( () -> assertFalse( timer.isRunning() ), //
                   () -> assertTrue( logCaptor.getLogs().isEmpty() ) );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void constructor_AutoStartTrue_DoesNotCrash()
    {
        final Stopwatch timer = new Stopwatch( "test", "mthd", "autostartTrue" );

        assertAll( () -> assertTrue( timer.isRunning() ), //
                   () -> assertTrue( logCaptor.getLogs().isEmpty() ) );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_Start_ActuallyStartsTime()
    {
        final Stopwatch timer = new Stopwatch( "time", "autostart", "autostartFalse", false );

        assertFalse( timer.isRunning() );
        timer.start();
        assertAll( () -> assertTrue( timer.isRunning() ), //
                   () -> assertTrue( logCaptor.getLogs().isEmpty() ) );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_StartTwice_LeavesTimeRunning()
    {
        final Stopwatch timer = new Stopwatch( "time", "multipleStart", "start", false );

        assertFalse( timer.isRunning() );
        timer.start();
        timer.start();
        assertTrue( timer.isRunning() );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_Stop_HaltsRunningTime()
    {
        final Stopwatch timer = new Stopwatch( "svc", "stopOnce", "stop" );

        assertTrue( timer.isRunning() );

        // when
        timer.stop();

        // then
        assertFalse( timer.isRunning() );

        timer.close(); // supress warning
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_multipleStop_HaltsRunningTime()
    {
        // gigven
        final Stopwatch timer = new Stopwatch( "svc", "multipeStop", "stop" );

        assertTrue( timer.isRunning() );

        // when
        timer.stop();
        timer.stop();
        timer.stop();

        // then
        assertFalse( timer.isRunning() );

        // timer.close(); // supress warning
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_Close_HaltsRunningTime()
    {
        final Stopwatch timer = new Stopwatch( "svc", "close", "ctx" );

        assertTrue( timer.isRunning() );
        timer.close();

        assertAll( () -> assertFalse( timer.isRunning() ), //
                   () -> assertFalse( logCaptor.getLogs().isEmpty() ), //
                   () -> assertTrue( StringUtility.contains( logCaptor.getLogs(),
                                                             "ctx, svc, close: '' - elapsed time: 0 ms" ) ) );
    }



    @Test
    void stopwatch_autoClose_HaltsRunningTime()
    {
        try ( Stopwatch timer = new Stopwatch( "svc", "method", "autoclose" ) )
        {
            assertTrue( timer.isRunning() );
        }

        assertTrue( StringUtility.contains( logCaptor.getLogs(), "autoclose, svc, method: '' - elapsed time: 0 ms" ) );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void stopwatch_runningTimeNotStarted_logs()
    {
        final Stopwatch timer = new Stopwatch( "svc", "logRunning", "notRunning" );

        timer.logRunningTime( "Test, timer not started" );

        assertTrue( StringUtility
                .contains( logCaptor.getLogs(),
                           "notRunning, svc, logRunning: 'Test, timer not started' - elapsed time: 0 ms" ) );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void stopwatch_runningTimeStarted_logs()
    {
        final Stopwatch timer = new Stopwatch( "svc", "logRunning", "running" );

        timer.start();
        timer.logRunningTime( "Test, timer started" );

        System.out.println( logCaptor.getLogs() );
        assertTrue( StringUtility.contains( logCaptor.getLogs(),
                                            "running, svc, logRunning: 'Test, timer started' - elapsed time: 0 ms" ) );
    }

}
