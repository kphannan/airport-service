package com.example.airline.location.airport.persistence.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


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
