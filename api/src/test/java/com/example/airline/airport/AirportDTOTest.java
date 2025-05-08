/* (C) 2025 */

package com.example.airline.airport;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Set;

import com.example.rest.validation.ConstraintValidationUtility;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@code RegionDTO}.
 */
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class AirportDTOTest
{
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final URI       testURI1  = URI.create( "http://www.atlanta-airport.com" );
    private final URI       testURI2  = URI.create( "https://en.wikipedia.org/wiki/Hartsfield–Jackson_Atlanta_International_Airport" );

    @Nested
    @DisplayName( "constructor will" )
    class Constructor
    {
        @Test
        @DisplayName( "throw an exception when all arguments are null" )
        void constructor_allArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null )
                                                 );
            assertThat( thrown.getMessage() )
                    .contains( "ident is marked non-null but is null" );
        }

        @Test
        @DisplayName( "throw an exception when all but first argument is null" )
        void constructor_OneArgNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null, "KATL", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null )
                                                 );
            assertEquals( "type is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_TwoArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null, "KATL", "::TT::", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null )
                                                 );
            assertEquals( "name is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_ThreeArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null, "KATL", "::TT::", "::NAME::", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null )
                                                 );
            assertEquals( "latitude is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_FourArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null, "KATL", "::TT::", "::NAME::", BigDecimal.ONE, null, null, null, null, null, null, null, null, null, null, null, null, null, null )
                                                 );
            assertEquals( "longitude is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_FiveArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null,
                                                                         "KATL",
                                                                         "::TT::",
                                                                         "::NAME::",
                                                                         BigDecimal.ONE,
                                                                         BigDecimal.TWO,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null )
                                                 );
            assertEquals( "continent is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_SixArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null,
                                                                         "KATL",
                                                                         "::TT::",
                                                                         "::NAME::",
                                                                         BigDecimal.ONE,
                                                                         BigDecimal.TWO,
                                                                         null,
                                                                         "NA",
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null )
                                                 );
            assertEquals( "isoCountry is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_SevenArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null,
                                                                         "KATL",
                                                                         "::TT::",
                                                                         "::NAME::",
                                                                         BigDecimal.ONE,
                                                                         BigDecimal.TWO,
                                                                         null,
                                                                         "NA",
                                                                         "PH",
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null )
                                                 );
            assertEquals( "isoRegion is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_EightArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null,
                                                                         "KATL",
                                                                         "::TT::",
                                                                         "::NAME::",
                                                                         BigDecimal.ONE,
                                                                         BigDecimal.TWO,
                                                                         null,
                                                                         "NA",
                                                                         "PH",
                                                                         "CB",
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null )
                                                 );
            assertEquals( "municipality is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_NineArgsNotNullAllOtherArgsNull_throwsIllegalArgument()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new AirportDTO( null,
                                                                         "KATL",
                                                                         "::TT::",
                                                                         "::NAME::",
                                                                         BigDecimal.ONE,
                                                                         BigDecimal.TWO,
                                                                         null,
                                                                         "NA",
                                                                         "PH",
                                                                         "CB",
                                                                         "Some city",
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null )
                                                 );
            assertEquals( "scheduledService is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "throw an exception when all but first two arguments are null" )
        void constructor_withRequiredArgs_returnsInstance()
        {
            final AirportDTO dto = new AirportDTO( null,
                                                   "KATL",
                                                   "::TT::",
                                                   "::NAME::",
                                                   BigDecimal.ONE,
                                                   BigDecimal.TWO,
                                                   null,
                                                   "NA",
                                                   "PH",
                                                   "CB",
                                                   "Some city",
                                                   "no",
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null
                                                 );
            assertNotNull( dto );
        }

    }


    @Nested
    @DisplayName( "NewAirportDTO - Validation" )
    class ValidationGroup
    {
        @Test
        @DisplayName( "reject a blank Airport ID" )
        void airport_blankId_returnsViolation()
        {
            final AirportDTO itemUnderTest = new AirportDTO( null,
                                                             "KATL",
                                                             "::TT::",
                                                             "::NAME::",
                                                             BigDecimal.ONE,
                                                             BigDecimal.TWO,
                                                             null,
                                                             "NA",
                                                             "PH",
                                                             "PH-CB",
                                                             "Some city",
                                                             "no",
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null
            );
            final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "id", "An airport id is required" )
                                           );
        }

        @Nested
        @DisplayName( "an IDENT conforms to" )
        class ValidationIdent
        {
            @Test
            @DisplayName( "not blank" )
            void airport_blankIdent_returnsViolation()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "   ",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "ident", "A 4 to 7 character airport ident code is required" ),
                                                 tuple( "ident", "Airport ident must a unique 4 to 7 character code following a specific pattern" )
                                               );
            }


            @Test
            @DisplayName( "not too short" )
            void airport_tooFewCharactersForIdent_returnsViolation()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "KA",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "ident", "Airport ident must a unique 4 to 7 character code following a specific pattern" )
                                               );
            }

            @Test
            @DisplayName( "not numeric" )
            void airport_numericIdent_returnsViolation()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "12345",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "ident", "Airport ident must a unique 4 to 7 character code following a specific pattern" )
                                               );
            }

            @Test
            @DisplayName( "<aa><nn> is accepted" )
            void airport_aann_isAccepted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "AB12",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }

            @Test
            @DisplayName( "<aa><nnn> is rejected" )
            void airport_aannn_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "AB123",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "ident", "Airport ident must a unique 4 to 7 character code following a specific pattern" )
                                               );
            }

            @Test
            @DisplayName( "<aa>-<nnnn> is accepted" )
            void airport_aannnn_isAccepted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "AB-1223",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }

            @Test
            @DisplayName( "<aaaa> is accepted" )
            void airport_aaaa_isAccepted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }
        }

        @Nested
        @DisplayName( "a continent code is" )
        class ValidationContinent
        {
            @Test
            @DisplayName( "blank is rejected" )
            void airportContinent_blank_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "  ",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                             tuple( "continent", "A 2-character continent code is required" ),
                                             tuple( "continent", "Continent code must be 2 uppercase characters" )
                                               );
            }


            @Test
            @DisplayName( "<a> is rejected" )
            void airportContinent_a_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "A",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "continent", "Continent code must be 2 uppercase characters" )
                                               );
            }

            @Test
            @DisplayName( "<aaa> is rejected" )
            void airportContinent_aaa_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428_101 ),
                                                                 1026,
                                                                 "ABC",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "continent", "Continent code must be 2 uppercase characters" )
                                               );
            }

            @Test
            @DisplayName( "<aa> is accepted" )
            void airportContinent_aas_isAccepted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "AA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }

            @Test
            @DisplayName( "numeric is rejected" )
            void airportContinent_numeric_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "12",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "continent", "Continent code must be 2 uppercase characters" )
                                               );
            }

        }

        @Nested
        @DisplayName( "a country code is" )
        class ValidationCountry
        {
            @Test
            @DisplayName( "blank is rejected" )
            void airportCountry_blank_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                           "ABCD",
                                                           "large_airport",
                                                           "Hartsfield Jackson Atlanta International Airport",
                                                           BigDecimal.valueOf( 33.6367 ),
                                                           BigDecimal.valueOf( -84.428101 ),
                                                           1026,
                                                           "NA",
                                                           "  ",
                                                           "US-GA",
                                                           "Atlanta, Georgia",
                                                           "yes",
                                                           "KATL",
                                                           "KATL",
                                                           "ATL",
                                                           "ATL",
                                                           testURI1,
                                                           testURI2,
                                                           "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                             tuple( "isoCountry", "An ISO 3166:1-alpha2 country code is required" ),
                                             tuple( "isoCountry", "Country code must a valid ISO 3166:1-alpha2" )
                                               );
            }

            @Test
            @DisplayName( "<a> is rejected" )
            void airportCountry_a_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "A",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "isoCountry", "Country code must a valid ISO 3166:1-alpha2" )
                                               );
            }

            @Test
            @DisplayName( "<aaa> is rejected" )
            void airportCountry_aaa_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "ABC",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "isoCountry", "Country code must a valid ISO 3166:1-alpha2" )
                                               );
            }


            @Test
            @DisplayName( "<aa> is accepted" )
            void airportCountry_aa_isAccepted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "QQ",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }

            @Test
            @DisplayName( "numeric is rejected" )
            void airportCountry_numeric_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "12",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "isoCountry", "Country code must a valid ISO 3166:1-alpha2" )
                                               );
            }


        }

        @Nested
        @DisplayName( "a region code is" )
        class ValidationRegion
        {
            @Test
            @DisplayName( "blank is rejected" )
            void airportRegion_blank_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "     ",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                             tuple( "isoRegion", "A unique region code is required" ),
                                             tuple( "isoRegion", "Region code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
                                               );
            }

            @Test
            @DisplayName( "numeric is rejected" )
            void airportRegion_numeric_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                           "ABCD",
                                                           "large_airport",
                                                           "Hartsfield Jackson Atlanta International Airport",
                                                           BigDecimal.valueOf( 33.6367 ),
                                                           BigDecimal.valueOf( -84.428101 ),
                                                           1026,
                                                           "NA",
                                                           "US",
                                                           "1234",
                                                           "Atlanta, Georgia",
                                                           "yes",
                                                           "KATL",
                                                           "KATL",
                                                           "ATL",
                                                           "ATL",
                                                           testURI1,
                                                           testURI2,
                                                           "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "isoRegion", "Region code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
                                               );
            }

            @Test
            @DisplayName( "<aa> is rejected" )
            void airportRegion_aa_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "AB",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "isoRegion", "Region code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
                                               );
            }

            @Test
            @DisplayName( "<aa>-<aa> is accepted" )
            void airportRegion_aa_aa_isAccepted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "AA-BB",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }

        }

        @Nested
        @DisplayName( "a icao code" )
        class ValidationIcao
        {
            @Test
            @DisplayName( "blank is rejected" )
            void airportIcao_blank_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "    ",
                                                                 "ATL",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "icaoCode", "IACO code has four alpha-numeric characters" )
                                               );
            }

            @Test
            @DisplayName( "<aaaa> is accepted" )
            void airportIcao_aaa_isAccpeted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "ABCD",
                                                                 "ABC",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }

            @Test
            @DisplayName( "<aaa> is rejected" )
            void airportIcao_aa_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "ABC",
                                                                 "ABC",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "icaoCode", "IACO code has four alpha-numeric characters" )
                                               );
            }

            @Test
            @DisplayName( "numeric is rejected" )
            void airportIcao_numeric_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "1234",
                                                                 "ABC",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "icaoCode", "IACO code has four alpha-numeric characters" )
                                               );
            }

        }

        @Nested
        @DisplayName( "a iata code" )
        class ValidationIata
        {
            @Test
            @DisplayName( "blank is rejected" )
            void airportIata_blank_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "   ",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "iataCode", "IATA code has three alphabetic characters" )
                                               );
            }

            @Test
            @DisplayName( "<aaa> is accepted" )
            void airportIata_aaa_isAccpeted()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "ABC",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                assertThat( constraintViolations.size() )
                        .isEqualTo( 0 );
            }

            @Test
            @DisplayName( "<aa> is rejected" )
            void airportIata_aa_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "AB",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "iataCode", "IATA code has three alphabetic characters" )
                                               );
            }

            @Test
            @DisplayName( "numeric is rejected" )
            void airportIata_numeric_isRejected()
            {
                final AirportDTO itemUnderTest = new AirportDTO( 1L,
                                                                 "ABCD",
                                                                 "large_airport",
                                                                 "Hartsfield Jackson Atlanta International Airport",
                                                                 BigDecimal.valueOf( 33.6367 ),
                                                                 BigDecimal.valueOf( -84.428101 ),
                                                                 1026,
                                                                 "NA",
                                                                 "US",
                                                                 "US-GA",
                                                                 "Atlanta, Georgia",
                                                                 "yes",
                                                                 "KATL",
                                                                 "KATL",
                                                                 "123",
                                                                 "ATL",
                                                                 testURI1,
                                                                 testURI2,
                                                                 "Key1, key2" );
                final Set<ConstraintViolation<AirportDTO>> constraintViolations = validator.validate( itemUnderTest );

                ConstraintValidationUtility
                        .assertConstraintErrors( constraintViolations,
                                                 tuple( "iataCode", "IATA code has three alphabetic characters" )
                                               );
            }

        }

    }

}
