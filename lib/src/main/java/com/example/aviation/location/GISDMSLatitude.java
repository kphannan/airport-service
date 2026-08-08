package com.example.aviation.location;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;

import com.example.utility.Range;
import org.jspecify.annotations.NonNull;

public class GISDMSLatitude extends GISLatitude
{
    private static final Range<BigDecimal> range = new Range<>( BigDecimal.valueOf( -90 ),
                                                                BigDecimal.valueOf(  90 ) );

    public GISDMSLatitude( @NonNull final String value )
    {
        this( parse( value ) );
    }


    static public GISDMSLatitude of( @NonNull final BigDecimal value )
    {
        return new GISDMSLatitude( value );
    }

    static public GISDMSLatitude of( @NonNull final String value )
    {
        return new GISDMSLatitude( value );
    }




    protected GISDMSLatitude( @NonNull final BigDecimal decimalDegrees )
    {
        super( decimalDegrees );

        if ( !range.isInRange( decimalDegrees ) )
        {
            throw new IllegalArgumentException( String.format( "Invalid DMS Latitude: '%s' is out of range [ %s, %s ]",
                                                               getDecimalDegrees(),
                                                               range.getMin(),
                                                               range.getMax() ) );
        }
    }



    static protected @NonNull BigDecimal parse( @NonNull final String input )
    {
        Matcher m = GISPatterns.DMS_LAT_PATTERN.matcher( input );
        if ( m.find() )
        {
            BigDecimal deg = new BigDecimal( m.group( 1 ) );
            BigDecimal min = new BigDecimal( m.group( 2 ) );
            BigDecimal sec = new BigDecimal( m.group( 3 ) );
            BigDecimal result = deg.add( min.divide( BigDecimal.valueOf( 60 ), 10, RoundingMode.HALF_UP ) )
                                   .add( sec.divide( BigDecimal.valueOf( 3600 ), 10, RoundingMode.HALF_UP ) );
            if ( "S".equalsIgnoreCase( m.group( 4 ) ) )
            {
                result = result.negate();
            }
            return result;
        }
        else
        {
            throw new IllegalArgumentException( String.format( "Invalid DMS Latitude: '%s'", input ) );
        }
    }

}

