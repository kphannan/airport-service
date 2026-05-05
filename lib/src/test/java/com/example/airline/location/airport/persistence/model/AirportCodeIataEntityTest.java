package com.example.airline.location.airport.persistence.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @Test
    @DisplayName( "Default value" )
    void ctor_noArgs_setsDefaultValues()
    {
        // --- given
        // --- then
        AirportCodeIataEntity cut = new AirportCodeIataEntity();

        // --- then
        assertThat( cut.getIataCode() ).matches( "ZZZ" );
    }

    @Test
    @DisplayName( "valid code" )
    void ctor_validArg_setsValues()
    {
        // --- given
        // --- then
        AirportCodeIataEntity cut = new AirportCodeIataEntity( "ABC" );

        // --- then
        assertThat( cut.getIataCode() ).matches( "ABC" );
    }

    @Test
    @DisplayName( "valid code with padding" )
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

    @Test
    @DisplayName( "invalid code" )
    void ctor_invalidArg_setsDefaultValues()
    {
        // --- given
        // --- then
        Throwable thrown = assertThrows( IllegalArgumentException.class,
                                         () -> new AirportCodeIataEntity( "abc" )
                                       );

        // --- then
        assertAll( () -> assertThat( thrown.getMessage() ).matches( "IATA code 'abc' is invalid" )
                 );
    }


}
