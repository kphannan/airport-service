package com.example.airline.airport;


import com.example.airline.location.LocationCodePatterns;
import com.example.utility.IgnoreGeneratedCoverage;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


/**
 * Count of the number of Airports in a specific Country.
 *
 * @param countryCode Alpha code of the country.
 * @param name name of the Country.
 * @param airportCount number of airports within the country.
 */
@IgnoreGeneratedCoverage
public record AirportCountInCountryDTO(
        @JsonProperty( "isoCountry" )
        @Schema( name = "isoCountry",
                 description = "The two-character ISO 3166-1:alpha2 code for the country.",
                 requiredMode = Schema.RequiredMode.REQUIRED,
                 minLength = 2,
                 maxLength = 2,
                 pattern = LocationCodePatterns.ISO_COUNTRY_CODE,
                 example = "US" )
        @NotBlank( message = "An ISO 3166-1:alpha2 country code is required" )
        @Pattern( regexp = LocationCodePatterns.ISO_COUNTRY_CODE,
                  message = "Country code must be a valid ISO 3166-1:alpha2" )
        String countryCode,

        @Schema( description = "Name of the country" )
        @NotBlank
        @Size( max = 52 )
        String name,

        @Schema( description = "Number of airports in the country")
        @PositiveOrZero
        Long airportCount
)
{}
