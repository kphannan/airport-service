/* (C) 2025 */

package com.example.airline.location;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

// TODO change code to a 2 character code...
// record ContinentDTO( Long id, String code, String name, String wikiLink,
// String keywords )
// {}


/**
 * API representation of a Continent.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewContinentDTO //implements Serializable
{
    // TODO convert to a Java record
    @JsonProperty( "code" )
    @Schema( name = "code",
             description = "Unique abbreviation, which is a 2-character uppercase alphabetic code\"",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "NA" )
    @NotBlank( message = "A 2-character code is required" )
//    @NotNull
    @Pattern( regexp = "[A-Z]{2}", message = "Code must be 2 uppercase characters" )
    private String code;

    @JsonProperty( "name" )
    @Schema( name = "name",
             description = "Common use name",
             example = "North America",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 52 )
    @NotBlank( message = "Name is required" )
    @Size( min = 2, max = 52, message = "Name must be between 2 and 52 characters" )
//    @NotNull
    private String name;

    @JsonProperty( "wikiLink" )
    @Schema( name = "wikiLink",
             description = "Wikipedia information",
             example = "https://en.wikipedia.org/wiki/North_America",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
//    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
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
