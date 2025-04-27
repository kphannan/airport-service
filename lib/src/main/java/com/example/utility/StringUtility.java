/* (C) 2025 */

package com.example.utility;


import java.util.Collection;
import java.util.List;

import jakarta.annotation.Nullable;


/**
 * Helper methods for manipulating and testing strings.
 */
public final class StringUtility
{

    private StringUtility()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }


    /**
     * Null safe check if a {@code substring} is found within (@code target}.
     *
     * @param target   the string to examine.
     * @param criteria the possible sub-string.
     * @return true if the substring is found, false otherwise.
     */
    public static boolean nullSafeContains( @Nullable final String target,
                                            @Nullable final String criteria )
    {
        if ( null == target || null == criteria )
        {
            return false;
        }

        return target.contains( criteria );
    }


    /**
     * Iterate over the elements in the collection {@code messages} checking each
     * element for {@code desiredSubstring} as a substring within the element.
     *
     * @param messages collection of strings to examine for the
     *                 {@code desiredString}
     * @param criteria the string to look for.
     * @return true if the {@code desiredString} is present.
     */
    public static boolean inAnyOf( final List<String> messages,
                                   final String criteria )
    {
        if ( null != messages && !messages.isEmpty() )
        {
            return null != criteria && messages.stream().anyMatch( e -> 0 <= e.indexOf( criteria ) );
        }

        return false;
    }


    /**
     * Determine if the string contains any of the specified sub-strings.
     *
     * @param target     the string to examine.
     * @param substrings a list of possible sub-strings.
     * @return true if one of the substrings is found, false otherwise.
     */
    public static boolean hasAnyOf( @Nullable final String target,
                                    @Nullable final Collection<String> substrings )
    {
        if ( null == substrings )
        {
            return false;
        }

        for ( final String substring : substrings )
        {
            if ( nullSafeContains( target, substring ) )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Determine if the string contains any of the specified sub-strings.
     *
     * @param target     the string to examine.
     * @param substrings a list of possible sub-strings.
     * @return true if one of the substrings is found, false otherwise.
     */
    public static boolean hasAllOf( @Nullable final String target,
                                    @Nullable final Collection<String> substrings )
    {
        if ( null == substrings )
        {
            return false;
        }

        for ( final String substring : substrings )
        {
            if ( !nullSafeContains( target, substring ) )
            {
                return false;
            }
        }

        return true;
    }

}
