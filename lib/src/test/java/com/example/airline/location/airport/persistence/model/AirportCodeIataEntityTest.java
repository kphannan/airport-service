package com.example.airline.location.airport.persistence.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author Kevin
 * @since 2026-03-15
 * <p>
 * Copyright (c) 2020-2026
 */
@DisplayName( "IATA code (entity)" )
public class AirportCodeIataEntityTest
{
    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {
        @Test
        @DisplayName( "Default constructor sets fallback value ZZZ" )
        void ctor_noArgs_setsDefaultValues()
        {
            // --- given
            // --- then
            AirportCodeIataEntity cut = new AirportCodeIataEntity();

            // --- then
            assertThat( cut.getIataCode() ).isEqualTo( "ZZZ" );
        }

        @ParameterizedTest
        @ValueSource( strings = { "JFK", "LHR", "DXB", "ABC", "ZZZ" } )
        @DisplayName( "Valid 3-letter uppercase codes are accepted" )
        void ctor_validArg_setsValues( final String code )
        {
            // --- given
            // --- when
            AirportCodeIataEntity cut = new AirportCodeIataEntity( code );

            // --- then
            assertThat( cut.getIataCode() ).isEqualTo( code );
        }



        @Test
        @DisplayName( "valid code with padding")
        void ctor_validArgWithPadding_setsValues()
        {
            // --- given
            // --- then
            Throwable thrown = assertThrows( IllegalArgumentException.class,
                                             () -> new AirportCodeIataEntity( " XYZ " )
                                           );

            // --- then
            assertThat( thrown.getMessage() ).matches( "IATA code ' XYZ ' is invalid" );
        }

        // @Test
        // @DisplayName( "invalid code")
        // void ctor_invalidArg_setsDefaultValues()
        // {
        //     // --- given
        //     // --- then
        //     Throwable thrown = assertThrows( IllegalArgumentException.class,
        //                                      () -> new AirportCodeIataEntity( "abc" )
        //                                    );
        //
        //     // --- then
        //     assertAll( () -> assertThat( thrown.getMessage() ).matches( "IATA code 'abc' is invalid" )
        //              );
        // }

        @ParameterizedTest
        @NullSource
        @ValueSource( strings = {
            "abc",      // Lowercase
            "Abc",      // Mixed case
            "123",      // Numeric
            "A1B",      // Alphanumeric
            "AB",       // Too short
            "ABCD",     // Too long
            "A B",      // Contains space
            "A-B",      // Special characters
            " XYZ "     // Padding/Whitespace
        } )
        @DisplayName( "Invalid codes throw IllegalArgumentException" )
        void ctor_invalidArg_throwsException( final String code )
        {
            assertThatThrownBy( () -> new AirportCodeIataEntity( code ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( String.format( "IATA code '%s' is invalid", code ) );
        }

    }


    @Nested
    @DisplayName( "Equals()" )
    class Equals
    {
        @Test
        @DisplayName( "Equality is based on the IATA code value" )
        void equals_sameCode_returnsTrue()
        {
            AirportCodeIataEntity entity1 = new AirportCodeIataEntity( "LHR" );
            AirportCodeIataEntity entity2 = new AirportCodeIataEntity( "LHR" );
            AirportCodeIataEntity entity3 = new AirportCodeIataEntity( "JFK" );

            assertAll( () -> assertThat( entity1.equals( entity1 ) ).isTrue(),
                       () -> assertThat( entity1.equals( entity2 ) ).isTrue(),
                       () -> assertThat( entity1.hashCode() ).isEqualTo( entity2.hashCode() ),
                       () -> assertThat( entity1 ).isNotEqualTo( entity3 )
            );
        }

        @Test
        @DisplayName( "Equality handles nulls and different types" )
        void equals_edgeCases_returnsFalse()
        {
            AirportCodeIataEntity entity = new AirportCodeIataEntity( "LHR" );

            assertAll( () -> assertThat( entity ).isNotEqualTo( null ),
                       () -> assertThat( entity ).isNotEqualTo( "LHR" ) // Different type
            );
        }
    }

}
