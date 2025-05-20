package com.example.airline.airport;

//import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
//import org.jspecify.annotations.NonNull;


@Data
@AllArgsConstructor
@Builder
public class AirportCountInContinentDTO
{
    /**
     * The code for the continent where the airport is (primarily) located. Allowed
     * values are "AF" (Africa), "AN" (Antarctica), "AS" (Asia), "EU" (Europe), "NA"
     * (North America), "OC" (Oceania), or "SA" (South America).
     */
    @JsonProperty( "continent" )
    @Schema( name = "continent",
             description = "Unique abbreviation, of the continent where this country is located.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "AS" )
    @NotBlank( message = "A 2-character continent code is required" )
    @Pattern( regexp = "[A-Z]{2}", message = "Continent code must be 2 uppercase characters" )
    @NotBlank
    @NotNull
    private String continentCode;
    private String name;
    private long   airportCount;
}

