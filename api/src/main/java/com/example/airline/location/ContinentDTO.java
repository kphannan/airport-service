/* (C) 2025 */

package com.example.airline.location;

//import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.validation.constraints.NotNull;      // TODO Use JSpecification
//import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Schema( name = "id", description = "Primary key", requiredMode = Schema.RequiredMode.REQUIRED )
//    @NonNull
    private Integer id;
//    @JsonProperty( "code" )
//    @Size( 2 )
//    @NonNull
    @Schema( name = "code", description = "2-character alpha code", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 2, example = "NA")
    private String  code;
//    @JsonProperty( )
//    @Length( min = 0, max = 32 )
//    @NonNull
    @Schema( name = "name", description = "Common use name", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 52 )
    private String  name;
    @Schema( name = "wikiLink", description = "Wikipedia information", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 255)
//    @Nullable
    private URI     wikiLink;
    @Schema( name = "keywords", description = "Optional additional search terms", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 255)
//    @Nullable
    private String  keywords; // May not need to exchange this
}
