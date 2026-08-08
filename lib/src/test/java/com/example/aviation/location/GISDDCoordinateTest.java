package com.example.aviation.location;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GISDDCoordinateTest
{

    @Test
    @DisplayName( "Should correctly compose latitude and longitude in a coordinate pair" )
    void testCoordinateComposition()
    {
        GISDDLatitude  lat = new GISDDLatitude( "40.753" );
        GISDDLongitude lon = new GISDDLongitude( "-73.983" );

        GISDDCoordinate coord = new GISDDCoordinate( lat, lon );

        assertAll( () -> assertThat( coord.latitude ).isSameAs( lat ),
                   () -> assertThat( coord.longitude ).isSameAs( lon ),
                   () -> assertThat( coord.latitude.getDecimalDegrees() )
                             .isEqualByComparingTo( "40.753" ),
                   () -> assertThat( coord.longitude.getDecimalDegrees() )
                             .isEqualByComparingTo( "-73.983" )
        );
    }

    @Test
    @DisplayName( "Should handle boundary values in coordinate pair" )
    void testBoundaryComposition()
    {
        GISDDLatitude  lat = new GISDDLatitude( "90.0" );
        GISDDLongitude lon = new GISDDLongitude( "-180.0" );

        GISDDCoordinate coord = new GISDDCoordinate( lat, lon );

        assertAll( () -> assertThat( coord.latitude.getDecimalDegrees() ).isEqualByComparingTo( "90.0" ),
                   () -> assertThat( coord.longitude.getDecimalDegrees() ).isEqualByComparingTo( "-180.0" )
        );
    }
}
