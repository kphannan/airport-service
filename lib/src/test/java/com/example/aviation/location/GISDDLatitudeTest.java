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

/**
 * Unit tests for {@link GISDDLatitude}.
 * <p>
 * Verifies:
 * <ul>
 *   <li>Correct parsing of positive and negative decimal degrees.</li>
 *   <li>Boundary value handling (e.g., 90.0).</li>
 *   <li>Rejection of out-of-range values (> 90 or < -90).</li>
 *   <li>Correct behavior of the {@code of()} factory method.</li>
 * </ul>
 */
class GISDDLatitudeTest
{
    @Nested
    @DisplayName( "Constructor" )
    class Constructor
    {
        @Test
        @DisplayName( "Should correctly parse valid DD latitude strings")
        void constructor_validInput_instantiatesInstance()
        {
            GISDDLatitude lat1 = new GISDDLatitude( "40.753" );
            GISDDLatitude lat2 = new GISDDLatitude( "-40.753" );
            GISDDLatitude lat3 = new GISDDLatitude( "90.0" );

            assertAll( () -> assertThat( lat1.getDecimalDegrees() )
                                 .isEqualByComparingTo( "40.753" ),
                       () -> assertThat( lat2.getDecimalDegrees() )
                                 .isEqualByComparingTo( "-40.753" ),
                       () -> assertThat( lat3.getDecimalDegrees() )
                                 .isEqualByComparingTo( "90.0" )
                     );
        }

        @ParameterizedTest
        @ValueSource( strings = { "91", "-91", "181", "abc", "", "-56N", "90.001" })
        @DisplayName( "Should throw IllegalArgumentException for invalid DD latitude")
        void constructor_badInput_throwsIllegalArgument( final String input )
        {
            assertThatThrownBy( () -> new GISDDLatitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DD Latitude '%s'", input );
        }


        @ParameterizedTest
        @ValueSource( doubles = { 91, -91, 90.001, -90.002, 200 } )
        @DisplayName( "Should throw IllegalArgumentException for out of range DD longitude")
        void constructor_badInput_throwsIllegalArgument( final double input )
        {
            assertThatThrownBy( () -> new GISDDLatitude( BigDecimal.valueOf( input ) ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DD Latitude: '%s'", input )
                .hasMessageContaining( "is out of range [ -90, 90 ]" );
        }
    }

    @Nested
    @DisplayName( "parse()" )
    class Parse
    {
        @ParameterizedTest
        // @ValueSource( strings = { "89°", /*"-89°",*/ "87.2°", "70.003°N" /*, "70.005°S" */ })
        @CsvSource( {
            "89°,         89",
            "-89°,       -89.",
            "87.2°,       87.2",
            "70.003°N,    70.003",
            "70.005°S,   -70.005",
            "70.005°s,   -70.005",
        })
        @DisplayName( "accept valid DD latitude")
        void parse_validInput_returnsValue( final String input, final BigDecimal expected )
        {
            final String numberOnly = input
                                          .replace( "N", "" )
                                          .replace( "n", "" )
                                          .replace( "S", "" )
                                          .replace( "s", "" )
                                          .replace( "°", "" );
            final BigDecimal lat = GISDDLatitude.parse( input );
            assertThat( lat )
                .isEqualByComparingTo( expected );
        }


        @ParameterizedTest
        @ValueSource( strings = { "91°", "-91°", "-70.003°N", "-70.005°S", "+70.005°S" })
        @DisplayName( "Should throw IllegalArgumentException for invalid DD latitude")
        void parse_invalidInput_throws( final String input )
        {
            assertThatThrownBy( () -> new GISDDLatitude( input ) )
                .isInstanceOf( IllegalArgumentException.class )
                .hasMessageContaining( "Invalid DD Latitude '%s'", input );
                // .hasMessageContaining( "Invalid DD Latitude '%s'", input );
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
            GISDDLatitude lat = GISDDLatitude.of( val );
            assertThat( lat.getDecimalDegrees() ).isEqualByComparingTo( val );
        }

        @Test
        @DisplayName( "Should correctly handle instantiation via of( String )")
        void ofString_instantiates()
        {
            GISDDLatitude lat = GISDDLatitude.of( "45.321" );
            assertThat( lat.getDecimalDegrees() ).isEqualByComparingTo( BigDecimal.valueOf( 45.321 ) );
        }
    }
}
