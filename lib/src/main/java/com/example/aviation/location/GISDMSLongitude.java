package com.example.aviation.location;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;

import com.example.utility.Range;
import org.jspecify.annotations.NonNull;

public class GISDMSLongitude extends GISLongitude
{
    private static final Range<BigDecimal> range = new Range<>( BigDecimal.valueOf( -180 ),
                                                                BigDecimal.valueOf(  180 ) );



    public GISDMSLongitude( @NonNull final String value )
    {
        this( parse( value ) );
    }


    static public GISDMSLongitude of( @NonNull final BigDecimal value )
    {
        return new GISDMSLongitude( value );
    }

    static public GISDMSLongitude of( @NonNull final String value )
    {
        return new GISDMSLongitude( value );
    }





    protected GISDMSLongitude( @NonNull final BigDecimal decimalDegrees )
    {
        super( decimalDegrees );

        if ( !range.isInRange( decimalDegrees ) )
        {
            throw new IllegalArgumentException( String.format( "Invalid DMS Longitude: '%s' is out of range [ %s, %s ]",
                                                               getDecimalDegrees(),
                                                               range.getMin(),
                                                               range.getMax() ) );
        }
    }

    static protected @NonNull BigDecimal parse( @NonNull final String input )
    {
        Matcher m = GISPatterns.DMS_LONG_PATTERN.matcher( input );
        if ( m.find() )
        {
            BigDecimal deg = new BigDecimal( m.group( 1 ) );
            BigDecimal min = new BigDecimal( m.group( 2 ) );
            BigDecimal sec = new BigDecimal( m.group( 3 ) );
            BigDecimal result = deg.add( min.divide( BigDecimal.valueOf( 60 ), 10, RoundingMode.HALF_UP ) )
                                   .add( sec.divide( BigDecimal.valueOf( 3600 ), 10, RoundingMode.HALF_UP ) );
            if ( "W".equalsIgnoreCase( m.group( 4 ) ) )
            {
                result = result.negate();
            }

            return result;
        }
        else
        {
            throw new IllegalArgumentException( String.format( "Invalid DMS Longitude: '%s' is out of range [ %s, %s ]",
                                                               input,
                                                               range.getMin(),
                                                               range.getMax()
                                                             ) );
        }

    }


}
