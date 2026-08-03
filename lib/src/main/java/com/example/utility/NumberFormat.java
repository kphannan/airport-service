/* (C) 2024 */

package com.example.utility;


/**
 * Utility for formating numbers.
 *
 * <p>
 * For readabilty, the bit string may group bits together with an intervening
 * space.
 * <table border="1" style="border-collapse:collapse;">
 * <caption>Bit strings for the hexadecimal value <b>0xFACE</b></caption>
 * <thead>
 * <tr>
 * <th>Group Size</th>
 * <th>Formatted</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td style="text-align:center">1</td>
 * <td style="text-align:right">1111101011001110</td>
 * </tr>
 * <tr>
 * <td style="text-align:center">2</td>
 * <td style="text-align:right">11 11 10 10 11 00 11 10</td>
 * </tr>
 * <tr>
 * <td style="text-align:center">3</td>
 * <td style="text-align:right">1 111 101 011 001 110</td>
 * </tr>
 * <tr>
 * <td style="text-align:center">4</td>
 * <td style="text-align:right">1111 1010 1100 1110</td>
 * </tr>
 * </tbody>
 * </table>
 */
public final class NumberFormat
{
    private NumberFormat()
    {
        throw new IllegalStateException();
    }


    /**
     * Converts an integer to a binary string.
     *
     * @param number    The number to convert
     * @param groupSize The number of bits in a group
     * @param bits      The number of bits to display
     *
     * @return The {@code bits} long string
     */
    public static String toBinaryString( final short number,
                                         final int groupSize,
                                         final int bits )
    {
        if ( bits > Short.SIZE )
        {
            throw new IllegalArgumentException(
                    String.format( "%d bits exceeds the length (%d) of a short primitive", bits, Short.SIZE ) );
        }

        return toBinaryString( (long)number, groupSize, bits );
    }


    /**
     * Converts an integer to a binary string.
     *
     * @param number    The number to convert
     * @param groupSize The number of bits in a group
     * @param bits      The number of bits to display
     *
     * @return The {@code bits} long string
     */
    public static String toBinaryString( final int number,
                                         final int groupSize,
                                         final int bits )
    {
        if ( bits > Integer.SIZE )
        {
            throw new IllegalArgumentException(
                    String.format( "%d bits exceeds the length (%d) of a int primitive", bits, Integer.SIZE ) );
        }

        return toBinaryString( (long)number, groupSize, bits );
    }


    /**
     * Converts an integer to a binary string.
     *
     * @param number    The number to convert
     * @param groupSize The number of bits in a group
     * @param bits      The number of bits to display
     *
     * @return The {@code bits} long string
     */
    public static String toBinaryString( final long number,
                                         final int groupSize,
                                         final int bits )
    {
        if ( bits <= 0 )
        {
            throw new IllegalArgumentException( String.format( "%d bits is illegal for a binary representation", bits ) );
        }

        if ( bits > Long.SIZE )
        {
            throw new IllegalArgumentException(
                    String.format( "%d bits exceeds the length (%d) of a long primitive", bits, Long.SIZE ) );
        }

        if ( groupSize > bits )
        {
            throw new IllegalArgumentException(
                String.format( "Grouping of %d bits exceeds the bit length (%d) requested", groupSize, bits ) );
        }

        final StringBuilder result = new StringBuilder();

        // Build the formatted string 1 bit at a time.
        // Test LSB and shift right for next iteration
        long value = number;
        for ( int i = bits - 1; i >= 0; i-- )
        {
            result.append( (value & 1L) == 1 ? "1" : "0" );
            value >>= 1; // shift next bit to LSB

            if ( (i > 0) && (i % groupSize == 0) )
            {
                result.append( ' ' );
            }
        }

        return result.reverse().toString();
    }

}
