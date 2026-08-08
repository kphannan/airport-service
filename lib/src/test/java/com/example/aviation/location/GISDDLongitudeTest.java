package com.example.aviation.location;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class GISDDLongitudeTest
{

    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {
        @Test
        @DisplayName( "Should correctly parse valid DD longitude strings")
        void constructor_validInput_instantiatesInstance()
        {
            GISDDLongitude lon0 = new GISDDLongitude( "0.0" );
            GISDDLongitude lon1 = new GISDDLongitude( "73.983" );
            GISDDLongitude lon2 = new GISDDLongitude( "-73.983" );
            GISDDLongitude lon3 = new GISDDLongitude( "180.0" );
            GISDDLongitude lon4 = new GISDDLongitude( "-180.0" );

            assertAll( () -> assertThat( lon0.getDecimalDegrees() )
                                 .isEqualByComparingTo( "0.000" ),
                       () -> assertThat( lon1.getDecimalDegrees() )
                                 .isEqualByComparingTo( "73.983" ),
                       () -> assertThat( lon2.getDecimalDegrees() )
                                 .isEqualByComparingTo( "-73.983" ),
                       () -> assertThat( lon3.getDecimalDegrees() )
                                 .isEqualByComparingTo( "180.0" ),
                       () -> assertThat( lon4.getDecimalDegrees() )
                                 .isEqualByComparingTo( "-180.0" )
                     );
        }


        @ParameterizedTest
        @ValueSource( strings = { "181", "-181", "200", "abc", "", "-44E" })
        @DisplayName( "Should throw IllegalArgumentException for invalid DD longitude")
        void constructor_badInput_throwsIllegalArgument( final String input )
        {
            assertThatThrownBy( () -> new GISDDLongitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DD Longitude" );
        }

        @ParameterizedTest
        @ValueSource( doubles = { 181, -181, 200, 180.001, -180.003 } )
        @DisplayName( "Should throw IllegalArgumentException for out of range DD longitude")
        void constructor_badNumericRange_throwsIllegalArgument( final double input )
        {
            assertThatThrownBy( () -> new GISDDLongitude( BigDecimal.valueOf( input ) ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DD Longitude: '%s'", input )
                .hasMessageContaining( "is out of range [ -180, 180 ]" );
        }
    }

    @Nested
    @DisplayName( "parse()" )
    class Parse
    {
        @ParameterizedTest
        // @ValueSource( strings = { "180°", "-180°", "87.2°", "70.003°E", "70.005°E", "70.005°W" })
        @CsvSource( {
            "180°,        180",
            "-180°,      -180",
            "87.2°,        87.2",
            "70.003°E,     70.003",
            "70.005°E,     70.005",
            "70.005°W,    -70.005",
            "70.007°w,    -70.007"
        })
        @DisplayName( "accept valid DD longitude")
        void parse_validInput_returnsValue( final String input, final BigDecimal expected )
        {
            // final String numberOnly = input
            //                               .replace( "E", "" )
            //                               .replace( "e", "" )
            //                               .replace( "W", "" )
            //                               .replace( "w", "" )
            //                               .replace( "°", "" );
            final BigDecimal longitude = GISDDLongitude.parse( input );
            assertThat( longitude )
                .isEqualByComparingTo( expected );
        }


        @ParameterizedTest
        @ValueSource( strings = { "181°", "-181°", "-70.003°W", "-70.005°E", "+70.005°E" })
        @DisplayName( "Should throw IllegalArgumentException for invalid DD longitude")
        void parse_invalidInput_throws( final String input )
        {
            assertThatThrownBy( () -> new GISDDLongitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DD Longitude '%s'", input );
        }
    }



    @Nested
    @DisplayName( "of" )
    class Of
    {
        @Test
        @DisplayName( "Should correctly handle instantiation via of( String )")
        void ofString_instantiates()
        {
            GISDDLongitude lon = GISDDLongitude.of( "-120.5" );
            assertThat( lon.getDecimalDegrees() ).isEqualByComparingTo( BigDecimal.valueOf( -120.5 ) );
        }

        @Test
        @DisplayName( "Should correctly handle instantiation via of( BigDecimal )")
        void ofNumber_instantiates()
        {
            GISDDLongitude lon = GISDDLongitude.of( BigDecimal.valueOf( -120.6 ) );
            assertThat( lon.getDecimalDegrees() ).isEqualByComparingTo( BigDecimal.valueOf( -120.6 ) );
        }
    }
}
