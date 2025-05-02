/* (C) 2025 */

package com.example.airline.location;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.Set;

import com.example.rest.validation.ConstraintValidationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;


/**
 * Unit tests for the {@code ContinentDTO}.
 */
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class CountryDTOTest
{
    private Validator validator;
    private URI       testURI;

    @BeforeEach
    void setUp()
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();

        testURI = URI.create( "http://test.domain/with/a/path" );
    }

    @Nested
    @DisplayName( "constructor will" )
    class Constructor
    {

        @Test
        @DisplayName( "throw an exception when the continent primary id is null" )
        void continent_nullArgs_throwsNullPointer()
        {
            Throwable thrown = assertThrows( IllegalArgumentException.class,
                                             () -> new CountryDTO( null, null, null, null, null, null )
                                           );
            assertThat( thrown.getMessage() )
                    .contains( "code is marked non-null but is null" );
        }

        @Test
        @DisplayName( "reject null for the continent code" )
        void constructor_NullArgs_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new CountryDTO( null, null, null, null, null, null )
                                                 );
            assertEquals( "code is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "accept minimum required arguments" )
        void constructor_withArgs_DoesNotCrash()
        {
            final CountryDTO dto = new CountryDTO( null, "EE", "::NAME::", "AS", null, null );

            assertNotNull( dto );
        }

        @Test
        @DisplayName( "reject null for the continent code" )
        void constructor_CodeNull_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new CountryDTO( null, null, ";;NAME;;", null, null, null )
                                                 );
            assertEquals( "code is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "reject null for the continent name" )
        void constructor_NameNull_throws()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new CountryDTO( null, "NN", null, null, null, null )
                                                 );
            assertEquals( "name is marked non-null but is null", thrown.getMessage() );
        }
    }


    @Nested
    @DisplayName( "NewCountryDTO - Validation" )
    class ValidationGroup
    {
        @Test
        @DisplayName( "reject a blank continent code" )
        void continent_blankid_returnsViolation()
        {
            CountryDTO itemUnderTest = new CountryDTO( null,
                                                           "CC",
                                                           "Name",
                                                           "NA",
                                                           testURI,
                                                           "Key1, key2" );
            Set<ConstraintViolation<CountryDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "id", "A country id is required" )
                                           );
        }

        @Test
        @DisplayName( "reject a blank continent code" )
        void continent_blankCode_returnsViolation()
        {
            CountryDTO itemUnderTest = new CountryDTO( 1,
                                                           "  ",
                                                           "Name",
                                                           "NA",
                                                           testURI,
                                                           "Key1, key2" );
            Set<ConstraintViolation<CountryDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "An ISO 3166:1-alpha2 country code is required" ),
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2" )
                                           );
        }

        @Test
        @DisplayName( "reject single character code" )
        void continent_singleCharCode_returnsViolation()
        {
            CountryDTO itemUnderTest = new CountryDTO( 1,
                                                           "A",
                                                           "Name",
                                                           "AS",
                                                           null,
                                                           null );
            Set<ConstraintViolation<CountryDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2" )
                                           );
        }

        @Test
        @DisplayName( "reject numeric code" )
        void continent_digitCode_returnsViolation()
        {
            CountryDTO itemUnderTest = new CountryDTO( 1,
                                                           "42",
                                                           "Name",
                                                           "NA",
                                                           null,
                                                           null );
            Set<ConstraintViolation<CountryDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2" )
                                           );
        }

        @Test
        @DisplayName( "reject a blank continent name" )
        void continent_nullName_returnsViolation()
        {
            CountryDTO itemUnderTest = new CountryDTO( 1,
                                                           "AA",
                                                           "   ",
                                                           "NA",
                                                           null,
                                                           null );

            Set<ConstraintViolation<CountryDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "name", "Name is required" ),
                                             tuple( "name", "Country name must be between 2 and 52 characters" ) );
        }

        @Test
        @DisplayName( "reject a blank continent code" )
        void continent_missingAllRequired_returnsViolation()
        {
            CountryDTO itemUnderTest = new CountryDTO( null,
                                                           "  ",
                                                           "   ",
                                                           "  ",
                                                           testURI,
                                                           "Key1, key2" );
            Set<ConstraintViolation<CountryDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "id", "A country id is required" ),
                                             tuple( "continent", "A 2-character continent code is required" ),
                                             tuple( "continent", "Continent code must be 2 uppercase characters" ),
                                             tuple( "name", "Name is required" ),
                                             tuple( "name", "Country name must be between 2 and 52 characters" ),
                                             tuple( "code", "An ISO 3166:1-alpha2 country code is required" ),
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2" )
                                           );
        }


    }

    @Nested
    @DisplayName( "JSON mapping" )
    class JsonMapping
    {
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp()
        {
            objectMapper = new ObjectMapper();
        }

        @Nested
        @DisplayName( "JSON to object" )
        class FromJson
        {
            @Test
            void continentDTO_fromJson_instantiatesObject() throws JsonProcessingException
            {
                // --- given
                final String json =
                        """
                                {
                                    "id": 42,
                                    "code": "AB",
                                    "name": "::NAME::",
                                    "continent": "AS",
                                    "wikiLink": "http://some.domain/path",
                                    "keywords": "k1, k2, k3"
                                }
                                """;

                // --- when
                final CountryDTO object = objectMapper.readValue( json, CountryDTO.class );

                // --- then
                assertThat( object ).isNotNull();
                assertThat( object.getCode() ).isEqualTo( "AB" );
                assertThat( object.getName() ).isEqualTo( "::NAME::" );
                assertThat( object.getWikipediaLink() ).isEqualTo( URI.create( "http://some.domain/path" ) );
                assertThat( object.getKeywords() ).isEqualTo( "k1, k2, k3" );
            }
        }


        @Nested
        @DisplayName( "Object to JSON" )
        class ToJson
        {
            @Test
            void continentDTO_fromObject_producesJSON() throws JsonProcessingException
            {
                // --- given
//                final CountryDTO dto = new CountryDTO();
                final CountryDTO dto = new CountryDTO( 42,
                                                       "AA",
                                                       "::NAME::",
                                                       "AS",
                                                       URI.create( "http://some.domain/path" ),
                                                       "k1, k2, k3" );
                final String json =
                        """
                                {
                                    "id": 42,
                                    "code": "AA",
                                    "name": "::NAME::",
                                    "continent": "AS",
                                    "wikiLink": "http://some.domain/path",
                                    "keywords": "k1, k2, k3"
                                }
                                """;

                // --- when
                final String result = objectMapper.writeValueAsString( dto );

                // --- then
                JSONAssert.assertEquals( json, result, JSONCompareMode.LENIENT );
//                assertThat( object ).isNotNull();
//                assertThat( object.getCode() ).isEqualTo( "AB" );
//                assertThat( object.getName() ).isEqualTo( "::NAME::" );
//                assertThat( object.getWikipediaLink() ).isEqualTo( URI.create( "http://some.domain/path" ) );
//                assertThat( object.getKeywords() ).isEqualTo( "k1, k2, k3" );
            }
        }
    }

}
