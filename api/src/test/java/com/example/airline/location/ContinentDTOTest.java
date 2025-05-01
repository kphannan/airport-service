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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@code ContinentDTO}.
 */
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class ContinentDTOTest
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
                                             () -> new ContinentDTO( null, null, null, null, null )
                                           );
            assertThat( thrown.getMessage() )
                    .contains( "code is marked non-null but is null" );
        }

        @Test
        @DisplayName( "reject null for the continent code" )
        void constructor_NullArgs_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new ContinentDTO( null, null, null, null, null )
                                                 );
            assertEquals( "code is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "accept minimum required arguments" )
        void constructor_withArgs_DoesNotCrash()
        {
            final ContinentDTO dto = new ContinentDTO( null, "EE", "::NAME::", null, null );

            assertNotNull( dto );
        }

        @Test
        @DisplayName( "reject null for the continent code" )
        void constructor_CodeNull_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new ContinentDTO( null, null, ";;NAME;;", null, null )
                                                 );
            assertEquals( "code is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "reject null for the continent name" )
        void constructor_NameNull_throws()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new ContinentDTO( null, "NN", null, null, null )
                                                 );
            assertEquals( "name is marked non-null but is null", thrown.getMessage() );
        }
    }


    @Nested
    @DisplayName( "NewContinentDTO - Validation" )
    class ValidationGroup
    {
        @Test
        @DisplayName( "reject a blank continent code" )
        void continent_blankid_returnsViolation()
        {
            ContinentDTO itemUnderTest = new ContinentDTO( null,
                                                           "CC",
                                                           "Name",
                                                           testURI,
                                                           "Key1, key2" );
            Set<ConstraintViolation<ContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "id", "A continent id is required" )
                                           );
        }

        @Test
        @DisplayName( "reject a blank continent code" )
        void continent_blankCode_returnsViolation()
        {
            ContinentDTO itemUnderTest = new ContinentDTO( 1,
                                                           "  ",
                                                           "Name",
                                                           testURI,
                                                           "Key1, key2" );
            Set<ConstraintViolation<ContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "A 2-character code is required" ),
                                             tuple( "code", "Code must be 2 uppercase characters" )
                                           );
        }

        @Test
        @DisplayName( "reject single character code" )
        void continent_singleCharCode_returnsViolation()
        {
            ContinentDTO itemUnderTest = new ContinentDTO( 1,
                                                           "A",
                                                           "Name",
                                                           null,
                                                           null );
            Set<ConstraintViolation<ContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must be 2 uppercase characters" )
                                           );
        }

        @Test
        @DisplayName( "reject numeric code" )
        void continent_digitCode_returnsViolation()
        {
            ContinentDTO itemUnderTest = new ContinentDTO( 1,
                                                           "42",
                                                           "Name",
                                                           null,
                                                           null );
            Set<ConstraintViolation<ContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must be 2 uppercase characters" )
                                           );
        }

        @Test
        @DisplayName( "reject a blank continent name" )
        void continent_nullName_returnsViolation()
        {
            ContinentDTO itemUnderTest = new ContinentDTO( 1,
                                                           "AA",
                                                           "   ",
                                                           null,
                                                           null );

            Set<ConstraintViolation<ContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "name", "Name is required" ) );
        }

        @Test
        @DisplayName( "reject a blank continent code" )
        void continent_missingAllRequired_returnsViolation()
        {
            ContinentDTO itemUnderTest = new ContinentDTO( null,
                                                           "  ",
                                                           "   ",
                                                           testURI,
                                                           "Key1, key2" );
            Set<ConstraintViolation<ContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "id", "A continent id is required" ),
                                             tuple( "code", "A 2-character code is required" ),
                                             tuple( "code", "Code must be 2 uppercase characters" ),
                                             tuple( "name", "Name is required" )
                                           );
        }


    }


}
