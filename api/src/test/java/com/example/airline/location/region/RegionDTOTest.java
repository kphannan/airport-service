/* (C) 2025 */

package com.example.airline.location.region;


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
 * Unit tests for the {@code RegionDTO}.
 */
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class RegionDTOTest
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
        @DisplayName( "throw an exception when the region primary id is null" )
        void region_nullArgs_throwsNullPointer()
        {
            Throwable thrown = assertThrows( IllegalArgumentException.class,
                                             () -> new RegionDTO( null, null, null, null, null, null, null, null )
                                           );
            assertThat( thrown.getMessage() )
                    .contains( "code is marked non-null but is null" );
        }

        @Test
        @DisplayName( "reject null for the continent code" )
        void constructor_NullArgs_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new RegionDTO( 1, null, null, null, null, null, null, null )
                                                 );
            assertEquals( "code is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "accept minimum required arguments" )
        void constructor_withArgs_DoesNotCrash()
        {
            final RegionDTO dto = new RegionDTO( null, "IE-D", "D", "County Dublin", "IE", "EU", null, null );

            assertNotNull( dto );
        }

        @Test
        @DisplayName( "reject null for the continent code" )
        void constructor_CodeNull_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new RegionDTO( null, null, "D", "County Dublin", "IE", "EU", null, null )
                                                 );
            assertEquals( "code is marked non-null but is null", thrown.getMessage() );
        }

        @Test
        @DisplayName( "reject null for the continent name" )
        void constructor_NameNull_throws()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> new RegionDTO( null, "IE-D", "D", null, "IE", "EU", null, null )
                                                 );
            assertEquals( "name is marked non-null but is null", thrown.getMessage() );
        }
    }


    @Nested
    @DisplayName( "NewRegionDTO - Validation" )
    class ValidationGroup
    {
        @Test
        @DisplayName( "reject a blank continent code" )
        void region_blankId_returnsViolation()
        {
            RegionDTO itemUnderTest = new RegionDTO( null,
                                                     "CC",
                                                     "LOCAL",
                                                     "Name",
                                                     "IE",
                                                     "EU",
                                                     testURI,
                                                     "Key1, key2" );
            Set<ConstraintViolation<RegionDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" ),
                                             tuple( "id", "A Region id is required" )
                                           );
        }

        @Test
        @DisplayName( "reject a blank continent code" )
        void region_blankCode_returnsViolation()
        {
            RegionDTO itemUnderTest = new RegionDTO( 1,
                                                     "  ",
                                                     "LOCAL",
                                                     "Name",
                                                     "IE",
                                                     "EU",
                                                     testURI,
                                                     "Key1, key2" );
            Set<ConstraintViolation<RegionDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "A unique region code is required" ),
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
                                           );
        }

        @Test
        @DisplayName( "reject single character code" )
        void region_singleCharCode_returnsViolation()
        {
            RegionDTO itemUnderTest = new RegionDTO( 1,
                                                     "A",
                                                     "LOCAL",
                                                     "Name",
                                                     "IE",
                                                     "EU",
                                                     null,
                                                     null );
            Set<ConstraintViolation<RegionDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
                                           );
        }

        @Test
        @DisplayName( "reject numeric code" )
        void region_digitCode_returnsViolation()
        {
            RegionDTO itemUnderTest = new RegionDTO( 1,
                                                     "42",
                                                     "LOCAL",
                                                     "Name",
                                                     "IE",
                                                     "EU",
                                                     null,
                                                     null );
            Set<ConstraintViolation<RegionDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
                                           );
        }

        @Test
        @DisplayName( "reject a blank continent name" )
        void region_nullName_returnsViolation()
        {
            RegionDTO itemUnderTest = new RegionDTO( 1,
                                                     "AA",
                                                     "LOCAL",
                                                     "   ",
                                                     "IE",
                                                     "EU",
                                                     null,
                                                     null );

            Set<ConstraintViolation<RegionDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" ),
                                             tuple( "name", "Region name is required" ),
                                             tuple( "name", "Region name must be between 7 and 80 characters" ) );
        }

        @Test
        @DisplayName( "reject a blank continent code" )
        void region_missingAllRequired_returnsViolation()
        {
            RegionDTO itemUnderTest = new RegionDTO( null,
                                                     "  ",
                                                     "  ",
                                                     "   ",
                                                     "IE",
                                                     "EU",
                                                     testURI,
                                                     "Key1, key2" );
            Set<ConstraintViolation<RegionDTO>> constraintViolations = validator.validate( itemUnderTest );

            ConstraintValidationUtility
                    .assertConstraintErrors( constraintViolations,
                                             tuple( "id", "A Region id is required" ),
                                             tuple( "code", "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" ),
                                             tuple( "code", "A unique region code is required" ),
                                             tuple( "localCode", "A unique local code is required" ),
                                             tuple( "localCode", "local code" ),
                                             tuple( "name", "Region name is required" ),
                                             tuple( "name", "Region name must be between 7 and 80 characters" )
                                           );
        }
    }

}
