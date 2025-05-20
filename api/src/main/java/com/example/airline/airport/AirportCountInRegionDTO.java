package com.example.airline.airport;

//import jakarta.persistence.Column;
//import org.jspecify.annotations.NonNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class AirportCountInRegionDTO
{
    /**
     * 'An alphanumeric code for the high-level administrative subdivision of a
     * country where the airport is primarily located (e.g., province, governorate),
     * prefixed by the ISO2 country code and a hyphen. OurAirports uses ISO 3166:2
     * codes whenever possible, preferring higher administrative levels, but also
     * includes some custom codes. See the documentation for regions.csv.'
     */
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
//    @lombok.NonNull
    // TODO also need to support 'U-A' for unassigned
    @Pattern( regexp = "[A-Z]{2}-[A-Z\\-]{1,4}",
              message = "Region code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
    private String regionCode;
    private long   airportCount;
}
