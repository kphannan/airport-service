package com.example.airline.airport;


import com.example.utility.IgnoreGeneratedCoverage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;




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
