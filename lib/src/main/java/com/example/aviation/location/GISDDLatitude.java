package com.example.aviation.location;


import java.math.BigDecimal;
import java.util.regex.Matcher;

// import com.example.utility.DoNotMutate;
import com.example.utility.Range;
import org.jspecify.annotations.NonNull;

/**
 * Represents a latitude coordinate in Decimal Degrees (DD) format.
 */
public class GISDDLatitude extends GISLatitude
{
    private static final Range<BigDecimal> RANGE = new Range<>( BigDecimal.valueOf( -90 ),
                                                       BigDecimal.valueOf(  90 ) );

    public GISDDLatitude( @NonNull final String input )
    {
        this( parse( input ) );
    }




    protected GISDDLatitude( @NonNull final BigDecimal decimalDegrees )
    {
        super( decimalDegrees );

        if ( !RANGE.isInRange( decimalDegrees ) )
        {
            throw new IllegalArgumentException( String.format( "Invalid DD Latitude: '%s' is out of range [ %s, %s ]",
                                                               getDecimalDegrees(),
                                                               RANGE.getMin(),
                                                               RANGE.getMax() ) );
        }
    }


    static public GISDDLatitude of( @NonNull final BigDecimal value )
    {
        return new GISDDLatitude( value );
    }

    static public GISDDLatitude of( @NonNull final String value )
    {
        return new GISDDLatitude( value );
    }


    static protected @NonNull BigDecimal parse( @NonNull final String input )
    {
        // capture groups always exists in regex.  Contents of the capture group is optional, not the group.
        Matcher m = GISPatterns.DD_LAT_PATTERN.matcher( input );
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

                return "S".equals( cardinality) ? value.negate() : value;
            }
        }

        throw new IllegalArgumentException( String.format( "Invalid DD Latitude '%s'", input ) );
    }
}
