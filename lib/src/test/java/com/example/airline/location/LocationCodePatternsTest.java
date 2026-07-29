package com.example.airline.location;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.StringJoiner;

import com.example.aviation.reference.AviationCodePatterns;
import com.example.utility.ValidateUtilityClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kevin
 * @since 2026-07-28
 *     <p>
 *     Copyright (c) 2020-2026
 */
class LocationCodePatternsTest
{

    @Nested
    @DisplayName( "Is a proper utility" )
    class MeetsDesignCriteria
    {
        /** Ensure the {@code StringUtiility} satisfies the Utility class design. */
        @Test
        @DisplayName( "Conforms to utility class standard" )
        void locationCodePatterns_isProperUtilityClass()
        {
            final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

            assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( LocationCodePatterns.class, reason ) ),
                       () -> assertEquals( "[]", reason.toString() ) );
        }
    }


}
