/* (C) 2025 */

package com.example.airline.location;


import java.io.Serializable;
import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;

// TODO change code to a 2 character code...
// record CountryDTO( Long id, String code, String name, String wikiLink,
// String keywords )
// {}



/**
 * API representation of a Country.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor  // required for Jackson mapping
public class CountryDTO implements Serializable
{
    // TODO convert to a Java record

    @JsonProperty( "id" )
    @Schema( name = "id",
             description = "Unique identifier",
             requiredMode = Schema.RequiredMode.REQUIRED )
    @NotNull( message = "A country id is required" )
    @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;



    @JsonProperty( "code" )
    @Schema( name = "code",
             description = "The two-character ISO 3166:1-alpha2 code for the country.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "PH" )
    @NotBlank( message = "An ISO 3166:1-alpha2 country code is required" )
    @NonNull
    @Pattern( regexp = "[A-Z]{2}", message = "Code must a valid ISO 3166:1-alpha2" )
    private String code;

    @JsonProperty( "name" )
    @Schema( name = "name",
             description =
                """
                   The common English-language name for the country.
                   Other variations of the name may appear in the keywords field to assist with search.
                """,
             example = "North America",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 52 )
    @NotBlank( message = "Name is required" )
    @Size( min = 2, max = 52, message = "Country name must be between 2 and 52 characters" )
    @Pattern( regexp = "[a-zA-Z][a-zA-Z ]{1,51}", message = "Country name must be between 2 and 52 characters" )
    @NonNull
    private String name;

    @JsonProperty( "continent" )
    @Schema( name = "continent",
             description = "Unique abbreviation, of the continent where this country is located.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "AS" )
    @NotBlank( message = "A 2-character continent code is required" )
    @NonNull
    @Pattern( regexp = "[A-Z]{2}", message = "Continent code must be 2 uppercase characters" )
    private String continent;

    @JsonProperty( "wikiLink" )
    @Schema( name = "wikiLink",
             description = "Link to the Wikipedia article about the country",
             example = "https://en.wikipedia.org/wiki/Philippines",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    private URI wikipediaLink;

    @JsonProperty( "keywords" )
    @Schema( name = "keywords",
             description = "Optional additional search terms",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
    private String keywords;
}
