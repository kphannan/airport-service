package com.example.airline.location.airport.model;


import com.example.utility.IgnoreGeneratedCoverage;
import jakarta.validation.constraints.PositiveOrZero;
import org.jspecify.annotations.NonNull;


/**
 * Represents the number of airports in the identified country.
 *
 * @param countryCode  ISO 3166-1:alpha2 country code.
 * @param name         common name of the country.
 * @param airportCount number of airports in the country.
 */
@IgnoreGeneratedCoverage
public record AirportCountInCountry(

    @NonNull
    String countryCode,

    @NonNull
    String name,

    @PositiveOrZero
    long airportCount
)
{
}

