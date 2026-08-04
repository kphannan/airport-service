package com.example.aviation.reference;

import java.util.regex.Pattern;

/**
 * Reference of regular expressions for aviation codes.
 *
 * <p>This utility class provides pre-compiled patterns and validation methods
 * for common aviation identifiers including IATA/ICAO airport codes,
 * aircraft registrations, and airline codes.
 *
 * @see <a href="https://en.wikipedia.org/wiki/ICAO_airport_code">ICAO Airport Code</a>
 */
public final class AviationCodePatterns
{
    // ========== Airport Codes ==========

    /**
     * IATA airport code: exactly 3 uppercase letters (e.g., "JFK", "LHR").
     */
    public static final String  AIRPORT_IATA          = "^[A-Z]{3}$";
    public static final Pattern AIRPORT_IATA_PATTERN  = Pattern.compile( AIRPORT_IATA );

    /**
     * ICAO airport codes consist of a four-letter alphanumeric sequence
     * (e.g., EGLL for London Heathrow) assigned by the International Civil Aviation
     * Organization to uniquely identify airports, heliports, and other aviation facilities.
     *
     * <p>The structure is hierarchical and regionally standardized:
     * <ul>
     *   <li>First Letter: Indicates the general geographic region (e.g., K for the contiguous
     *       United States, C for Canada, E for Northern Europe, L for Southern Europe).</li>
     *   <li>ICAO codes do not start with the letters I, J, Q or X.</li>
     *   <li>Second Letter: Identifies the specific country or territory within that region.</li>
     *   <li>Last Two Letters: Designate the specific airport or facility.</li>
     * </ul>
     *
     * <p>Note: ZZZZ is a pseudocode used in flight plans for aerodromes with no ICAO code assigned.
     *
     * @see <a href="https://en.wikipedia.org/wiki/ICAO_airport_code">ICAO Airport Code</a>
     */
    public static final String  AIRPORT_ICAO          = "^[A-HK-PR-WY-Z][A-Z]{3}$";
    public static final Pattern AIRPORT_ICAO_PATTERN  = Pattern.compile( AIRPORT_ICAO );

    /**
     * Application representation of an airport identifier. Predominantly the ICAO code.
     * When that is not unique, it is a combination of the country code, a dash, and an
     * alphanumeric or numeric value.
     *
     * <p>Pattern breakdown:
     * <ul>
     *   <li>1-2 digits, optional 1-2 letters, optional digit</li>
     *   <li>3-4 letters, optional digit</li>
     *   <li>1-2 letters, dash, 2-5 digits OR 3-4 letters</li>
     *   <li>1-2 letters, 1-2 digits, optional 1-2 letters, optional digit</li>
     *   <li>1-2 letters, 1-2 digits</li>
     *   <li>1-2 letters, dash, (1-2 digits+letters OR 2-3 letters+digits)</li>
     * </ul>
     */
    public static final String  AIRPORT_IDENTIFIER    = "^(([0-9]{1,2}[A-Z]{1,2}[0-9]?)|([A-Z]{3,4}[0-9]?)|([A-Z]{1,2}-([0-9]{2,5}|[A-Z]{3,4}))|([A-Z]{1,2}[0-9]{1,2}[A-Z]{1,2}?[0-9]?)|([A-Z]{1,2}[0-9]{1,2})|([A-Z]{1,2}-(([0-9]{1,2}[A-Z]{1,2}[0-9]?)|([A-Z]{2,3}[0-9]{1,2}))))$";
    public static final Pattern AIRPORT_IDENTIFIER_PATTERN = Pattern.compile( AIRPORT_IDENTIFIER );

    // ========== Aircraft ==========

