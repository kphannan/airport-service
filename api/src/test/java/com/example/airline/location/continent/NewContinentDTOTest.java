package com.example.airline.location.continent;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.Set;

import com.example.rest.validation.ConstraintValidationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;



/**
 * Unit tests for the NewContinentDTO class.
 *
 * <p> This class contains unit tests for the NewContinentDTO class, which is used to represent a new continent
 * in the system. The tests cover various scenarios, including validation of input parameters and JSON mapping.
 */
@Log4j2
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" } )
class NewContinentDTOTest
{
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final URI       testURI   = URI.create( "http://test.domain/with/a/path" );

    @Nested
    @DisplayName( "constructor will" )
    class Constructor
    {

        @Test
        @DisplayName( "throw an exception when the continent code is null" )
        void newContinent_nullArgs_throwsNullPointer()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new NewContinentDTO( null, null, null, null )
                                                 );
            assertThat( thrown.getMessage() )
                    .contains( "code is marked non-null but is null" );
        }

        @Test
        @DisplayName( "throw an exception when the continent name is null" )
        void newContinent_nullName_throwsNullPointer()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new NewContinentDTO( "", null, null, null )
                                                 );
            assertThat( thrown.getMessage() )
                    .contains( "name is marked non-null but is null" );
        }

        @Test
        void newContinent_null_returnsViolation()
        {
            final NewContinentDTO                           itemUnderTest        = new NewContinentDTO();
            final Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "A 2-character code is required" ),
                                             tuple( "name", "Name is required" )
                                           );
        }

    }

    @Nested
    @DisplayName( "NewContinentDTO - Validation" )
    class ValidationGroup
    {
        @Test
        @DisplayName( "reject a blank continent code" )
        void newContinent_blankCode_returnsViolation()
        {
            final NewContinentDTO                           itemUnderTest        = new NewContinentDTO( "  ",
                                                                                                        "Name",
                                                                                                        testURI,
                                                                                                        "Key1, key2" );
            final Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "A 2-character code is required" ),
                                             tuple( "code", "Code must be 2 uppercase characters" )
                                           );
        }

        @Test
        @DisplayName( "reject single character code" )
        void newContinent_singleCharCode_returnsViolation()
        {
            final NewContinentDTO                           itemUnderTest        = new NewContinentDTO( "A",
                                                                                                        "Name",
                                                                                                        null,
                                                                                                        null );
            final Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must be 2 uppercase characters" )
                                           );
        }

        @Test
        @DisplayName( "reject numeric code" )
        void newContinent_digitCode_returnsViolation()
        {
            final NewContinentDTO                           itemUnderTest        = new NewContinentDTO( "42",
                                                                                                        "Name",
                                                                                                        null,
                                                                                                        null );
            final Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must be 2 uppercase characters" )
                                           );
        }

        @Test
        @DisplayName( "reject a blank continent name" )
        void newContinent_nullName_returnsViolation()
        {
            final NewContinentDTO itemUnderTest = new NewContinentDTO( "AA",
                                                                       "   ",
                                                                       null,
                                                                       null );

            final Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "name", "Name is required" ),
                                             tuple( "name", "Continent name must be 2 to 52 characters" ) );
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


        @Test
        void newContinentDTO_fromJson_instantiatesObject() throws JsonProcessingException
        {
            // --- given
            final String json =
                    """
                    {
                        "code": "AB",
                        "name": "::NAME::",
                        "wikiLink": "http://some.domain/path",
                        "keywords": "k1, k2, k3"
                    }
                    """;

            // --- when
            final NewContinentDTO object = objectMapper.readValue( json, NewContinentDTO.class );

            // --- then
            assertThat( object ).isNotNull();
            assertThat( object.getCode() ).isEqualTo( "AB" );
            assertThat( object.getName() ).isEqualTo( "::NAME::" );
            assertThat( object.getWikiLink() ).isEqualTo( URI.create( "http://some.domain/path" ) );
            assertThat( object.getKeywords() ).isEqualTo( "k1, k2, k3" );
        }
    }
}
