package com.example.airline.location.airport.persistence.model;


import com.example.airline.location.LocationCodePatterns;
import com.example.utility.IgnoreGeneratedCoverage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


/**
 * Number of airports within a continent.
 *
 * @param continentCode de facto abbreviation of the continent.
 * @param name          common name of the continent.
 * @param airportCount  number of airports on the continent.
 */
@IgnoreGeneratedCoverage
public record AirportCountInContinentEntity(
    @Pattern( regexp = LocationCodePatterns.CONTINENT_CODE,
              message = "Continent code must be 2 uppercase characters" )
    String continentCode,

    @NotBlank
    @Size( max = 52 )
    String name,

    @PositiveOrZero
    Long airportCount
)
{
}

