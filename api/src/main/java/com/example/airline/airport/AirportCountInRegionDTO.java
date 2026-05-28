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
 * Count of the number of Airports in a specific Region.
 *
 * @param regionCode Region code of a single Region
 * @param name name of the Region
 * @param airportCount number of airports within the Region
 */
@Schema( description = "Represents the number of airports in a high-level administrative subdivision of a country" )
@IgnoreGeneratedCoverage
public record AirportCountInRegionDTO(
    @JsonProperty( "isoRegion" )
    @Schema( name = "isoRegion",
             description =
                 """
                 ISO 3166-2 Codes for the representation of names of countries and their subdivisions.
                 
                 The two-character country code followed by an abbreviation
                 for the administrative subdivision (e.g., province, state)
                 """,
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 3,
             maxLength = 7,
             pattern = LocationCodePatterns.ISO_REGION_CODE,
             example = "IE-D" )
    @NotBlank( message = "A unique region code is required" )
    @Pattern( regexp = LocationCodePatterns.ISO_REGION_CODE,
              message = "Region code must be a valid ISO 3166-1:alpha2 followed by '-' and a local code" )
    String regionCode,

    @Schema( description = "Common name of the region.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             type = "string",
             minLength = 1,
             maxLength = 80 )
    @NotBlank
    @Size( max = 80 )
    String name,

    @Schema( description = "Number of airports in the region.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             type = "integer",
             minimum = "0"
    )
    @PositiveOrZero
    Long   airportCount
)
{}
