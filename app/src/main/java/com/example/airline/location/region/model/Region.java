/* (C) 2025 */

package com.example.airline.location.region.model;


import java.net.URI;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

// TODO change code to a 2 character code...

// Dataset download https://www.ip2location.com/free/iso3166-2
// https://pypi.org/project/iso3166-2/
// https://github.com/stefangabos/world_countries/blob/master/data/subdivisions/subdivisions.csv
// https://www.geonames.org/countries/
// https://www.statoids.com
// https://unstats.un.org/unsd/methodology/m49/overview/


/**
 * High-level administrative subdivision of a country (e.g. province, governorate, state).
 *
 * @param id            primary key assigned by the database.
 * @param code          local_code prefixed with the country code to make a globally unique identifier.
 * @param localCode     The local code for the administrative subdivision. Whenever possible, these are official ISO
 *                      3166:2, at the highest level available, but in some cases unofficial codes are used. There is
 *                      also a pseudocode "U-A" for each country, which means that the airport has not yet been assigned
 *                      to a region (or perhaps can't be, as in the case of a deep-sea oil platform).
 * @param name          The common English-language name for the administrative subdivision. In some cases, the name in
 *                      local languages will appear in the keyword field assist search.
 * @param country       The two-character ISO 3166:1-alpha2 code for the country containing the administrative
 *                      subdivision. A handful of unofficial, non-ISO codes are also in use, such as "XK" for Kosovo.
 * @param continent     A code for the continent to which the region belongs. See the continent field in airports.csv
 *                      for a list of codes.
 * @param wikipediaLink Optional, link to the Wikipedia article describing the subdivision.
 * @param keywords      Optional, A comma-separated list of keywords to help with search. May include former names for
 *                      the region, and/or the region name in other languages.
 */
public record Region(

    @NonNull
    @SuppressWarnings( "PMD.ShortVariable" )
    Integer id,

    @Pattern( regexp = "[A-Z]{2}-[A-Z\\-]{1,4}",
              message = "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code"
    )
    @NonNull
    String code,

    @Pattern( regexp = "([A-Z]{2}-[A-Z\\-]{1,4}|U-A)",
              message = "Code must a valid ISO 3166:1-alpha2 followed by '-' and a local code"
    )
    @NonNull
    String localCode,

    @Pattern( regexp = "[a-zA-Z][a-zA-Z ]{1,51}", message = "Continent name must be 2 to 52 characters" )
    @NonNull
    String name,

    @Pattern( regexp = "[A-Z]{2}", message = "Country code must a valid ISO 3166:1-alpha2" )
    @NonNull
    String country, // ! Create a domain object for the country code

    @Pattern( regexp = "[A-Z]{2}", message = "Continent code must be 2 uppercase characters" )
    @NonNull
    String continent, // ! Create a domain object for continent code

    @Nullable
    URI wikipediaLink,

    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
    @Nullable
    String keywords
)
{
}
