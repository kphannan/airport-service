/* (C) 2025 */

package com.example.airline.location.region;


import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
//import org.jspecify.annotations.NonNull;
//import org.jspecify.annotations.Nullable;

// TODO change code to a 2 character code...
// record CountryDTO( Long id, String code, String name, String wikiLink,
// String keywords )
// {}



/**
 * API representation of a Region.
 */
@Data
@AllArgsConstructor
public class RegionDTO
{
    // TODO convert to a Java record
    /**
     * Internal OurAirports integer identifier for the region. This will stay
     * persistent, even if the region code changes.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    @JsonProperty( "id" )
    @Schema( name = "id",
             description = "Unique identifier",
             requiredMode = Schema.RequiredMode.REQUIRED )
    @NotNull( message = "A Region id is required" )
    private Integer id;

    /**
     * local_code prefixed with the country code to make a globally unique
     * identifier.
     */
    @JsonProperty( "code" )
    @Schema( name = "code",
             description =
                     """
                        The two-character ISO 3166:1-alpha2 code for the country,
                        followed by a local code.
                     """,
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 3,
             maxLength = 7,
             pattern = "[A-Z]{2}-[A-Z\\-]{1,4}",
             example = "IE-D" )
    @NotBlank( message = "A unique region code is required" )
    @NonNull
    // TODO also need to support 'U-A' for unassigned
    @Pattern( regexp = "[A-Z]{2}-[A-Z\\-]{1,4}",
              message = "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
    private String code;

    /**
     * The local code for the administrative subdivision. Whenever possible, these
     * are official ISO 3166:2, at the highest level available, but in some cases
     * OurAirports has to use unofficial codes. There is also a pseudocode "U-A"
     * for each country, which means that the airport has not yet been assigned to a
     * region (or perhaps can't be, as in the case of a deep-sea oil platform).
     */
    @JsonProperty( "localCode" )
    @Schema( name = "localCode",
             description =
                     """
                        local code.
                     """,
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 3,
             maxLength = 7,
             pattern = "[A-Z]{2}-[A-Z\\-]{1,4}",
             example = "IE-D" )
    @NotBlank( message = "A unique local code is required" )
    @NonNull
    // TODO also need to support 'U-A' for unassigned
    @Pattern( regexp = "[A-Z0-9]{1,2}[A-Z\\-]{0,5}",
              message = "local code" )
    private String localCode;

    /**
     * The common English-language name for the administrative subdivision. In some
     * cases, the name in local languages will appear in the 'keywords' field assist
     * search.
     */
    @JsonProperty( "name" )
    @Schema( name = "name",
             description =
                     """
                        The common English-language name for the region.
                        Other variations of the name may appear in the keywords field to assist with search.
                     """,
             example = "North America",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 7,
             maxLength = 80 )
    @NotBlank( message = "Region name is required" )
    @Size( min = 2, max = 52, message = "Region name must be between 7 and 80 characters" )
    @Pattern( regexp = "[a-zA-Z][a-zA-Z ]{1,51}", message = "Region name must be between 7 and 80 characters" )
    @NonNull
    private String name;

    /**
     * The two-character ISO 3166:1-alpha2 code for the country containing the
     * administrative subdivision. A handful of unofficial, non-ISO codes are also
     * in use, such as "XK" for Kosovo.
     */
    @JsonProperty( "country" )
    @Schema( name = "country",
             description = "The two-character ISO 3166:1-alpha2 code for the country.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "IE" )
    @NotBlank( message = "An ISO 3166:1-alpha2 country code is required" )
    @NonNull
    @Pattern( regexp = "[A-Z]{2}", message = "Code must a valid ISO 3166:1-alpha2" )
    private String country; // ! Create domain-object for the country code

    /**
     * A code for the continent to which the region belongs. See the continent field
     * in airports.csv for a list of codes.
     */
    @JsonProperty( "continent" )
    @Schema( name = "continent",
             description = "Unique abbreviation, of the continent where this country is located.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             minLength = 2,
             maxLength = 2,
             pattern = "[A-Z]{2}",
             example = "EU" )
    @NotBlank( message = "A 2-character continent code is required" )
    @NonNull
    @Pattern( regexp = "[A-Z]{2}", message = "Continent code must be 2 uppercase characters" )
    private String continent; // ! Create domain-object for continent code

    /**
     * A link to the Wikipedia article describing the subdivision.
     */
    @JsonProperty( "wikiLink" )
    @Schema( name = "wikiLink",
             description = "Link to the Wikipedia article about the country",
             example = "https://en.wikipedia.org/wiki/County_Dublin",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    private URI wikipediaLink;

    /**
     * A comma-separated list of keywords for helping with search. May include former
     * names for the region, and/or the region name in other languages.
     */
    @JsonProperty( "keywords" )
    @Schema( name = "keywords",
             description = "Optional additional search terms",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
    private String keywords;
}
