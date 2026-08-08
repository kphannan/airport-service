package com.example.aviation.location;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;

import com.example.utility.Range;
import org.jspecify.annotations.NonNull;

public class GISDDMLongitude extends GISLongitude
{
    private static final Range<BigDecimal> range = new Range<>( BigDecimal.valueOf( -180 ),
                                                                BigDecimal.valueOf(  180 ) );
    public GISDDMLongitude( @NonNull final String value )
    {
        this( parse( value ) );
    }

    protected GISDDMLongitude( @NonNull final BigDecimal decimalDegrees )
    {
        super( decimalDegrees );

        // check range -180 <= longitude <= 180 after sign for hemisphere
        if ( !range.isInRange( getDecimalDegrees() ) )
        {
            throw new IllegalArgumentException( String.format( "Invalid DDM Longitude: '%s' is out of range [ %s, %s ]",
                                                               getDecimalDegrees(),
                                                               range.getMin(),
                                                               range.getMax() ) );
        }
    }

    static public GISDDMLongitude of( @NonNull final BigDecimal value )
    {
        return new GISDDMLongitude( value );
    }

    static public GISDDMLongitude of( @NonNull final String value )
    {
        return new GISDDMLongitude( value );
    }

    static protected @NonNull BigDecimal parse( @NonNull final String input )
    {
        Matcher m = GISPatterns.DDM_LONG_PATTERN.matcher(input);
        if (m.find()) {
            BigDecimal deg = new BigDecimal(m.group(1)); // Degrees
            BigDecimal min = new BigDecimal(m.group(2)); // Decimal Minutes

            BigDecimal result = deg.add(min.divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP));
            if ("W".equalsIgnoreCase(m.group(3))) { // Hemisphere
                result = result.negate();
            }
            return result;
        }
        else
        {
            throw new IllegalArgumentException( String.format( "Invalid DDM Longitude '%s'", input ) );
        }
    }
}
