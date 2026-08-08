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

class GISDDMLatitudeTest
{

    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {
        @Test
        @DisplayName( "Should correctly parse valid DDM latitude strings")
        void constructor_validInput_instantiatesInstance()
        {
            // 40° 45.18'N -> 40 + (45.18 / 60) = 40.753
            GISDDMLatitude latNorth = new GISDDMLatitude( "40°45.18'N" );
            // 40° 45.18'S -> -(40 + (45.18 / 60)) = -40.753
            GISDDMLatitude latSouth = new GISDDMLatitude( "40°45.18'S" );

            assertAll(
                () -> assertThat( latNorth.getDecimalDegrees().setScale( 3, RoundingMode.HALF_UP ) )
                          .isEqualByComparingTo( "40.753" ),
                () -> assertThat( latSouth.getDecimalDegrees().setScale( 3, RoundingMode.HALF_UP ) )
                          .isEqualByComparingTo( "-40.753" )
                     );
        }

        @ParameterizedTest
        @ValueSource( strings = { "91°0.0'W", "90°0.1'W", "abc", "", "-40°45.18'N",
                                  "-91°0.0'W", "-90°0.1'W" }
        )
        @DisplayName( "Should throw IllegalArgumentException for invalid DDM latitude")
        void constructor_badInput_throwsIllegalArgument( final String input )
        {
            assertThatThrownBy( () -> new GISDDMLatitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DDM Latitude" );
        }

        @ParameterizedTest
        @ValueSource( doubles = { 91, -91, 200, 90.001, -90.003 } )
        @DisplayName( "Should throw IllegalArgumentException for out of range DD longitude")
        void constructor_badNumericRange_throwsIllegalArgument( final double input )
        {
            assertThatThrownBy( () -> new GISDDMLatitude( BigDecimal.valueOf( input ) ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DDM Latitude: '%s'", input )
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
            GISDDMLatitude lat = GISDDMLatitude.of( val );
            assertThat( lat.getDecimalDegrees() ).isEqualByComparingTo( val );
        }

        @Test
        @DisplayName( "Should correctly handle instantiation via of( String )")
        void ofString_instantiates()
        {
            GISDDMLatitude lat = GISDDMLatitude.of( "89°0.0'S" );
            assertThat( lat.getDecimalDegrees() ).isEqualByComparingTo( BigDecimal.valueOf( -89.0 ) );
        }
    }
}


