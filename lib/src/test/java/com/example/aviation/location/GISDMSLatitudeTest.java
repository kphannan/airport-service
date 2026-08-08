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

class GISDMSLatitudeTest
{

    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {
        @Test
        @DisplayName( "Should correctly parse valid DMS latitude strings")
        void constructor_validInput_instantiatesInstance()
        {
            // 40° 45' 11" N -> 40 + (45/60) + (11/3600) = 40.753055...
            GISDMSLatitude latNorth = new GISDMSLatitude( "40°45'11\"N" );
            // 40° 45' 11" S -> -(40 + (45/60) + (11/3600)) = -40.753055...
            GISDMSLatitude latSouth = new GISDMSLatitude( "40°45'11\"S" );

            assertAll( () -> assertThat( latNorth.getDecimalDegrees().setScale( 5, RoundingMode.HALF_UP ) )
                                 .isEqualByComparingTo( "40.75306" ),
                       () -> assertThat( latSouth.getDecimalDegrees().setScale( 5, RoundingMode.HALF_UP ) )
                                 .isEqualByComparingTo( "-40.75306" )
                     );
        }

        @ParameterizedTest
        @ValueSource( strings = { "91°0'0\"N", "abc", "", "-40°45'11\"N" })
        @DisplayName( "Should throw IllegalArgumentException for invalid DMS latitude")
        void constructor_badInput_throwsIllegalArgument( final String input )
        {
            assertThatThrownBy( () -> new GISDMSLatitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DMS Latitude: '%s'", input );
        }

        @ParameterizedTest
        @ValueSource( doubles = { 181, -181, 200, 180.001, -180.003 })
        @DisplayName( "Should throw IllegalArgumentException for out of range DD longitude")
        void constructor_badNumericRange_throwsIllegalArgument( final double input )
        {
            assertThatThrownBy( () -> new GISDMSLatitude( BigDecimal.valueOf( input ) ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DMS Latitude: '%s'", input )
                .hasMessageContaining( "is out of range [ -90, 90 ]" );
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
            BigDecimal    val = new BigDecimal( "45.123" );
            GISDMSLatitude lat = GISDMSLatitude.of( val );
            assertThat( lat.getDecimalDegrees() ).isEqualByComparingTo( val );
        }

        @Test
        @DisplayName( "Should correctly handle instantiation via of( String )")
        void ofString_instantiates()
        {
            GISDMSLatitude lat = GISDMSLatitude.of( "89°0'0\"S" );
            assertThat( lat.getDecimalDegrees() )
                .isEqualByComparingTo( BigDecimal.valueOf( -89.0 ) );
        }
    }

}
