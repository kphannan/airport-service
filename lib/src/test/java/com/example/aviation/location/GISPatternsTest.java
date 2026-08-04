package com.example.aviation.location;


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



public class GISPatternsTest
{

    @Nested
    @DisplayName( "Is a proper utility" )
    class MeetsDesignCriteria
    {
        /** Ensure the {@code GISPatterns} satisfies the Utility class design. */
        @Test
        @DisplayName( "Conforms to utility class standard" )
        void gisCodePatterns_isProperUtilityClass()
        {
            final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

            assertAll( () -> assertThat( ValidateUtilityClass.isProperUtilityClass( GISPatterns.class, reason ) ).isTrue(),
                       () -> assertThat( reason.toString() ).isEqualTo( "[]" )
            );
        }
    }



    @Nested
    @DisplayName( "Decimal Degrees (DD) Pattern" )
    class DecimalDegreesPattern
    {

        @ParameterizedTest
        @ValueSource( strings = { "40.753", "-73.983", "0.0", "40.446°N", "-40.446°S", "90", "90.0", "180.0", "-180" } )
        @DisplayName( "Matches valid single DD coordinates" )
        void matchesValidSingleDD( final String coord )
        {
            final boolean isLat = GISPatterns.DD_LAT_PATTERN.matcher( coord ).matches();
            final boolean isLon = GISPatterns.DD_LONG_PATTERN.matcher( coord ).matches();
            assertThat( isLat || isLon )
                .as( "Expected '%s' to be valid as either latitude or longitude", coord )
                .isTrue();
        }

        @ParameterizedTest
        @ValueSource( strings = { "40.753, -73.983", "0, 0", "+90, +180", "-45.123456, 120.654321" } )
        @DisplayName( "Matches valid DD lat/long pairs" )
        void matchesValidLatLongPairs( final String coord )
        {
            assertThat( GISPatterns.isDDValidLatLong( coord ) )
                .as( "Expected '%s' to be a valid DD pair", coord )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "91", "-91", "181", "-181", "40.753", "abc",
                                  "", "91.753, 181", "181, 40", "+90.01, -180.01" } )
        @DisplayName( "Rejects invalid DD coordinates" )
        void rejectsInvalidDD( final String coord )
        {
            assertThat( GISPatterns.isDDValidLatLong( coord ) )
                .as( "Expected '%s' to be rejected", coord )
                .isFalse();
        }

