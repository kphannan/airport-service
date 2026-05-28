package com.example.airline.airport;


import com.example.airline.location.LocationCodePatterns;
import com.example.utility.IgnoreGeneratedCoverage;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema( description = "Count of the number of Airports in a specific Continent." )
@IgnoreGeneratedCoverage
public record AirportCountInContinentDTO(

    @Schema( name = "continentCode",
             description = "de facto continent code",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2
             )
    @Pattern( regexp = LocationCodePatterns.CONTINENT_CODE,
              message = "Continent code must be 2 uppercase characters" )
    @NotBlank
    String continentCode,

    @Schema( name = "name",
             description = "Continent name",
             requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank
    @Size( max = 52 )
    String name,

    @Schema( name = "airportCount",
             description = "Number of airports on the continent",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minimum = "0" )
    @PositiveOrZero
    Long airportCount )
{}
