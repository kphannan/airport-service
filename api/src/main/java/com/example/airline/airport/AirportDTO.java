/* (C) 2025 */

package com.example.airline.airport;


import java.math.BigDecimal;
import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


/**
 * API representation of an Airport.
 */
@Data
@AllArgsConstructor
public class AirportDTO
{
    /**
     * T Internal OurAirports integer identifier for the airport. This will stay
     * persistent, even if the airport code changes.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    @NonNull private Long id;

    /**
     * The text identifier used in the OurAirports URL. This will be the ICAO code
     * if available. Otherwise, it will be a local airport code (if no conflict), or
     * if nothing else is available, an internally generated code starting with the
     * ISO2 country code, followed by a dash and a four-digit number.
     */
    @NonNull private String ident;

    /**
     * The type of the airport. Allowed values are "closed_airport", "heliport",
     * "large_airport", "medium_airport", "seaplane_base", and "small_airport". See
     * the map legend for a definition of each type.
     */
    @NonNull private String type;

    /**
     * The official airport name, including "Airport", "Airstrip", etc.
     */
    @NonNull private String name;

    /**
     * The airport latitude in decimal degrees (positive for north).
     */
    @NonNull private BigDecimal latitude;

    /**
     * The airport longitude in decimal degrees (positive for east).
     */
    @NonNull private BigDecimal longitude;

    /**
     * The airport elevation MSL in feet (not metres).
     */
    @Nullable private Integer elevation;

    /**
     * The code for the continent where the airport is (primarily) located. Allowed
     * values are "AF" (Africa), "AN" (Antarctica), "AS" (Asia), "EU" (Europe), "NA"
     * (North America), "OC" (Oceania), or "SA" (South America).
     */
    @NonNull private String continent;

    /**
     * The two-character ISO 3166:1-alpha2 code for the country where the airport is
     * (primarily) located. A handful of unofficial, non-ISO codes are also in use,
     * such as "XK" for Kosovo. Points to the code column in countries.csv.
     */
    @NonNull private String isoCountry;

    /**
     * 'An alphanumeric code for the high-level administrative subdivision of a
     * country where the airport is primarily located (e.g., province, governorate),
     * prefixed by the ISO2 country code and a hyphen. OurAirports uses ISO 3166:2
     * codes whenever possible, preferring higher administrative levels, but also
     * includes some custom codes. See the documentation for regions.csv.'
     */
    @NonNull private String isoRegion;

    /**
     * The primary municipality that the airport serves (when available). Note that
     * this is not necessarily the municipality where the airport is physically
     * located.
     */
    @NonNull private String municipality;

    /**
     * "yes" if the airport currently has scheduled airline service, "no" otherwise.
     */
    @NonNull private String scheduledService; // boolean...

    /**
     * The code that an aviation GPS database (such as Jeppesen's or Garmin's) would
     * normally use for the airport. This will always be the ICAO code if one
     * exists. Note that, unlike the ident column, this is not guaranteed to be
     * globally unique.
     */
    @Nullable private String gpsCode;

    /**
     * The three-letter ICAO code for the airport (if it has one).
     */
    @Nullable private String icaoCode;

    /**
     * The three-letter IATA code for the airport (if it has one).
     */
    @Nullable private String iataCode;

    /**
     * The local country code for the airport, if different from the gps_code and
     * iata_code fields (used mainly for US airports).
     */
    @Nullable private String localCode;

    /**
     * URL of the airport''s official home page on the web, if one exists.
     */
    @Nullable private String homeLink; // URI

    /**
     * URL of the airport''s page on Wikipedia, if one exists.
     */
    @Nullable private URI wikipediaLink; // URI

    /**
     * Extra keywords/phrases for search, comma-separated. May include
     * former names for the airport, alternate codes, names in other languages,
     * nearby tourist destinations, etc.
     */
    @Nullable private String keywords;
}
