package com.example.airline.location;



/**
 * Regular expression patterns for location abbreviations.
 */
public final class LocationCodePatterns
{
    private LocationCodePatterns()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }

    // ---------- Location Codes ----------
    // ----- Continent -----
    /**
     * Enumeration of the de facto continent 2 character abbreviations.
     */
    public static final String CONTINENT_CODE = "AF|AN|AS|EU|NA|OC|SA";

    // ----- Country -----
    /**
     * {@linkurl https://en.wikipedia.org/wiki/ISO_3166-1 ISO 3166-1 alpha2}
     * code designating a country.
     *
     */
    public static final String ISO_COUNTRY_CODE = "[A-Z]{2}";

    // ----- Region -----
    /**
     * {@linkurl https://en.wikipedia.org/wiki/ISO_3166-2 ISO 3166-2}
     * country subdivision code.
     *
     * Capture Groups:
     *     1. Country code
     *     1. Abbreviation of the country's administrative subdivision.
     */
    public static final String ISO_REGION_CODE     = "([A-Z]{2})-((?:U-A)|(?:[A-Z0-9]{1,3}))";
    /**
     * {@linkurl https://en.wikipedia.org/wiki/ISO_3166-2 ISO 3166-2}
     * country subdivision code.
     *
     * Capture Groups:
     *     1. Abbreviation of the country's administrative subdivision.
     */
    public static final String ISO_REGION_SUB_CODE = "((?:U-A)|(?:[A-Z0-9]{1,3}))";


    // TODO may split this out to a GIS centric class.
    // ----- Latitude and Longitude -----
    /*
     * Latitude and longitude coordinates are primarily expressed in three formats:
     * Decimal Degrees (DD), Degrees, Minutes, and Seconds (DMS), and Decimal Minutes (DM).
     */

    /**
     * **Decimal Degrees (DD)**: The industry standard for digital systems and APIs.
     * Coordinates are single numbers separated by a comma, where positive values
     * indicate North/East and negative values indicate South/West.
     * - Format: Latitude, Longitude
     * - Example: 40.753, -73.983
     */
    public static final String LAT_LONG_DD = "^[+-]?(?:0|[1-9]{1,6})\\.(?:[0-9]{1,14})?$";

    /**
     *  Decimal Minutes (DM): An intermediate format where degrees are whole numbers
     *  and minutes contain the decimal fraction.
     *  - Format: DD° MM.M' N/S, DDD° MM.M' E/W
     *  - Example: 40°45.18'N, 73°58.98'W
     */

    public static final String LAT_LONG_DM = "";
    /**
     * Degrees, Minutes, Seconds (DMS): A traditional format using sexagesimal notation.
     * Latitude is listed first, followed by longitude.
     * - Format: DD° MM' SS" N/S, DDD° MM' SS" E/W
     * - Example: 40°45'11"N, 73°58'59"W
     */
    public static final String LAT_LONG_DMS = "";
}
