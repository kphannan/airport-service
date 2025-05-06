package com.example.rest.validation;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.groups.Tuple;


/**
 * Utility class for validating constraint violations.
 *
 * <p> This class is not intended to be instantiated. It contains static methods for
 * validating constraint violations and asserting that they match expected values.
 */
@Log4j2
public final class ConstraintValidationUtility
{
    // TODO add verification this is a valid utility class.

    private ConstraintValidationUtility() throws IllegalAccessException
    {
        throw new IllegalAccessException();
    }

    /**
     * Prints the constraint violations to the log.
     * @param <T> the type of the object being validated
     * @param constraints the set of constraint violations
     */
    public static <T> void showViolations( final Set<ConstraintViolation<T>> constraints )
    {
        constraints.forEach( cv -> log.info( cv ) );
    }

    /**
     * Asserts that the constraint errors match the expected informed tuples.
     */
    public static <T> void assertConstraintErrors( final Set<ConstraintViolation<T>> constraints, final Tuple...tuples )
    {
        showViolations( constraints );

        assertThat( constraints )
                .hasSize( tuples.length )
                .extracting(
                        t -> t.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                           )
                .containsExactlyInAnyOrder( tuples )
        ;
    }

}
