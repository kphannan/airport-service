package com.example.airline.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.StringJoiner;

import com.example.utility.ValidateUtilityClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
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
        /** Ensure the {@code LocationCodePatterns} satisfies the Utility class design. */
        @Test
        @DisplayName( "Conforms to utility class standard" )
        void locationCodePatterns_isProperUtilityClass()
        {
            final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

            assertAll(
                () -> assertThat( ValidateUtilityClass.isProperUtilityClass( LocationCodePatterns.class, reason ) ).isTrue(),
                () -> assertThat( reason.toString() ).isEqualTo( "[]" )
                     );
        }
    }

    @Nested
    @DisplayName( "isValidContinentCode" )
    class IsValidContinentCode
    {
        @ParameterizedTest
        @ValueSource( strings = { "AF", "AN", "AS", "EU", "NA", "OC", "SA" } )
        @DisplayName( "Accepts valid 2-letter continent codes" )
        void acceptsValidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidContinentCode( code ) ).isTrue();
        }

        @ParameterizedTest
        @ValueSource( strings = { "af", "AFR", "EUROPE", "xx", " " } )
        @NullSource
        @DisplayName( "Rejects invalid, lowercase, or null codes" )
        void rejectsInvalidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidContinentCode( code ) ).isFalse();
        }
    }

    @Nested
    @DisplayName( "isValidCountryCode" )
    class IsValidCountryCode
    {
        @ParameterizedTest
        @ValueSource( strings = { "US", "DE", "JP", "CA" } )
        @DisplayName( "Accepts valid ISO 3166-1 alpha-2 codes" )
        void acceptsValidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidCountryCode( code ) ).isTrue();
        }

        @ParameterizedTest
        @ValueSource( strings = { "us", "USA", "1A", "A", " " } )
        @NullSource
        @DisplayName( "Rejects lowercase, wrong length, or non-alpha codes" )
        void rejectsInvalidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidCountryCode( code ) ).isFalse();
        }
    }

    @Nested
    @DisplayName( "isValidRegionCode" )
    class IsValidRegionCode
    {
        @ParameterizedTest
        @ValueSource( strings = { "US-CA", "DE-BY", "CN-11", "US-U-A" } )
        @DisplayName( "Accepts valid ISO 3166-2 region codes" )
        void acceptsValidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidRegionCode( code ) ).isTrue();
        }

        @ParameterizedTest
        @ValueSource( strings = { "us-ca", "US-", "US-TOOLONG", "US-1234", "US-U-A-EXTRA", " " } )
        @NullSource
        @DisplayName( "Rejects invalid format, wrong case, or out-of-bound codes" )
        void rejectsInvalidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidRegionCode( code ) ).isFalse();
        }
    }

    @Nested
    @DisplayName( "isValidRegionSubCode" )
    class IsValidRegionSubCode
    {
        @ParameterizedTest
        @ValueSource( strings = { "CA", "BY", "11", "U-A" } )
        @DisplayName( "Accepts valid region subdivision codes" )
        void acceptsValidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidRegionSubCode( code ) ).isTrue();
        }

        @ParameterizedTest
        @ValueSource( strings = { "ca", "TOOLONG", "1234", "u-a", " " } )
        @NullSource
        @DisplayName( "Rejects lowercase, out-of-length, or null codes" )
        void rejectsInvalidCodes( String code )
        {
            assertThat( LocationCodePatterns.isValidRegionSubCode( code ) ).isFalse();
        }
    }
}
