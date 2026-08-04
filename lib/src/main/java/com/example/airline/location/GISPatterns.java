package com.example.airline.location;


import java.util.regex.Pattern;

/**
 * Geographic coordinate pattern validators supporting three common formats:
 * <ul>
 *   <li>Decimal Degrees (DD)</li>
 *   <li>Decimal Minutes (DM)</li>
 *   <li>Degrees, Minutes, Seconds (DMS)</li>
 * </ul>
 * <p>Latitudes range from -90 to 90; Longitudes range from -180 to 180.
 * Up to 6 decimal places are supported for sub-degree precision.
 *
 * <p>
 * Decimal Degrees (DD)
 *   40.446° N         79.982° W
 *   Precede South latitudes and West longitudes with a minus sign.
 *   Latitudes range from -90 to 90
 *   Longitudes range from -180 to 180
 *   Up to 6 decimal places
 *   DD.latitude  - /^[\+-]?(([1-8]?\d)(\.\d{1,})?|90)\D*[NSns]?$/
 *   DD.longitude - /^[\+-]?((1[0-7]\d|[1-9]?\d)(\.\d{1,})?|180)\D*[EWew]?$/
 * <p>
 * Degrees Decimal Minutes (DDM)
 *   40° 26.767′ N    79° 58.933′ W
 *   90°  0′     N   180°  0′     W
 *   90°         N   180°         W
 *   Latitudes range from 0 to 90.
 *   Longitudes range from 0 to 180.
 *   Use N, S, E or W as either the last character,
 *     which represents a compass direction North, South, East or West.
 *   The last degree, minute, or second of a latitude or longitude
 *     may contain a decimal portion.
 *   DDM.latitude  - /^[\+-]?(([1-8]?\d)\D+[1-6]?\d(\.\d{1,})?|90(\D+0)?)\D+[NSns]?$/
 *   DDM.longitude - /^[\+-]?((1[0-7]\d|[1-9]?\d)\D+[1-6]?\d(\.\d{1,})?|180(\D+0)?)\D+[EWew]?$/
 * <p>Degrees Minutes Seconds (DMS)
 *   40° 26′ 46″ N 79° 58′ 56″ W
 *   40° 26′ 46″ S 79° 58′ 56″ E
 *   90° 0′ 0″ S 180° 0′ 0″ E
 *   40° 26′ 45.9996″ N   79° 58′ 55.2″ E
 *   Latitudes range from 0 to 90.
 *   Longitudes range from 0 to 180.
 *   Minutes & Seconds range from 0-60
 *   Use N, S, E or W as either the last character,
 *     which represents a compass direction North, South, East or West.
 *   D & M must be integers, S may be an integer or float.
 *   DMS.latitude  - /^[\+-]?(([1-8]?\d)\D+([1-5]?\d|60)\D+([1-5]?\d|60)(\.\d+)?|90\D+0\D+0)\D+[NSns]?$/
 *   DMS.longitude - /^[\+-]?([1-7]?\d{1,2}\D+([1-5]?\d|60)\D+([1-5]?\d|60)(\.\d+)?|180\D+0\D+0)\D+[EWew]?$/
 *
 */
public final class GISPatterns
{

    // ----- Decimal Degrees (DD) -----
    // Latitude: -90 to 90, optional sign, optional °, optional N/S
    public static final String  DD_LAT         = "^-?([1-8]?\\d(\\.\\d{1,6})?|90(\\.0{1,6})?)°?\\s*[NSns]?$";
    public static final Pattern DD_LAT_PATTERN = Pattern.compile( DD_LAT );
    // Longitude: -180 to 180, optional sign, optional °, optional E/W
    public static final String  DD_LONG         =
        "^-?(1[0-7]\\d(\\.\\d{1,6})?|180(\\.0{1,6})?|\\d{1,2}(\\.\\d{1,6})?)°?\\s*[EWew]?$";
    public static final Pattern DD_LONG_PATTERN = Pattern.compile( DD_LONG );
    // Combined lat,lon pair validation (comma-separated)
    public static final String  DD_LAT_LONG         = "^" + DD_LAT.replace( "^", "" ).replace( "$", "" )
                                                          + ",\\s*"
                                                          + DD_LONG.replace( "^", "" ).replace( "$", "" )
                                                          + "$";
    public static final Pattern DD_LAT_LONG_PATTERN = Pattern.compile( DD_LAT_LONG );


