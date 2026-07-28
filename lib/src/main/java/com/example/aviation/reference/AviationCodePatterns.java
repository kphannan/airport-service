package com.example.aviation.reference;


// https://gist.github.com/eightyknots/4372d1166a192d5e9754

/**
 * Reference of regular expressions for aviation codes.
 */
public final class AviationCodePatterns
{
    private AviationCodePatterns()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }

    // ---------- Regex Pattern Strings ----------
    // ----- Airport Codes
    public static final String AIRPORT_IATA = "[A-Z]{3}";

    /**
     * ICAO airport codes consist of a four-letter alphanumeric sequence
     * (e.g., EGLL for London Heathrow) assigned by the International Civil Aviation
     * Organization to uniquely identify airports, heliports, and other aviation facilities.
     *
     * <p>The structure is hierarchical and regionally standardized:
     *
     * <p>First Letter: Indicates the general geographic region (e.g., K for the
     * contiguous United States, C for Canada, E for Northern Europe, L for Southern Europe).
     *
     * <p>ICAO codes do not start with the letters I, J, Q or X.
     *
     * <p>ZZZZ is also a pseudocode, used in flight plans for airodromes with no ICAO code assigned.
     *
     * <p>Second Letter: Identifies the specific country or territory within that region
     * (e.g., G for the United Kingdom, F for France, D for Germany).
     * Last Two Letters: Designate the specific airport or facility, assigned by
     * national authorities to avoid duplication.
     *
     * {@linkurl https://en.wikipedia.org/wiki/ICAO_airport_code Wikipedia}
     */
    public static final String AIRPORT_ICAO       = "[A-HK-PR-WY-Z][A-Z]{3}"; // "[A-Z]{4}";

    /**
     * Application representation of an airport identifier.  Predominantly the ICAO code.
     * When that is not unique it is a combination of the country code a dash and an
     * alphanumeric or numeric value.
     */
    public static final String AIRPORT_IDENTIFIER = "(([0-9]{1,2}[A-Z]{1,2}[0-9]?)|([A-Z]{3,4}[0-9]?)|([A-Z]{1,2}-([0-9]{2,5}|[A-Z]{3,4}))|([A-Z]{1,2}[0-9]{1,2}[A-Z]{1,2}?[0-9]?)|([A-Z]{1,2}[0-9]{1,2})|([A-Z]{1,2}-(([0-9]{1,2}[A-Z]{1,2}[0-9]?)|([A-Z]{2,3}[0-9]{1,2}))))";

    // ----- Aircraft
    /**
     * National registration.
     */
    public static final String aircraftRegistration = "[A-Z]-[A-Z]{4}|[A-Z]{2}-[A-Z]{3}|N[0-9]{1,5}[A-Z]{0,2}";
    /**
     * IATA aircraft type.
     */
    public static final String aircraftTypeIATA     = "[A-Z0-9]{3}";
    /**
     * ICAO aircraft type.
     */
    public static final String aircraftTypeICAO     = "[A-Z]{1}[A-Z0-9]{1,3}";

    // ----- Airline

    /**
     * The standard regex for an IATA airline code (a 2-character commercial service mark).
     *
     * This pattern enforces that the first two characters contain **at least one alphabetic
     * letter**, rejecting pure numeric combinations like "11" while accepting formats like
     * **BA**, **X3**, or **9F**.
     */
    public static final String AIRLINE_IATA = "[A-Z]{3}";

    /**
     * ICAO airline code.
     */
    public static final String AIRLINE_ICAO = "[A-Z]{3}";

    // ---------- Regex Pattern Matcher ----------
}
