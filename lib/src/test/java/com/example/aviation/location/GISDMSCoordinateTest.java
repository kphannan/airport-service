package com.example.aviation.location;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.RoundingMode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GISDMSCoordinateTest
{

    @Test
    @DisplayName( "Should correctly compose DMS latitude and longitude in a coordinate pair" )
    void testCoordinateComposition()
    {
        GISDMSLatitude  lat = new GISDMSLatitude( "40°45'11\"N" );
        GISDMSLongitude lon = new GISDMSLongitude( "73°58'59\"W" );

        GISDMSCoordinate coord = new GISDMSCoordinate( lat, lon );

        assertAll( () -> assertThat( coord.latitude ).isSameAs( lat ),
                   () -> assertThat( coord.longitude ).isSameAs( lon ),
                   () -> assertThat( lat.getDecimalDegrees().setScale( 5, RoundingMode.HALF_UP ) )
                             .isEqualByComparingTo( "40.75306" ),
                   () -> assertThat( lon.getDecimalDegrees().setScale( 5, RoundingMode.HALF_UP ) )
                             .isEqualByComparingTo( "-73.98306" )
        );
    }
}
