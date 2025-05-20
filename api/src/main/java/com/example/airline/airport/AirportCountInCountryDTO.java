package com.example.airline.airport;

//import org.jspecify.annotations.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class AirportCountInCountryDTO

{
    /**
     * The two-character ISO 3166:1-alpha2 code for the country where the airport is
     * (primarily) located. A handful of unofficial, non-ISO codes are also in use,
     * such as "XK" for Kosovo. Points to the code column in countries.csv.
     */
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
    private String countryCode;
    private long   airportCount;
}
