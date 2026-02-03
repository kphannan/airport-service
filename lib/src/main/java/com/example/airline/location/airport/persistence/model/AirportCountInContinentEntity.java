package com.example.airline.location.airport.persistence.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;




public record AirportCountInContinentEntity(
        @Pattern( regexp = "[A-Z]{2}", message = "Continent code must be 2 uppercase characters" )
        String continentCode,
        @NotBlank
        @Size( max = 52 )
        String name,
        @PositiveOrZero
        Long airportCount )
{}

