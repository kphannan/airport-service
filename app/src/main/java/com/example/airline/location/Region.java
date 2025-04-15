/* (C) 2025 */

package com.example.airline.location;


import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

// TODO change code to a 2 character code...



/**
 * Domain model object representing a single geographic Region.
 */
@Data
@AllArgsConstructor
public class Region
{
    /**
     * Internal OurAirports integer identifier for the region. This will stay
     * persistent, even if the region code changes.
     */
    @NonNull @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;

    /**
     * local_code prefixed with the country code to make a globally-unique
     * identifier.
     */
    @NonNull private String code;

    /**
     * The local code for the administrative subdivision. Whenever possible, these
     * are official ISO 3166:2, at the highest level available, but in some cases
     * OurAirports has to use unofficial codes. There is also a pseudo code "U-A"
     * for each country, which means that the airport has not yet been assigned to a
     * region (or perhaps can't be, as in the case of a deep-sea oil platform).
     */
    @NonNull private String localCode;

    /**
     * The common English-language name for the administrative subdivision. In some
     * cases, the name in local languages will appear in the keywords field assist
     * search.
     */
    @NonNull private String name;

    /**
     * The two-character ISO 3166:1-alpha2 code for the country containing the
     * administrative subdivision. A handful of unofficial, non-ISO codes are also
     * in use, such as "XK" for Kosovo.
     */
    @NonNull private String country; // ! Create domain object for the country code

    /**
     * A code for the continent to which the region belongs. See the continent field
     * in airports.csv for a list of codes.
     */
    @NonNull private String continent; // ! Create domain object for contient code

    /**
     * A link to the Wikipedia article describing the subdivision.
     */
    @Nullable private URI wikipediaLink;

    /**
     * A comma-separated list of keywords to assist with search. May include former
     * names for the region, and/or the region name in other languages.
     */
    @Nullable private String keywords;
}
