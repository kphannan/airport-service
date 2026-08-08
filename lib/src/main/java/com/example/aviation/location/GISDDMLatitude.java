package com.example.aviation.location;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;

import com.example.utility.Range;
import org.jspecify.annotations.NonNull;

/**
 * Represents a latitude in Degrees Decimal Minutes (DDM) format.
 *
 * <p>Example: 40° 45.18' N
 *
 * <p>Conversion formula: {@code Decimal Degrees = Degrees + (Minutes / 60)}
 *
 * 40.446° N
 * Precede South latitudes with a minus sign.
 * Latitudes range from -90 to 90
 */
public class GISDDMLatitude extends GISLatitude
{
    private static final Range<BigDecimal> range = new Range<>( BigDecimal.valueOf( -90 ),
                                                                BigDecimal.valueOf(  90 ) );
    /**
     * Constructs a DDM latitude from a formatted string.
     *
     * @param value The coordinate string (e.g., "40° 45.18' N").
     * @throws IllegalArgumentException if the input does not match {@link GISPatterns#isDDMValidLat}.
     */
    public GISDDMLatitude( @NonNull final String value )
    {
        this( parse( value ) );
    }

    protected GISDDMLatitude( @NonNull final BigDecimal decimalDegrees )
    {
        super( decimalDegrees );

        if ( !range.isInRange( getDecimalDegrees() ) )
        {
            throw new IllegalArgumentException( String.format( "Invalid DDM Latitude: '%s' is out of range [ %s, %s ]",
                                                               getDecimalDegrees(),
                                                               range.getMin(),
                                                               range.getMax() ) );
        }
    }

    static public GISDDMLatitude of( @NonNull final BigDecimal value )
    {
        return new GISDDMLatitude( value );
    }

    static public GISDDMLatitude of( @NonNull final String value )
    {
        return new GISDDMLatitude( value );
    }

    /**
     * Parses a DDM string into a {@link BigDecimal} decimal degree value.
     *
     * @param input The formatted DDM string.
     * @return The calculated decimal degrees.
     * @throws IllegalArgumentException if parsing fails or input is invalid.
     */
    static protected @NonNull BigDecimal parse( @NonNull final String input )
    {
        Matcher m = GISPatterns.DDM_LAT_PATTERN.matcher(input);
        if (m.find()) {
            BigDecimal deg = new BigDecimal(m.group(1)); // Degrees
            BigDecimal min = new BigDecimal(m.group(2)); // Decimal Minutes

            BigDecimal result = deg.add(min.divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP));

            if ( "S".equalsIgnoreCase(m.group( 3 ) ) )
            { // Hemisphere
                result = result.negate();
            }

            return result;
        }
        else
        {
            throw new IllegalArgumentException( String.format( "Invalid DDM Latitude '%s'", input ) );
        }
    }
}
