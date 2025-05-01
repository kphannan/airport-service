package com.example.rest.validation;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.groups.Tuple;


// TODO add verification this is a valid utility class.
@Log4j2
public final class ConstraintValidationUtility
{
    private ConstraintValidationUtility() throws IllegalAccessException
    {
        throw new IllegalAccessException();
    }

    public static <T> void showViolations( Set<ConstraintViolation<T>> constraints )
    {
        constraints.forEach( cv -> log.info( cv ) );
    }

    /**
     * Asserts that the constraint errors match the expected informed tuples
     */
    public static <T> void assertConstraintErrors(Set<ConstraintViolation<T>> constraints, Tuple...tuples ) {
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