    // ----- Decimal Minutes (DDM) -----
    // Format: DD° MM.M' N/S, DDD° MM.M' E/W
    public static final String  DDM_LAT          = "^\\s*([1-8]?\\d|90)°\\s*([1-5]?\\d|60)\\.\\d+['′]\\s*[NS]\\s*$";
    public static final Pattern DDM_LAT_PATTERN  = Pattern.compile( DDM_LAT );
    public static final String  DDM_LONG         = "^\\s*[\\+-]?((?:1[0-7][0-9]|[1-9][0-9]{0,1}|180|0)°)\\s*(((?:[0-9]{1,2})(?:\\.[0-9]{1,3})?)['′])?\\s*([EWew]?)$";
    public static final Pattern DDM_LONG_PATTERN     = Pattern.compile( DDM_LONG );
    public static final String  DDM_LAT_LONG         = "^" + DDM_LAT.replace( "^", "" ).replace( "$", "" )
                                                           + ",\\s*"
                                                           + DDM_LONG.replace( "^", "" ).replace( "$", "" )
                                                           + "$";
    public static final Pattern DDM_LAT_LONG_PATTERN = Pattern.compile( DDM_LAT_LONG );


    // ----- Degrees, Minutes, Seconds (DMS) -----
    // Format: DD° MM' SS" N/S, DDD° MM' SS" E/W
    public static final String  DMS_LAT          = "^\\s*([1-8]?\\d|90)°\\s*([1-5]?\\d|60)['']\\s*([1-5]?\\d|60)\\.?\\d*\"?\\s*[NS]\\s*$";
    public static final Pattern DMS_LAT_PATTERN  = Pattern.compile( DMS_LAT );
    public static final String  DMS_LONG         = "^[\\+-]?([1-7]?\\d{1,2}°\s*([1-5]?\\d|60)['′]\s*([1-5]?\\d|60)(\\.\\d+)?|180[^0-9]+0[^0-9]+0)[″\"]?\s*[EWew]?$";
    public static final Pattern DMS_LONG_PATTERN = Pattern.compile( DMS_LONG );
    // Combined DMS lat,lon pair
    public static final String  DMS_LAT_LONG         = "^" + DMS_LAT.replace( "^", "" ).replace( "$", "" )
                                                          + ",\\s*"
                                                          + DMS_LONG.replace( "^", "" ).replace( "$", "" )
                                                          + "$";
    public static final Pattern DMS_LAT_LONG_PATTERN = Pattern.compile( DMS_LAT_LONG );



    private GISPatterns()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }

    // ---------- Validation Methods ----------

    /**
     * Validates a latitude/longitude coordinate pair in Decimal Degrees (DD) format. Expected format: "lat, lon" (e.g.,
     * "40.753, -73.983")
     */
    public static boolean isDDValidLatLong( final String coordinate )
    {
        return coordinate != null && DD_LAT_LONG_PATTERN.matcher( coordinate ).matches();
    }

    /**
     * Validates a single latitude value in Decimal Degrees (DD) format.
     */
    public static boolean isDDValidLatitude( final String latitude )
    {
        return latitude != null && DD_LAT_PATTERN.matcher( latitude ).matches();
    }

    /**
     * Validates a single longitude value in Decimal Degrees (DD) format.
     */
    public static boolean isDDValidLongitude( final String longitude )
    {
        return longitude != null && DD_LONG_PATTERN.matcher( longitude ).matches();
    }

    /**
     * Validates a latitude/longitude coordinate pair in Decimal Minutes (DM) format. Expected format: "DD° MM.M' N/S,
     * DDD° MM.M' E/W"
     */
    public static boolean isDDMValidLatLong( final String coordinate )
    {
        return coordinate != null && DDM_LAT_LONG_PATTERN.matcher( coordinate ).matches();
    }

    public static boolean isDDMValidLat( final String coordinate )
    {
        return coordinate != null && DDM_LAT_PATTERN.matcher( coordinate ).matches();
    }

    public static boolean isDDMValidLong( final String coordinate )
    {
        return coordinate != null && DDM_LONG_PATTERN.matcher( coordinate ).matches();
    }

    /**
     * Validates a latitude/longitude coordinate pair in Degrees, Minutes, Seconds (DMS) format. Expected format: "DD°
     * MM' SS" N/S, DDD° MM' SS" E/W"
     */
    public static boolean isDMSValidLatLong( final String coordinate )
    {
        return coordinate != null && DMS_LAT_LONG_PATTERN.matcher( coordinate ).matches();
    }

    public static boolean isDMSValidLat( final String coordinate )
    {
        return coordinate != null && DMS_LAT_PATTERN.matcher( coordinate ).matches();
    }

    public static boolean isDMSValidLong( final String coordinate )
    {
        return coordinate != null && DMS_LONG_PATTERN.matcher( coordinate ).matches();
    }

}
