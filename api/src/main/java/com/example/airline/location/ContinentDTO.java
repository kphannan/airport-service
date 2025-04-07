/* (C) 2025 */

package com.example.airline.location;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
//import org.hibernate.validator.constraints.Length;    // TODO use Jakarta / Jackson valiations
import io.swagger.v3.oas.annotations.media.Schema;
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
public class ContinentDTO //implements Serializable
{
    // TODO convert to a Java record
    @SuppressWarnings( "PMD.ShortVariable" )
    @JsonProperty( "id" )
    @Schema( name = "id", description = "Primary key", requiredMode = Schema.RequiredMode.REQUIRED )
    @Nullable private Integer id;
   @JsonProperty( "code" )
//    @Size( 2 )
    @Schema( name = "code", description = "2-character alpha code", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 2, example = "NA")
    @Nullable private String  code;
    @JsonProperty( "name" )
    //    @Length( min = 0, max = 32 )
    @Schema( name = "name", description = "Common use name", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 52 )
    @Nullable private String  name;
    @JsonProperty( "wikilink" )
    @Schema( name = "wikiLink", description = "Wikipedia information", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 255)
    @Nullable private URI     wikiLink;
    @JsonProperty( "keywords" )
    @Schema( name = "keywords", description = "Optional additional search terms", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 255)
    @Nullable private String  keywords; // May not need to exchange this
}
