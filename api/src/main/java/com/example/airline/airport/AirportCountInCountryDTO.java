package com.example.airline.airport;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


public record AirportCountInCountryDTO(
        @JsonProperty( "isoCountry" )
        @Schema( name = "isoCountry",
                 description = "The two-character ISO 3166:1-alpha2 code for the country.",
                 requiredMode = Schema.RequiredMode.REQUIRED,
                 minLength = 2,
                 maxLength = 2,
                 pattern = "[A-Z]{2}",
                 example = "US" )
        @NotBlank( message = "An ISO 3166:1-alpha2 country code is required" )
        @Pattern( regexp = "[A-Z]{2}", message = "Country code must a valid ISO 3166:1-alpha2" )
        String countryCode,
        @NotBlank
        @Size( max = 52 )
        String name,
        @PositiveOrZero
        Long airportCount
)
{}
