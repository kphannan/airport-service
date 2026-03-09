/* (C) 2025 */

package com.example.airline.location.continent;

import java.net.URI;

import com.example.utility.IgnoreGeneratedCoverage;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.validation.annotation.Validated;

// TODO change code to a 2 character code...
// record ContinentDTO( Long id, String code, String name, String wikiLink,
// String keywords )
// {}



/**
 * API representation of a Continent.
 */
@IgnoreGeneratedCoverage
@Validated
public record ContinentDTO(
    @SuppressWarnings( "PMD.ShortVariable" )
    @JsonProperty( "id" )
    @Schema( name = "id",
             description = "Unique identifier",
             requiredMode = Schema.RequiredMode.REQUIRED )
    @NotNull( message = "A continent id is required" )
    Integer id,

    @JsonProperty( value = "code", required = true )
    @Schema( name = "code",
             description = "Unique abbreviation, which is a 2-character uppercase alphabetic code\"",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "NA" )
    @NotNull( message = "A continent code is required" )
    @Pattern( regexp = "[A-Z]{2}", message = "Code must be 2 uppercase characters" )
    @NonNull
    String code,

    @JsonProperty( value = "name", required = true )
    @Schema( name = "name",
             description = "Common use name",
             example = "North America",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 52 )
    @Pattern( regexp = "[a-zA-Z][a-zA-Z ]{1,51}", message = "Continent name must be 2 to 52 characters" )
    @NonNull
    String name,

    @JsonProperty( "wikiLink" )
    @Schema( name = "wikiLink",
             description = "Wikipedia information",
             example = "https://en.wikipedia.org/wiki/North_America",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    URI    wikiLink,

    @JsonProperty( "keywords" )
    @Schema( name = "keywords",
             description = "Optional additional search terms",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
    String keywords // May not need to exchange this
){}
