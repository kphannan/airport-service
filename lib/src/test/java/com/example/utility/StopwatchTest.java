/* (C) 2025 */

package com.example.utility;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;



/**
 * Unit tests for the {@code Stopwatch} timer.
 */
// intentional practice to use literals in tests for clarity instead of chasing
// down constant symbols
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class StopwatchTest
{
    @SuppressWarnings( "initialization.field.uninitialized" )
    private LogCaptor logCaptor; // will be set in @BeforeAll

    @BeforeEach
    void init()
    {
        logCaptor = LogCaptor.forClass( Stopwatch.class );
    }



    @AfterEach
    void clearFixture()
    {
        logCaptor.clearLogs();
    }





    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {

        @Test
        @SuppressWarnings( "PMD.CloseResource")
        void constructor_AutoStartFalse_DoesNotCrash()
        {
            final Stopwatch timer = new Stopwatch( "test", "ctor", "autostartFalse", false );

            assertAll( () -> assertThat( timer.isRunning() ).isFalse(),
                       () -> assertThat( logCaptor.getLogs() ).isEmpty() );
        }


        @Test
        @SuppressWarnings( "PMD.CloseResource")
        void constructor_AutoStartTrue_DoesNotCrash()
        {
            final Stopwatch timer = new Stopwatch( "test", "mthd", "autostartTrue" );

            assertAll( () -> assertThat( timer.isRunning() ).isTrue(),
                       () -> assertThat( logCaptor.getLogs() ).isEmpty() );
        }


        @Test
        @SuppressWarnings( "PMD.CloseResource")
        void runningTime_Start_ActuallyStartsTime()
        {
            final Stopwatch timer = new Stopwatch( "time", "autostart", "autostartFalse", false );

            assertThat( timer.isRunning() ).isFalse();
            timer.start();
            assertAll( () -> assertThat( timer.isRunning() ).isTrue(),
                       () -> assertThat( logCaptor.getLogs() ).isEmpty() );
        }
    }


    @Test
    void logRunningTime_notStarted_logsNothing()
    {
        try ( Stopwatch timer = new Stopwatch( "svc", "method", "logNothing", false ) )
        {
            timer.logRunningTime( "Does not log" );
            // assertThat( timer.isRunning() ).isTrue();
            assertAll( () -> assertThat( timer.isRunning() ).isFalse(),
                       () -> assertThat( logCaptor.getLogs() )
                                 .hasSize( 0 )
            );
        }
    }




    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_StartTwice_LeavesTimeRunning()
    {
        final Stopwatch timer = new Stopwatch( "time", "multipleStart", "start", false );

        assertThat( timer.isRunning() ).isFalse();

        timer.start();
        timer.start();

        assertThat( timer.isRunning() ).isTrue();
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_Stop_HaltsRunningTime()
    {
        final Stopwatch timer = new Stopwatch( "svc", "stopOnce", "stop" );

        assertThat( timer.isRunning() ).isTrue();

        // when
        timer.stop();

        // then
        assertThat( timer.isRunning() ).isFalse();

        timer.close(); // supress warning
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_multipleStop_HaltsRunningTime()
    {
        // gigven
        final Stopwatch timer = new Stopwatch( "svc", "multipeStop", "stop" );

        assertThat( timer.isRunning() ).isTrue();

        // when
        timer.stop();
        timer.stop();
        timer.stop();

        // then
        assertThat( timer.isRunning() ).isFalse();

        // timer.close(); // supress warning
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void runningTime_Close_HaltsRunningTime()
    {
        final Stopwatch timer = new Stopwatch( "svc", "close", "ctx" );

        assertThat( timer.isRunning() )
            .as( "Timer should immediately start running" )
            .isTrue();

        timer.close();

        assertAll( () -> assertThat( timer.isRunning() ).as( "close() should stop the timer" ).isFalse(),
                   () -> assertThat( logCaptor.getLogs() )
                             .isNotEmpty(),
                   () -> assertThat( logCaptor.getLogs() )
                             .contains( "ctx, svc, close: '' - elapsed: 0 ms" )
                   // () -> assertThat( logCaptor.getLogs() )
                   //           .anyMatch( s -> s."ctx, svc, close: '' - elapsed: [0-9]+ ms" )
        );
    }



    @Test
    void stopwatch_autoClose_HaltsRunningTime()
    {
        try ( Stopwatch timer = new Stopwatch( "svc", "method", "autoclose" ) )
        {
            assertThat( timer.isRunning() ).isTrue();
        }

        Pattern pattern = Pattern.compile( "autoclose.*svc.*method.*elapsed:\\s\\d+\\sms" );
        var zzz = logCaptor.getLogs();
        assertThat( zzz ) //logCaptor.getLogs() )
            .anyMatch( s -> pattern.matcher( s ).matches() );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void stopwatch_runningTimeNotStarted_logs()
    {
        final Stopwatch timer = new Stopwatch( "svc", "logRunning", "notRunning" );

        timer.logRunningTime( "Test, timer not started" );

        assertThat( logCaptor.getLogs() )
            .contains( "notRunning, svc, logRunning: 'Test, timer not started' - elapsed: 0 ms" );
    }



    @Test
    @SuppressWarnings( "PMD.CloseResource" )
    void stopwatch_runningTimeStarted_logs()
    {
        final Stopwatch timer = new Stopwatch( "svc", "logRunning", "running" );

        timer.start();
        timer.logRunningTime( "Test, timer started" );

        assertThat( logCaptor.getLogs() )
            .contains( "running, svc, logRunning: 'Test, timer started' - elapsed: 0 ms" );
    }

}
