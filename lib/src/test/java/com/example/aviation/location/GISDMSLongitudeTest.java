package com.example.aviation.location;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GISDMSLongitudeTest
{

    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {
        @Test
        @DisplayName( "Should correctly parse valid DMS longitude strings" )
        void constructor_validInput_instantiatesInstance()
        {
            // 73° 58' 59" W -> -(73 + (58/60) + (59/3600)) = -73.983055...
            GISDMSLongitude lonWest = new GISDMSLongitude( "73°58'59\"W" );
            // 73° 58' 59" E -> (73 + (58/60) + (59/3600)) = 73.983055...
            GISDMSLongitude lonEast = new GISDMSLongitude( "73°58'59\"E" );

            assertAll( () -> assertThat( lonWest.getDecimalDegrees().setScale( 5, RoundingMode.HALF_UP ) )
                                 .isEqualByComparingTo( "-73.98306" ),
                       () -> assertThat( lonEast.getDecimalDegrees().setScale( 5, RoundingMode.HALF_UP ) )
                                 .isEqualByComparingTo( "73.98306" )
            );
        }

        @ParameterizedTest
        // @ValueSource( strings = { "181°0'0\"W", "abc", "", "73°61'0\"W" } )  // TODO
        @ValueSource( strings = { "181°0'0\"N", "180°0'1\"N", "181°0'0\"S", "180°0'1\"S",
                                  "abc", "", "73°61'0\"W" } )
        @DisplayName( "Should throw IllegalArgumentException for invalid DMS longitude" )
        void constructor_badInput_throwsIllegalArgument( final String input )
        {
            assertThatThrownBy( () -> new GISDMSLongitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DMS Longitude: '%s'", input );
                // .hasMessageContaining( "is out of range [ -180, 180 ]" );
        }

        @ParameterizedTest
        @ValueSource( strings = { "181°0.0'W", "180°0.1'W" })
        @DisplayName( "invalid range DMS longitude")
        void testOutsideRange( final String input )
        {
            assertThatThrownBy( () -> new GISDMSLongitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DMS Longitude:" );
                // .hasMessageContaining( "is out of range [ -180, 180 ]" );
        }
    }


    @Nested
    @DisplayName( "of" )
    class Of
    {
        @Test
        @DisplayName( "Should correctly handle instantiation via of( BigDecimal )")
        void testOfMethod()
        {
            GISDMSLongitude lat = GISDMSLongitude.of( new BigDecimal( "55.335" ) );
            assertThat( lat.getDecimalDegrees() )
                .isEqualByComparingTo( BigDecimal.valueOf( 55.335 ) );
        }

        @Test
        @DisplayName( "Should correctly handle instantiation via of( String )")
        void ofString_instantiates()
        {
            GISDMSLongitude lat = GISDMSLongitude.of( "73°58'59\"W" );
            assertThat( lat.getDecimalDegrees() )
                .isEqualByComparingTo( BigDecimal.valueOf( -73.9830555556 ) );
        }
    }

}
