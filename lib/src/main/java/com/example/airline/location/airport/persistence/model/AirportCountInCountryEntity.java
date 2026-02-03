package com.example.airline.location.airport.persistence.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


public record AirportCountInCountryEntity(
    @NotBlank( message = "An ISO 3166:1-alpha2 country code is required" )
    @Pattern( regexp = "[A-Z]{2}", message = "Country code must a valid ISO 3166:1-alpha2" )
    String countryCode,
    @NotBlank
    @Size( max = 52 )
    String name,
    @PositiveOrZero
    Long   airportCount
)
{}