    /**
     * National aircraft registration pattern.
     * Supports formats like: G-ABCD, F-ABC, N12345A
     */
    // public static final String  AIRCRAFT_REGISTRATION = "[A-Z]-[A-Z]{4}|[A-Z]{2}-[A-Z]{3}|N[0-9]{1,5}[A-Z]{0,2}|N[1-9][0-9]{0,2}[]A-Z]";
    public static final String  AIRCRAFT_REGISTRATION = "^(?:[A-Z]-[A-Z]{4}|[A-Z]{2}-[A-Z]{3}|N[1-9](([0-9]{0,4})|([0-9]{0,3}[A-HJ-NP-Z])|([0-9]{0,2}[A-HJ-NP-Z]{2})))$";
    public static final Pattern AIRCRAFT_REGISTRATION_PATTERN = Pattern.compile( AIRCRAFT_REGISTRATION );

    /**
     * IATA aircraft type code: 3 alphanumeric characters (e.g., "B73", "A32").
     */
    public static final String  AIRCRAFT_TYPE_IATA    = "^[A-Z0-9]{3}$";
    public static final Pattern AIRCRAFT_TYPE_IATA_PATTERN = Pattern.compile( AIRCRAFT_TYPE_IATA );

    /**
     * ICAO aircraft type code: 1 letter followed by 1-3 alphanumeric characters (e.g., "B738", "A320").
     */
    public static final String  AIRCRAFT_TYPE_ICAO    = "^[A-Z][A-Z0-9]{1,3}$";
    public static final Pattern AIRCRAFT_TYPE_ICAO_PATTERN = Pattern.compile( AIRCRAFT_TYPE_ICAO );

    // ========== Airlines ==========

    /**
     * IATA airline code: exactly 2 characters containing at least one alphabetic letter.
     * Accepts formats like "BA", "X3", "9F" but rejects pure numeric combinations like "11".
     */
    public static final String  AIRLINE_DESIGNATOR_IATA         = "^([A-Z][A-Z0-9]|[A-Z0-9][A-Z])$";
    public static final Pattern AIRLINE_DESIGNATOR_IATA_PATTERN = Pattern.compile( AIRLINE_DESIGNATOR_IATA );

    /**
     * ICAO airline code: exactly 3 uppercase letters (e.g., "BAW", "AAL").
     */
    public static final String  AIRLINE_DESIGNATOR_ICAO         = "^[A-Z]{3}$";
    public static final Pattern AIRLINE_DESIGNATOR_ICAO_PATTERN = Pattern.compile( AIRLINE_DESIGNATOR_ICAO );


    private AviationCodePatterns()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }



    // ========== Validation Methods ==========

    /**
     * Validates whether the given string is a valid IATA airport code.
     *
     * @param code the code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIataAirportCode( final String code )
    {
        return code != null && AIRPORT_IATA_PATTERN.matcher( code ).matches();
    }

    /**
     * Validates whether the given string is a valid ICAO airport code.
     *
     * @param code the code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIcaoAirportCode( final String code )
    {
        return code != null && AIRPORT_ICAO_PATTERN.matcher( code ).matches();
    }

    /**
     * Validates whether the given string is a valid ICAO or IATA airport code.
     *
     * @param code the code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAirportCode( final String code )
    {
        return isValidIataAirportCode( code ) || isValidIcaoAirportCode( code );
    }

    /**
     * Validates whether the given string is a valid airline IATA designator.
     *
     * @param designator the designator to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIataAirlineDesignator( final String designator )
    {
        return designator != null && AIRLINE_DESIGNATOR_IATA_PATTERN.matcher( designator ).matches();
    }

    /**
     * Validates whether the given string is a valid airline ICAO designator.
     *
     * @param designator the designator to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIcaoAirlineDesignator( final String designator )
    {
        return designator != null && AIRLINE_DESIGNATOR_ICAO_PATTERN.matcher( designator ).matches();
    }

    /**
     * Validates whether the given string is a valid ICAO or IATA airline designator.
     *
     * @param designator the designator to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAirlineDesignator( final String designator )
    {
        return  isValidIataAirlineDesignator( designator ) || isValidIcaoAirlineDesignator( designator );
    }

    /**
     * Validates whether the given string is a valid aircraft registration.
     *
     * @param registration the registration to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAircraftRegistration( final String registration )
    {
        return registration != null && AIRCRAFT_REGISTRATION_PATTERN.matcher( registration ).matches();
    }
}
