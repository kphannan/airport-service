package com.example.airline.location;


import java.util.regex.Pattern;




/**
 * Regular expression patterns for location abbreviations.
 */
public final class LocationCodePatterns
{
    // ---------- Location Codes ----------
    // ----- Continent -----
    /**
     * Enumeration of the de facto continent 2 character abbreviations.
     */
    public static final String  CONTINENT_CODE         = "^(AF|AN|AS|EU|NA|OC|SA)$";
    public static final Pattern CONTINENT_CODE_PATTERN = Pattern.compile( CONTINENT_CODE );

    // ----- Country -----
    /**
     * {@linkurl https://en.wikipedia.org/wiki/ISO_3166-1 ISO 3166-1 alpha2}
     * code designating a country.
     */
    public static final String ISO_COUNTRY_CODE = "^[A-Z]{2}$";
    public static final Pattern ISO_COUNTRY_CODE_PATTERN = Pattern.compile( ISO_COUNTRY_CODE );

    // ----- Region -----
    /**
     * {@linkurl https://en.wikipedia.org/wiki/ISO_3166-2 ISO 3166-2}
     * country subdivision code.
     *
     * <p>Capture Groups:
     *     1. Country code
     *     2. Abbreviation of the country's administrative subdivision.
     */
    public static final String ISO_REGION_CODE = "^([A-Z]{2})-((?:U-A)|(?:[A-Z0-9]{1,3}))$";
    public static final Pattern ISO_REGION_CODE_PATTERN = Pattern.compile( ISO_REGION_CODE );

    /**
     * {@linkurl https://en.wikipedia.org/wiki/ISO_3166-2 ISO 3166-2}
     * country subdivision code.
     *
     * <p>Capture Groups:
     *     1. Abbreviation of the country's administrative subdivision.
     */
    public static final String ISO_REGION_SUB_CODE = "^((?:U-A)|(?:[A-Z0-9]{1,3}))$";
    public static final Pattern ISO_REGION_SUB_CODE_PATTERN = Pattern.compile( ISO_REGION_SUB_CODE );


    private LocationCodePatterns()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }



    // ---------- Validation Methods ----------

    /**
     * Validates a continent code.
     */
    public static boolean isValidContinentCode( final String code )
    {
        return code != null && CONTINENT_CODE_PATTERN.matcher( code ).matches();
    }

    /**
     * Validates an ISO 3166-1 alpha-2 country code.
     */
    public static boolean isValidCountryCode( final String code )
    {
        return code != null && ISO_COUNTRY_CODE_PATTERN.matcher( code ).matches();
    }

    /**
     * Validates an ISO 3166-2 region code.
     */
    public static boolean isValidRegionCode( final String code )
    {
        return code != null && ISO_REGION_CODE_PATTERN.matcher( code ).matches();
    }

    /**
     * Validates an ISO 3166-2 region subdivision code.
     */
    public static boolean isValidRegionSubCode( final String code )
    {
        return code != null && ISO_REGION_SUB_CODE_PATTERN.matcher( code ).matches();
    }

}
