package com.example.aviation.location;


import java.math.BigDecimal;
import java.util.regex.Matcher;

import com.example.utility.Range;
import org.jspecify.annotations.NonNull;


/**
 * Represents a longitude coordinate in Decimal Degrees (DD) format.
 *
 * <p>Example: 79.982° W
 * Precede West longitudes with a minus sign.
 * Longitudes range from -180 to 180
 */
public class GISDDLongitude extends GISLongitude
{
    private static final Range<BigDecimal> range = new Range<>( BigDecimal.valueOf( -180 ),
                                                                BigDecimal.valueOf(  180 ) );

    protected GISDDLongitude( @NonNull final BigDecimal decimalDegrees )
    {
        super( decimalDegrees );

        if ( !range.isInRange( decimalDegrees ) )
        {
            throw new IllegalArgumentException( String.format( "Invalid DD Longitude: '%s' is out of range [ %s, %s ]",
                                                               getDecimalDegrees(),
                                                               range.getMin(),
                                                               range.getMax() ) );
        }
    }

    public GISDDLongitude( @NonNull final String value )
    {
        this( parse( value ) );
    }



    static public GISDDLongitude of( @NonNull final BigDecimal value )
    {
        return new GISDDLongitude( value );
    }

    static public GISDDLongitude of( @NonNull final String value )
    {
        return new GISDDLongitude( value );
    }

    protected static @NonNull BigDecimal parse( @NonNull final String input )
    {
        final Matcher m = GISPatterns.DD_LONG_PATTERN.matcher( input );
        if ( m.find() )
        {
            // Don't accept both sign and cardinal direction letters
            final String cardinality = m.group( 3 ).toUpperCase();
            final String sign = m.group( 2 );
            if ( !(!sign.isBlank() && !cardinality.isBlank()) )
            {
                // remove degree symbol.
                final String numeric = m.group( 1 );
                                        // .replace( "°", "" );

                final BigDecimal value = new BigDecimal( numeric );

                return "W".equals( cardinality) ? value.negate() : value;
            }
        }

        throw new IllegalArgumentException( String.format( "Invalid DD Longitude '%s'", input ) );
    }

}
