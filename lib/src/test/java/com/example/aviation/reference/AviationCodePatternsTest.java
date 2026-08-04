package com.example.aviation.reference;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.StringJoiner;

import com.example.utility.ValidateUtilityClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName( "AviationCodePatterns Tests" )
class AviationCodePatternsTest
{
    @Nested
    @DisplayName( "Is a proper utility" )
    class MeetsDesignCriteria
    {
        /** Ensure the {@code StringUtiility} satisfies the Utility class design. */
        @Test
        @DisplayName( "Conforms to utility class standard" )
        void aviationCodePatterns_isProperUtilityClass()
        {
            final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

            assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( AviationCodePatterns.class, reason ) ),
                       () -> assertEquals( "[]", reason.toString() ) );
        }
    }



    @Nested
    @DisplayName( "Airport Codes" )
    class AirportCodes
    {
        @Nested
        @DisplayName( "IATA Airport Code Validation" )
        class IataAirportCodeTests
        {
            @ParameterizedTest
            @ValueSource( strings = { "JFK", "LHR", "CDG", "NRT", "SIN", "PEK", "DXB", "HND" } )
            @DisplayName( "Should return true for valid IATA codes" )
            void shouldAcceptValidIataCodes( String code )
            {
                assertTrue( AviationCodePatterns.isValidIataAirportCode( code ),
                            () -> "Expected '" + code + "' to be a valid IATA airport code" );
            }

            @ParameterizedTest
            @NullSource
            @ValueSource( strings = { "", "jfk", "JfK", "JK", "JFKK", "123", "JF1", "JK1", "AB1" } )
            @DisplayName( "Should return false for invalid IATA codes" )
            void shouldRejectInvalidIataCodes( String code )
            {
                assertFalse( AviationCodePatterns.isValidIataAirportCode( code ),
                             () -> "Expected '" + code + "' to be an invalid IATA airport code" );
            }
        }


        @Nested
        @DisplayName( "ICAO Airport Code Validation" )
        class IcaoAirportCodeTests
        {
            @ParameterizedTest
            @ValueSource( strings = { "EGLL", "KJFK", "LFPG", "RJTT", "WSSS", "VHHH", "YSSY", "OMDB", "ZZZZ" } )
            @DisplayName( "Should return true for valid ICAO codes" )
            void shouldAcceptValidIcaoCodes( String code )
            {
                assertTrue( AviationCodePatterns.isValidIcaoAirportCode( code ),
                            () -> "Expected '" + code + "' to be a valid ICAO airport code" );
            }

            @ParameterizedTest
            @NullSource
            @ValueSource( strings = { "", "egll", "EGL", "EGLLL", "IJKL", "JABC", "QABC", "XABC", "1ABC", "E1LL" } )
            @DisplayName( "Should return false for invalid ICAO codes" )
            void shouldRejectInvalidIcaoCodes( String code )
            {
                assertFalse( AviationCodePatterns.isValidIcaoAirportCode( code ),
                             () -> "Expected '" + code + "' to be an invalid ICAO airport code" );
            }

            @Test
            @DisplayName( "Should reject ICAO codes starting with I, J, Q, or X" )
            void shouldRejectReservedFirstLetters()
            {
                assertAll( () -> assertFalse( AviationCodePatterns.isValidIcaoAirportCode( "IABC" ) ),
                           () -> assertFalse( AviationCodePatterns.isValidIcaoAirportCode( "JABC" ) ),
                           () -> assertFalse( AviationCodePatterns.isValidIcaoAirportCode( "QABC" ) ),
                           () -> assertFalse( AviationCodePatterns.isValidIcaoAirportCode( "XABC" ) )
                );
            }
        }



        @ParameterizedTest
        @ValueSource( strings = { "JFK", "LHR", "CDG", "NRT", "SIN", "PEK", "DXB", "HND",
                                  "EGLL", "KJFK", "LFPG", "RJTT", "WSSS", "VHHH", "YSSY", "OMDB", "ZZZZ" } )
        @DisplayName( "Should return true for valid IATO or ICAO codes" )
        void shouldAcceptValidCodes( String code )
        {
            assertTrue( AviationCodePatterns.isValidAirportCode( code ),
                        () -> "Expected '" + code + "' to be a valid IATA or ICAO airport code" );
        }

        @ParameterizedTest
        @ValueSource( strings = { "", "jfk", "JfK", "JK", "JFKK", "123", "JF1", "JK1", "AB1",
                                  "egll", "EGLLL", "IJKL", "JABC", "QABC", "XABC", "1ABC", "E1LL" } )
        @DisplayName( "Should return false for invalid IATO or ICAO codes" )
        void shouldRejectInvalidCodes( String code )
        {
            assertFalse( AviationCodePatterns.isValidAirportCode( code ),
                        () -> "Expected '" + code + "' to be a invalid IATA or ICAO airport code" );
        }

    }


    @Nested
    @DisplayName( "Airline Codes" )
    class AirlineCodes
    {

        @Nested
        @DisplayName( "IATA Airline Code Validation" )
        class IataAirlineCodeTests
        {
            @ParameterizedTest
            @ValueSource( strings = { "BA", "AA", "LH", "AF", "KL", "X3", "9F", "A1", "Z9" } )
            @DisplayName( "Should return true for valid IATA airline codes" )
            void shouldAcceptValidIataAirlineCodes( String code )
            {
                assertTrue( AviationCodePatterns.isValidIataAirlineDesignator( code ),
                            () -> "Expected '" + code + "' to be a valid IATA airline code" );
            }

            @ParameterizedTest
            @NullSource
            @ValueSource( strings = { "", "B", "BA1", "11", "22", "99", "ba" } )
            @DisplayName( "Should return false for invalid IATA airline codes" )
            void shouldRejectInvalidIataAirlineCodes( String code )
            {
                assertFalse( AviationCodePatterns.isValidIataAirlineDesignator( code ),
                             () -> "Expected '" + code + "' to be an invalid IATA airline code" );
            }

            @ParameterizedTest
            @NullSource
            @ValueSource( strings = { "11", "23", "99" } )
            @DisplayName( "Should reject pure numeric IATA airline codes" )
            void shouldRejectPureNumericCodes( final String designator )
            {
                assertFalse( AviationCodePatterns.isValidIataAirlineDesignator( designator ),
                             () -> "Expected '" + designator + "' to be an invalid IATA airline designator" );
            }
        }

        @Nested
        @DisplayName( "ICAO Airline Code Validation" )
        class IcaoAirlineCodeTests
        {
            @ParameterizedTest
            @ValueSource( strings = { "BAW", "AAL", "DLH", "AFR", "KLM", "UAE", "SIA", "QFA" } )
            @DisplayName( "Should return true for valid ICAO airline codes" )
            void shouldAcceptValidIcaoAirlineCodes( String code )
            {
                assertTrue( AviationCodePatterns.isValidIcaoAirlineDesignator( code ),
                            () -> "Expected '" + code + "' to be a valid ICAO airline code" );
            }

            @ParameterizedTest
            @NullSource
            @ValueSource( strings = { "", "BA", "BAWW", "baw", "B1W", "BA1", "1AW", "B12" } )
            @DisplayName( "Should return false for invalid ICAO airline codes" )
            void shouldRejectInvalidIcaoAirlineCodes( String code )
            {
                assertFalse( AviationCodePatterns.isValidIcaoAirlineDesignator( code ),
                             () -> "Expected '" + code + "' to be an invalid ICAO airline code" );
            }
        }


        @ParameterizedTest
        @ValueSource( strings = { "BA", "AA", "LH", "AF", "KL", "X3", "9F", "A1", "Z9",
                                  "BAW", "AAL", "DLH", "AFR", "KLM", "UAE", "SIA", "QFA"
        } )
        @DisplayName( "Should return true for valid IATA or ICAO airline codes" )
        void shouldAcceptValidAirlineCodes( String code )
        {
            assertTrue( AviationCodePatterns.isValidAirlineDesignator( code ),
                        () -> "Expected '" + code + "' to be a valid IATA airline code" );
        }

        @ParameterizedTest
        @ValueSource( strings = { "", "B", "BA1", "11", "22", "99", "ba",
                                  "22", "45", "84",
                                  "BAWW", "baw", "B1W", "BA1", "1AW", "B12"
        } )
        @DisplayName( "Should return false for invalid IATA or ICAO airline codes" )
        void shouldRejectInvalidAirlineCodes( String code )
        {
            assertFalse( AviationCodePatterns.isValidAirlineDesignator( code ),
                        () -> "Expected '" + code + "' to be a invalid IATA or ICAO airline code" );
        }

    }

    @Nested
    @DisplayName( "Aircraft Registration" )
    class AircraftRegistration
    {
        @Nested
        @DisplayName( "Aircraft Registration Validation" )
        class AircraftRegistrationTests
        {
            @ParameterizedTest
            @ValueSource( strings = { "G-ABCD", "F-ABCD", "N1234A", "N12345", "N1", "G-EFGH", "F-XYZA" } )
            @DisplayName( "Should return true for valid aircraft registrations" )
            void shouldAcceptValidAircraftRegistrations( String registration )
            {
                assertTrue( AviationCodePatterns.isValidAircraftRegistration( registration ),
                            () -> "Expected '" + registration + "' to be a valid aircraft registration" );
            }

            @ParameterizedTest
            @NullSource
            @ValueSource(
                strings = { "", "G-ABC", "G-ABCDE", "F-AB", "F-ABC", "N123456", "N12345AB", "g-ABCD", "N1234AA" }
            )
            @DisplayName( "Should return false for invalid aircraft registrations" )
            void shouldRejectInvalidAircraftRegistrations( String registration )
            {
                assertFalse( AviationCodePatterns.isValidAircraftRegistration( registration ),
                             () -> "Expected '" + registration + "' to be an invalid aircraft registration" );
            }

            @Test
            @DisplayName( "Should accept various UK registration formats (G-XXXX)" )
            void shouldAcceptUKRegistrations()
            {
                assertAll( () -> assertThat( AviationCodePatterns.isValidAircraftRegistration( "G-ABCD" ) )
                                     .as( "G-ABCD" ).isTrue(),
                           () -> assertThat( AviationCodePatterns.isValidAircraftRegistration( "G-EFGH" ) )
                                     .as( "G-EFGH" ).isTrue(),
                           () -> assertThat( AviationCodePatterns.isValidAircraftRegistration( "G-TEST" ) )
                                     .as( "G-TEST" ).isTrue()
                );
            }

            @Test
            @DisplayName( "Should accept various French registration formats (F-XXXX)" )
            void shouldAcceptFrenchRegistrations()
            {
                assertAll( () -> assertThat( AviationCodePatterns.isValidAircraftRegistration( "F-ABCD" ) ).isTrue(),
                           () -> assertThat( AviationCodePatterns.isValidAircraftRegistration( "F-XYZZ" ) ).isTrue()
                );
            }

            // @Test
            @DisplayName( "Should accept various US registration formats (NXXXXX)" )
            @ParameterizedTest
            @ValueSource( strings = { "N12345", "N1234A", "N1" } )
            void shouldAcceptUSRegistrations( final String registration )
            {
                assertThat( AviationCodePatterns.isValidAircraftRegistration( registration ) )
                    .as( "Expected '" + registration + "' to be a valid US aircraft registration" )
                    .isTrue();
            }
        }
    }

    @Nested
    @DisplayName( "Pattern Constants Tests" )
    class PatternConstantsTests
    {
        @Test
        @DisplayName( "Should have non-null pattern constants" )
        void shouldHaveNonNullPatterns()
        {
            assertAll( () -> assertNotNull( AviationCodePatterns.AIRPORT_IATA_PATTERN ),
                       () -> assertNotNull( AviationCodePatterns.AIRPORT_ICAO_PATTERN ),
                       () -> assertNotNull( AviationCodePatterns.AIRPORT_IDENTIFIER_PATTERN ),
                       () -> assertNotNull( AviationCodePatterns.AIRCRAFT_REGISTRATION_PATTERN ),
                       () -> assertNotNull( AviationCodePatterns.AIRCRAFT_TYPE_IATA_PATTERN ),
                       () -> assertNotNull( AviationCodePatterns.AIRCRAFT_TYPE_ICAO_PATTERN ),
                       () -> assertNotNull( AviationCodePatterns.AIRLINE_DESIGNATOR_IATA_PATTERN ),
                       () -> assertNotNull( AviationCodePatterns.AIRLINE_DESIGNATOR_ICAO_PATTERN )
            );
        }

        @Test
        @DisplayName( "Should have non-null string constants" )
        void shouldHaveNonNullStringConstants()
        {
            assertAll( () -> assertNotNull( AviationCodePatterns.AIRPORT_IATA ),
                       () -> assertNotNull( AviationCodePatterns.AIRPORT_ICAO ),
                       () -> assertNotNull( AviationCodePatterns.AIRPORT_IDENTIFIER ),
                       () -> assertNotNull( AviationCodePatterns.AIRCRAFT_REGISTRATION ),
                       () -> assertNotNull( AviationCodePatterns.AIRCRAFT_TYPE_IATA ),
                       () -> assertNotNull( AviationCodePatterns.AIRCRAFT_TYPE_ICAO ),
                       () -> assertNotNull( AviationCodePatterns.AIRLINE_DESIGNATOR_IATA ),
                       () -> assertNotNull( AviationCodePatterns.AIRLINE_DESIGNATOR_ICAO )
            );
        }
    }
}
