package com.example.utility;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RangeTest
{

    @Test
    @DisplayName( "Should correctly identify values within a BigDecimal range" )
    void testBigDecimalRange()
    {
        Range<BigDecimal> range = new Range<>( new BigDecimal( "10.0" ), new BigDecimal( "20.0" ) );

        assertAll( () -> assertThat( range.isInRange( new BigDecimal( "10.0" ) ) ).isTrue(),
                   () -> assertThat( range.isInRange( new BigDecimal( "15.5" ) ) ).isTrue(),
                   () -> assertThat( range.isInRange( new BigDecimal( "20.0" ) ) ).isTrue(),
                   () -> assertThat( range.isInRange( new BigDecimal( "20.1" ) ) ).isFalse()
        );
    }

    @Test
    @DisplayName( "Should correctly identify values within an Integer range" )
    void testIntegerRange()
    {
        Range<Integer> range = new Range<>( 1, 10 );

        assertAll( () -> assertThat( range.isInRange( 1 ) ).isTrue(),
                   () -> assertThat( range.isInRange( 5 ) ).isTrue(),
                   () -> assertThat( range.isInRange( 10 ) ).isTrue(),
                   () -> assertThat( range.isInRange( 0 ) ).isFalse(),
                   () -> assertThat( range.isInRange( 11 ) ).isFalse()
        );
    }

    @Test
    @DisplayName( "Should correctly identify values within a Double range" )
    void testDoubleRange()
    {
        Range<Double> range = new Range<>( 0.5, 1.5 );

        assertAll( () -> assertThat( range.isInRange( 0.5 ) ).isTrue(),
                   () -> assertThat( range.isInRange( 1.0 ) ).isTrue(),
                   () -> assertThat( range.isInRange( 1.5 ) ).isTrue(),
                   () -> assertThat( range.isInRange( 0.4 ) ).isFalse(),
                   () -> assertThat( range.isInRange( 1.6 ) ).isFalse()
        );
    }

    @Test
    @DisplayName( "Should throw exception when min is greater than max regardless of type" )
    void testInvalidRangeConstruction()
    {
        assertThatThrownBy( () -> new Range<>( 10, 5 ) )
            .isInstanceOf( IllegalArgumentException.class )
            .hasMessageContaining( "Minimum value cannot be greater than maximum value" );
    }

    @Test
    @DisplayName( "Should handle single-point ranges (min == max)" )
    void testSinglePointRange()
    {
        Range<Integer> range = new Range<>( 10, 10 );

        assertAll( () -> assertThat( range.isInRange( 10 ) ).isTrue(),
                   () -> assertThat( range.isInRange( 11 ) ).isFalse(),
                   () -> assertThat( range.isInRange( 9 ) ).isFalse()
        );
    }


    @Test
    @DisplayName( "Should correctly validate using the static isInRange method" )
    void testStaticIsInRange()
    {
        assertAll(
            // Test with Integers: Value 5 is in range [1, 10]
            () -> assertThat( Range.isInRange( 5, 1, 10 ) ).isTrue(),
            // Test with Integers: Value 11 is outside range [1, 10]
            () -> assertThat( Range.isInRange( 11, 1, 10 ) ).isFalse(),
            // Test with BigDecimals: Value 15.0 is in range [10.0, 20.0]
            () -> assertThat( Range.isInRange( new BigDecimal( "15.0" ), new BigDecimal( "10.0" ), new BigDecimal( "20.0" ) ) ).isTrue(),
            // Test with BigDecimals: Value 5.0 is outside range [10.0, 20.0]
            () -> assertThat( Range.isInRange( new BigDecimal( "5.0" ), new BigDecimal( "10.0" ), new BigDecimal( "20.0" ) ) ).isFalse(),
            // Test with Strings: "B" is alphabetically between "A" and "C"
            () -> assertThat( Range.isInRange( "B", "A", "C" ) ).isTrue(),
            () -> assertThat( Range.isInRange( "D", "A", "C" ) ).isFalse()
        );
    }

    @Test
    @DisplayName( "Should throw exception when static method is called with min > max" )
    void testStaticInvalidRange()
    {
        assertThatThrownBy( () -> Range.isInRange( 5, 10, 1 ) )
            .isInstanceOf( IllegalArgumentException.class )
            .hasMessageContaining( "Minimum value cannot be greater than maximum value" );
    }

}
