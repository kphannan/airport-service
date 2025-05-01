package com.example.airline.location;


import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Log4j2
public class NewContinentDTOTest
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
    @DisplayName( "constructor requires" )
    class Constructor
    {

        @Test
        void newContinent_nullArgs_throwsNullPointer()
        {
            Throwable thrown = assertThrows( NullPointerException.class,
                                             () -> new NewContinentDTO( null, null, null, null )
                                           );
            assertThat( thrown.getMessage() )
                    .contains( "code is marked non-null but is null" );
        }

        @Test
        void newContinent_nullName_throwsNullPointer()
        {
            Throwable thrown = assertThrows( NullPointerException.class,
                                             () -> new NewContinentDTO( "", null, null, null )
                                           );
            assertThat( thrown.getMessage() )
                    .contains( "name is marked non-null but is null" );
        }


        @Test
        void newContinent_blankCode_returnsViolation()
        {
            NewContinentDTO                           itemUnderTest        = new NewContinentDTO( "  ",
                                                                                                  "Name",
                                                                                                  testURI,
                                                                                                  "Key1, key2" );
            Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            assertConstraintErrors( constraintViolations,
                                    tuple( "code", "A 2-character code is required" ),
                                    tuple( "code", "Code must be 2 uppercase characters")
                                  );
        }

        @Test
        void newContinent_nullName_returnsViolation()
        {
            NewContinentDTO itemUnderTest = new NewContinentDTO( "AA",
                                                                 "   ",
                                                                 null,
                                                                 null );

            Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            assertConstraintErrors( constraintViolations, tuple("name", "Name is required" ) );
        }

        @Test
        void newContinent_null_returnsViolation()
        {
            NewContinentDTO                           itemUnderTest        = new NewContinentDTO();
            Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            assertConstraintErrors( constraintViolations,
                                    tuple("code", "A 2-character code is required" ),
                                    tuple("name", "Name is required" )
                                  );
        }

    }

    @Nested
    @DisplayName( "Validation" )
    class ValidationGroup
    {
        @Test
        @DisplayName( "reject single character code" )
        void newContinent_singleCharCode_returnsViolation()
        {
            NewContinentDTO                           itemUnderTest        = new NewContinentDTO( "A",
                                                                                                  "Name",
                                                                                                  null,
                                                                                                  null );
            Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            assertConstraintErrors( constraintViolations,
                                    tuple("code", "Code must be 2 uppercase characters" )
                                  );
        }

        @Test
        @DisplayName( "reject numeric code" )
        void newContinent_digitCode_returnsViolation()
        {
            NewContinentDTO                           itemUnderTest        = new NewContinentDTO( "42",
                                                                                                  "Name",
                                                                                                  null,
                                                                                                  null );
            Set<ConstraintViolation<NewContinentDTO>> constraintViolations = validator.validate( itemUnderTest );

            assertConstraintErrors( constraintViolations,
                                    tuple("code", "Code must be 2 uppercase characters" )
                                  );
        }
    }

    @Nested
    @DisplayName( "JSON mapping" )
    class JsonMapping
    {
    }


    private <T> void showViolations( Set<ConstraintViolation<T>> constraints )
    {
        constraints.forEach( cv -> log.error( cv ) );
    }

    /**
     * Asserts that the constraint errors match the expected informed tuples
     */
    private <T> void assertConstraintErrors(Set<ConstraintViolation<T>> constraints, Tuple...tuples) {
        showViolations( constraints );
        assertThat(constraints)
                .hasSize( tuples.length )
                .extracting(
                        t -> t.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                           )
                .containsExactlyInAnyOrder( tuples )
        ;
    }
}



//            constraintViolations.forEach( cv -> log.error( cv ) );
//            ConstraintViolation<NewContinentDTO>[] cva = constraintViolations.toArray(new ConstraintViolation[0] );
//            assertAll( () -> assertEquals( 1, constraintViolations.size() ),
//                       () -> assertEquals( "code", cva[0].getPropertyPath().toString() )
//                     );

//private <T> void assertConstraintErrors(Set<ConstraintViolation<T>> constraints, Tuple...tuples) {
//    assertThat(constraints)
//            .hasSize(tuples.length)
//            .extracting(
//                    t -> t.getPropertyPath().toString(), // field name
//                    ConstraintViolation::getMessage.     // error message
//                )
//                .containsExactlyInAnyOrder(tuples)
//    ;
//}
