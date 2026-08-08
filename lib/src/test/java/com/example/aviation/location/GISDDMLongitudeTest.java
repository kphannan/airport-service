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

class GISDDMLongitudeTest
{

    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {
        @Test
        @DisplayName( "Should correctly parse valid DDM longitude strings")
        void constructor_validInput_instantiatesInstance()
        {
            // 73° 58.98'W -> -(73 + (58.98 / 60)) = -73.983
            GISDDMLongitude lonWest = new GISDDMLongitude( "73°58.98'W" );
            // 73° 58.98'E -> (73 + (58.98 / 60)) = 73.983
            GISDDMLongitude lonEast = new GISDDMLongitude( "73°58.98'E" );

            assertAll( () -> assertThat( lonWest.getDecimalDegrees().setScale( 3, RoundingMode.HALF_UP ) )
                                 .isEqualByComparingTo( "-73.983" ),
                       () -> assertThat( lonEast.getDecimalDegrees().setScale( 3, RoundingMode.HALF_UP ) )
                                 .isEqualByComparingTo( "73.983" )
                     );
        }

        @ParameterizedTest
        @ValueSource( strings = { "181°0.0'W", "180°0.1'W", "abc", "", "73°61.0'W",
                                  "-181°0.0'W", "-180°0.1'W"
        }
        )
        @DisplayName( "Should throw IllegalArgumentException for invalid DDM longitude")
        void constructor_badInput_throwsIllegalArgument( final String input )
        {
            assertThatThrownBy( () -> new GISDDMLongitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DDM Longitude" );
        }


        @ParameterizedTest
        @ValueSource( doubles = { 181, -181, 200, 180.001, -180.003 } )
        @DisplayName( "Should throw IllegalArgumentException for out of range DMS longitude")
        void constructor_badNumericRange_throwsIllegalArgument( final double input )
        {
            assertThatThrownBy( () -> new GISDMSLongitude( BigDecimal.valueOf( input ) ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DMS Longitude: '%s'", input )
                .hasMessageContaining( "is out of range [ -180, 180 ]" );
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
            GISDDMLongitude lat = GISDDMLongitude.of( new BigDecimal( "55.335" ) );
            assertThat( lat.getDecimalDegrees() )
                .isEqualByComparingTo( BigDecimal.valueOf( 55.335 ) );
        }

        @Test
        @DisplayName( "Should correctly handle instantiation via of( String )")
        void ofString_instantiates()
        {
            GISDDMLongitude lat = GISDDMLongitude.of( "45°0.321'W" );
            assertThat( lat.getDecimalDegrees() )
                .isEqualByComparingTo( BigDecimal.valueOf( -45.00535 ) );
        }
    }
}
