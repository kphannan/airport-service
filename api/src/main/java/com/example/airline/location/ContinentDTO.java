/* (C) 2025 */

package com.example.airline.location;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
//import org.hibernate.validator.constraints.Length;    // TODO use Jakarta / Jackson validations
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
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
public class ContinentDTO //implements Serializable
{
    // TODO convert to a Java record
    @SuppressWarnings( "PMD.ShortVariable" )
    @JsonProperty( "id" )
    @Schema( name = "id",
             description = "Unique identifier",
             requiredMode = Schema.RequiredMode.REQUIRED )
    @Nullable private Integer id;
    @JsonProperty( "code" )
    @Schema( name = "code",
             description = "Unique abbreviation, which is a 2-character uppercase alphabetic code",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "AS" )
    @NonNull
    private           String  code;
    @JsonProperty( "name" )
    @Schema( name = "name",
             description = "Common use name",
             example = "Asia",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 52 )
    @NonNull private String  name;
    @JsonProperty( "wikilink" )
    @Schema( name = "wikiLink",
             description = "URI link to Wikipedia information",
             example = "https://en.wikipedia.org/wiki/Asia",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable private URI     wikiLink;
    @JsonProperty( "keywords" )
    @Schema( name = "keywords",
             description = "Optional additional search terms",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable private String  keywords; // May not need to exchange this
}
