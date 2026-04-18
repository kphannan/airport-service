package com.example.airline.location.airport.persistence.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 *
 * @author kevin
 * @since 2026-03-15
 * <p>
 * Copyright (c) 2020-2026
 */
public class AirportCodeIataEntityTest
{
    @Test
    void ctor_noArgs_setsDefaultValues()
    {
        // --- given
        // --- then
        AirportCodeIataEntity cut = new AirportCodeIataEntity();

        // --- then
        assertThat( cut.getIataCode() ).matches( "ZZZ" );
    }

    @Test
    void ctor_validArg_setsDefaultValues()
    {
        // --- given
        // --- then
        AirportCodeIataEntity cut = new AirportCodeIataEntity( "ABC" );

        // --- then
        assertThat( cut.getIataCode() ).matches( "ABC" );
    }


}
