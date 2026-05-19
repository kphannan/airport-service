package com.example.airline.location.airport.persistence.model;

import com.example.utility.IgnoreGeneratedCoverage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


/**
 * Number of airports within a {@see Region}.
 *
 * @param regionCode An alphanumeric code for the high-level administrative subdivision of a
 *                   country where the airport is primarily located
 * @param name       Common name of the region.
 * @param airportCount total number of airports within the region.
 */
@IgnoreGeneratedCoverage
public record AirportCountInRegionEntity(
        @NotBlank( message = "A unique region code is required" )
        @Pattern( regexp = "[A-Z]{2}-[A-Z\\-]{1,4}",
                  message = "Region code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
        String regionCode,

        @NotBlank
        @Size( max = 80 )
        String name,

        @PositiveOrZero
        Long   airportCount
)
{}
