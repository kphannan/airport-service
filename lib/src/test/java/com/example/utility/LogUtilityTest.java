package com.example.utility;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kevin
 * @since 2026-08-07
 *     <p>
 *     Copyright (c) 2020-2026
 */
public class LogUtilityTest
{
    @SuppressWarnings( "initialization.field.uninitialized" )
    private LogCaptor logCaptor; // will be set in @BeforeAll

    @BeforeEach
    void init()
    {
        logCaptor = LogCaptor.forClass( LogUtility.class );
    }



    @AfterEach
    void clearFixture()
    {
        logCaptor.clearLogs();
    }


    @Nested
    @DisplayName( "Is a proper utility" )
    class MeetsDesignCriteria
    {
        /** Ensure the {@code LogUtility} satisfies the Utility class design. */
        @Test
        @DisplayName( "Conforms to utility class standard" )
        void stringUtility_isProperUtilityClass()
        {
            final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

            assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( LogUtility.class, reason ) ),
                       () -> assertEquals( "[]", reason.toString() ) );
        }
    }



    @Test
    void printList_nullList_noOutput()
    {
        LogUtility.printList( "Test null collection", null );
        assertThat( logCaptor.getLogs() )
            .hasSize( 1 )
            .contains( "Display collection Test null collection" );
    }

    @Test
    void printList_emptyList_noOutput()
    {
        List<String> cut = Collections.emptyList();

        LogUtility.printList( "Test empty collection", cut );
        assertThat( logCaptor.getLogs() )
            .hasSize( 1 )
            .contains( "Display collection Test empty collection" );
    }

    @Test
    void printList_oneEntryList_hasOutput()
    {
        List<String> cut = List.of( "One entry" );

        LogUtility.printList( "Test empty collection", cut );
        assertThat( logCaptor.getLogs() )
            .hasSize( 2 )
            .contains( "Display collection Test empty collection" )
            .contains( "One entry" );
    }

}
