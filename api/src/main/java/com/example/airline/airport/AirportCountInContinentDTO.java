package com.example.airline.airport;


import com.example.utility.IgnoreGeneratedCoverage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


/**
 * Count of the number of Airports in a specific Continent.
 *
 * @param continentCode alpha code for the Continent.
 * @param name common name of the Continent.
 * @param airportCount number of Airports within the Continent.
 */
@IgnoreGeneratedCoverage
public record AirportCountInContinentDTO(
    @Pattern( regexp = "[A-Z]{2}", message = "Continent code must be 2 uppercase characters" )
    String continentCode,
    @NotBlank
    @Size( max = 52 )
    String name,
    @PositiveOrZero
    Long airportCount )
{}
