/* (C) 2025 */

package com.example.airline.location.continent;

import java.net.URI;

import com.example.airline.location.LocationCodePatterns;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// import lombok.NonNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.validation.annotation.Validated;

// TODO change code to a 2 character code...
// record ContinentDTO( Long id, String code, String name, String wikiLink,
// String keywords )
// {}


/**
 * API representation of a Continent.
 */
@Schema( description = "Create a new continent record with with the specified values." )
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class NewContinentDTO
{
    // TODO convert to a Java record
    @JsonProperty( value = "code", required = true )
    @Schema( name = "code",
             description = "Unique abbreviation, which is a 2-character uppercase alphabetic code",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = LocationCodePatterns.CONTINENT_CODE,
             example = "NA" )
    @NonNull
    @NotNull( message =
                  """
                  Code must be one of these character sequences:
                  AF, AN, AS, EU, NA, OC, SA
                  """
    )
    @Pattern( regexp = LocationCodePatterns.CONTINENT_CODE,
              message =
                  """
                  Code must be one of these character sequences:
                  AF, AN, AS, EU, NA, OC, SA
                  """
    )
    private String code;

    @JsonProperty( value = "name", required = true )
    @Schema( name = "name",
             description = "Common use name",
             example = "North America",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 52 )
    @Pattern( regexp = "[a-zA-Z][a-zA-Z ]{1,51}", message = "Continent name must be between 2 and 52 characters" )
    @NonNull
    @NotNull( message = "Continent name must be between 2 to 52 characters" )
    private String name;

    @JsonProperty( "wikiLink" )
    @Schema( name = "wikiLink",
             description = "Wikipedia information",
             example = "https://en.wikipedia.org/wiki/North_America",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    private URI    wikiLink;

    @JsonProperty( "keywords" )
    @Schema( name = "keywords",
             description = "Optional additional search terms",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
    private String keywords; // May not need to exchange this
}
