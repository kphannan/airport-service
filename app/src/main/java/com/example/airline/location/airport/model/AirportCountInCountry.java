package com.example.airline.location.airport.model;


import com.example.utility.IgnoreGeneratedCoverage;
import org.jspecify.annotations.NonNull;


/**
 * Represents the number of airports in the identified country.
 *
 * @param countryCode  ISO 3166 country code.
 * @param name         common name of the country.
 * @param airportCount number of airports in the country.
 */
@IgnoreGeneratedCoverage
public record AirportCountInCountry(

    @NonNull
    String countryCode,

    String name,

    long airportCount
)
{
}

