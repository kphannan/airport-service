/* (C) 2024 */

package com.example.utility;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.StringJoiner;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;



/**
 * Unit tests for the {@code NumberFormat} utility class.
 *
 * <p>
 * Bit patterns for some hex 'words'
 * <ul>
 * <li>BEAD - 1011 1110 1010 1111</li>
 * <li>CAFE - 1100 1010 1111 1110</li>
 * <li>FACE - 1111 1010 1100 1110</li>
 * <li>FADE - 1111 1010 1101 1110</li>
 * <li>DEAD - 1101 1110 1010 1101</li>
 * <li>BEEF - 1011 1110 1110 1111</li>
 * </ul>
 */
/* default */
class NumberFormatTest
{
    @Test
    @DisplayName( "NumberFormat is a proper utility class" )
    void isUtility()
    {
        final StringJoiner reason = new StringJoiner( "; ", "[", "]" );

        assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( NumberFormat.class, null ) ),
                   () -> assertEquals( "[]", reason.toString() ) );
    }

    // ----- Exceptions -----


    @Nested
    @DisplayName( "An exception is thrown when")
    class Exceptions
    {

        @Test
        @DisplayName( "number of bits to display is negative" )
        void binaryString_shortWithNegativeBits_throwsIllegalArgumentException()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> NumberFormat.toBinaryString( (short)0xDEAF, 4, -1 ) );

            assertEquals( "-1 bits exceeds the length (64) of a long primitive", thrown.getMessage() );
        }


        @Test
        @DisplayName( "asked to display 17 bits for a short (16 bit value)" )
        void binaryString_shortWith17Bits_throwsIllegalArgumentException()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> NumberFormat.toBinaryString( (short)0xDEAF, 4, 17 ) );

            assertEquals( "17 bits exceeds the length (16) of a short primitive", thrown.getMessage() );
        }


        @Test
        @DisplayName( "asked to display 33 bits for a int (32 bit value)" )
        void binaryString_intWith33Bits_throwsIllegalArgumentException()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> NumberFormat.toBinaryString( 0xDEADBEEF, 4, 33 ) );

            assertEquals( "33 bits exceeds the length (32) of a int primitive", thrown.getMessage() );
        }


        @Test
        @DisplayName( "asked to display 65 bits for a long (65 bit value)" )
        void binaryString_longWith65Bits_throwsIllegalArgumentException()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> NumberFormat.toBinaryString( 0xBEADCAFEFACEFADEL, 4, 65 ) );

            assertEquals( "65 bits exceeds the length (64) of a long primitive", thrown.getMessage() );
        }
    }


    /** A bit size of zero (0) is a trivial case that produces and empty string. */
    @Test
    @DisplayName( "Zero bits is an empty string" )
    void binaryString_zeroBits_returnsEmptyString()
    {
        final String value = NumberFormat.toBinaryString( 0xDEAF, 4, 0 );

        // D E A F
        assertEquals( "", value );
    }



    @Test
    @DisplayName( "16 bits format correctly" )
    void binaryString_shortArg_returnsFormattedString()
    {
        final short  number = (short)0xDEAF;
        final String value  = NumberFormat.toBinaryString( number, 4, 16 );

        // D E A F
        assertEquals( "1101 1110 1010 1111", value );
    }



    @Test
    @DisplayName( "32 bits format correctly" )
    void binaryString_intArg_returnsFormattedString()
    {
        final String value = NumberFormat.toBinaryString( 0xDEADBEEF, 4, 32 );

        // D E A D - B E E F
        assertEquals( "1101 1110 1010 1101 1011 1110 1110 1111", value );
    }



    @Test
    @DisplayName( "64 bits format correctly" )
    void binaryString_longArg_returnsFormattedString()
    {
        final String value = NumberFormat.toBinaryString( 0xBEADCAFEFACEFADEL, 4, 64 );

        // B E A D - C A F E - F A C E - F A D E
        assertEquals( "1011 1110 1010 1101 1100 1010 1111 1110 1111 1010 1100 1110 1111 1010 1101 1110", value );
    }

    /** Parameterized test input for {@code toBinaryString()}. */
    private static final class SimpleFormatProvider implements ArgumentsProvider
    {
        @Override
        public Stream<? extends Arguments> provideArguments( ExtensionContext context ) throws Exception
        {
            return Stream.of( Arguments.of( 0, 0, 0, "" ), //
                              Arguments.of( 0, 1, 1, "0" ), //
                              Arguments.of( 1, 1, 1, "1" ), //
                              Arguments.of( 0, 4, 4, "0000" ), //
                              Arguments.of( 12, 4, 4, "1100" ), //
                              Arguments.of( 0b01011010, 4, 8, "0101 1010" ),
                              Arguments.of( 0xFFFF, 16, 16, "1111111111111111" ),
                              Arguments
                                      .of( 0xBEADCAFEFACEFADEL, 4, 64,
                                           "1011 1110 1010 1101 1100 1010 1111 1110 1111 1010 1100 1110 1111 1010 1101 1110" ) );
        }
    }

    @DisplayName( "format various length integers" )
//    @ParameterizedTest( name = "{3} is produced by binaryString( {0}, {1}, {2} )" )
    @ParameterizedTest( name = "{2} bits with grouping {1} is ({3})" )
    @ArgumentsSource( SimpleFormatProvider.class )
    void binaryString_validInput_returnCorrectString( final long number,
                                                      final int groupSize,
                                                      final int bits,
                                                      final String result )
    {
        assertEquals( result, NumberFormat.toBinaryString( number, groupSize, bits ) );
    }

}
