package com.example.aviation.location;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.RoundingMode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GISDDMCoordinateTest
{

    @Test
    @DisplayName( "Should correctly compose DDM latitude and longitude in a coordinate pair" )
    void testCoordinateComposition()
    {
        GISDDMLatitude  lat = new GISDDMLatitude( "40°45.18'N" );
        GISDDMLongitude lon = new GISDDMLongitude( "73°58.98'W" );

        GISDDMCoordinate coord = new GISDDMCoordinate( lat, lon );

        assertAll( () -> assertThat( coord.latitude ).isSameAs( lat ),
                   () -> assertThat( coord.longitude ).isSameAs( lon ),
                   () -> assertThat( lat.getDecimalDegrees().setScale( 3, RoundingMode.HALF_UP ) )
                             .isEqualByComparingTo( "40.753" ),
                   () -> assertThat( lon.getDecimalDegrees().setScale( 3, RoundingMode.HALF_UP ) )
                             .isEqualByComparingTo( "-73.983" )
        );
    }
}
