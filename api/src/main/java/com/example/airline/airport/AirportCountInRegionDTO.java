package com.example.airline.airport;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;




public record AirportCountInRegionDTO(
        @JsonProperty( "isoRegion" )
        @Schema( name = "isoRegion",
                 description =
                         """
                            The two-character country code followed by an abbreviation
                            for the administrative subdivision (e.g., province, state)
                         """,
                 requiredMode = Schema.RequiredMode.REQUIRED,
                 minLength = 3,
                 maxLength = 7,
                 pattern = "[A-Z]{2}-[A-Z\\-]{1,4}",
                 example = "IE-D" )
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