        @ParameterizedTest
        @ValueSource( strings = { "40.753", "90", "-45.123", "0.000001", "90", "+90", "+0.0", "-0.0" } )
        @DisplayName( "Validates single latitude values" )
        void isValidLatitudeDD( final String lat )
        {
            assertThat( GISPatterns.isDDValidLatitude( lat ) )
                .as( "Expected '%s' to be valid latitude", lat )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "91", "-91", "181", "-181", "abc", "", "91.753, 181", "181, 40", "-90.01", "+90.002" } )
        @DisplayName( "Rejects invalid or null DD latitude" )
        void rejectsInvalidDDLatitude( final String latitude )
        {
            assertThat( GISPatterns.isDDValidLatitude( latitude ) )
                .as( "Expected '%s' to be rejected", latitude )
                .isFalse();
        }

        @ParameterizedTest
        @ValueSource( strings = { "73.983", "180.0", "-120.5", "0.0" } )
        @DisplayName( "Validates single longitude values" )
        void isValidLongitudeDD( final String longitude )
        {
            assertThat( GISPatterns.isDDValidLongitude( longitude ) )
                .as( "Expected '%s' to be valid longitude", longitude )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "190.983", "180.1", "-180.5", "200.0" } )
        @DisplayName( "Rejects invalid or null DD longitude" )
        void rejectsInvalidDDLongitude( final String longitude )
        {
            assertThat( GISPatterns.isDDValidLongitude( longitude ) )
                .as( "Expected '%s' to be rejected", longitude )
                .isFalse();
        }
    }


    @Nested
    @DisplayName( "Decimal Minutes (DDM) Pattern" )
    class DecimalMinutesPattern
    {

        @ParameterizedTest
        @ValueSource( strings = { "40°45.18'N", "0°0.0'N", "90°0.0'N" } )
        @DisplayName( "Matches valid DDM latitude" )
        void matchesValidDDMLat( final String latitude )
        {
            assertThat( GISPatterns.isDDMValidLat( latitude ) )
                .as( "Expected '%s' to be valid DDM latitude", latitude )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "91", "-91", "181", "-181", "abc", "",
                                  "91.753, 181", "181, 40", "-0.1", "-0°0.1'N" /*, "90°0.1'N" */
        } ) // TODO
        @DisplayName( "Rejects invalid or null DDM latitude" )
        void rejectsInvalidDDMLatitude( final String latitude )
        {
            assertThat( GISPatterns.isDDMValidLat( latitude ) )
                .as( "Expected '%s' to be rejected", latitude )
                .isFalse();
        }


        @ParameterizedTest
        @ValueSource( strings = { " 73°58.98'W", "0°0.0'E", "180°0.0'W" } )
        @DisplayName( "Matches valid DDM longitude" )
        void matchesValidDDMLong( final String longitude )
        {
            assertThat( GISPatterns.isDDMValidLong( longitude ) )
                .as( "Expected '%s' to be valid DDM longitude", longitude )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "-181", "abc", "", "181", "-0.1", "-0°0.2'N", "180°0.3'N" } )
        @DisplayName( "Rejects invalid or null DDM longitude" )
        void rejectsInvalidDDMLongitude( final String longitude )
        {
            assertThat( GISPatterns.isDDMValidLong( longitude ) )
                .as( "Expected '%s' to be rejected", longitude )
                .isFalse();
        }



        @ParameterizedTest
        @ValueSource( strings = { "40°45.18'N, 73°58.98'W", "0°0.0'N, 0°0.0'E", "90°0.0'N, 180°0.0'W" } )
        @DisplayName( "Matches valid DDM coordinates" )
        void matchesValidDDM( final String coord )
        {
            assertThat( GISPatterns.isDDMValidLatLong( coord ) )
                .as( "Expected '%s' to be valid DM", coord )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "40.753, -73.983", "40°45'11\"N, 73°58'59\"W", "91°0.0'N, 0°0.0'E" } )
        @DisplayName( "Rejects invalid DDM coordinates" )
        void rejectsInvalidDDM( final String coord )
        {
            assertThat( GISPatterns.isDDMValidLatLong( coord ) )
                .as( "Expected '%s' to be rejected", coord )
                .isFalse();
        }
    }


    @Nested
    @DisplayName( "Degrees Minutes Seconds (DMS) Pattern" )
    class DMSPattern
    {

        @ParameterizedTest
        @ValueSource( strings = { "40°45'11\"N", "0°0'0\"N", "90°0'0\"N" } )
        @DisplayName( "Matches valid DMS Latitude" )
        void matchesValidDMSLat( final String latitude )
        {
            assertThat( GISPatterns.isDMSValidLat( latitude ) )
                .as( "Expected '%s' to be valid DMS latitude", latitude )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "91", "-91", "181", "-181", "abc", "", "91.753", "90°00'11\"N", "-0°00'13\"N" } )
        @DisplayName( "Rejects invalid or null DMS latitude" )
        void rejectsInvalidDMSLatitude( final String latitude )
        {
            assertThat( GISPatterns.isDMSValidLat( latitude ) )
                .as( "Expected '%s' to be rejected", latitude )
                .isFalse();
        }



        @ParameterizedTest
        @ValueSource( strings = { "79° 58′ 56″ W", "79° 58′ 54\" W", "73°58'59\"W", "0°0'0\"E", "180°0'0\"W" } )
        @DisplayName( "Matches valid DMS longitude" )
        void matchesValidDMSLong( final String longitude )
        {
            assertThat( GISPatterns.isDMSValidLong( longitude ) )
                .as( "Expected '%s' to be valid DMS Longitude", longitude )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "91", "-91", "181", "-181", "abc", "", "91.753", "180°00'11\"N", "-0°00'11\"N" } )
        @DisplayName( "Rejects invalid or null DMS longitude" )
        void rejectsInvalidDMSLongitude( final String longitude )
        {
            assertThat( GISPatterns.isDMSValidLong( longitude ) )
                .as( "Expected '%s' to be rejected", longitude )
                .isFalse();
        }


        @ParameterizedTest
        @ValueSource( strings = { "40°45'11\"N, 73°58'59\"W", "0°0'0\"N, 0°0'0\"E", "90°0'0\"N, 180°0'0\"W" } )
        @DisplayName( "Matches valid DMS coordinates" )
        void matchesValidDMS( final String coord )
        {
            assertThat( GISPatterns.isDMSValidLatLong( coord ) )
                .as( "Expected '%s' to be valid DMS", coord )
                .isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "40.753, -73.983", "40°45.18'N, 73°58.98'W", "91°0'0\"N, 0°0'0\"E" } )
        @DisplayName( "Rejects invalid DMS coordinates" )
        void rejectsInvalidDMS( final String coord )
        {
            assertThat( GISPatterns.isDMSValidLatLong( coord ) )
                .as( "Expected '%s' to be rejected", coord )
                .isFalse();
        }
    }


    @Nested
    @DisplayName( "Null and Edge Cases" )
    class EdgeCases
    {

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = { "   ", "40.753,", ", -73.983", "40.753, -73.983, 1" } )
        @DisplayName( "Handles malformed input gracefully" )
        void rejectsMalformedInput( final String coord )
        {
            assertThat( GISPatterns.isDDValidLatLong( coord ) )
                .as( "Expected '%s' to be rejected", coord )
                .isFalse();
        }
    }

}
