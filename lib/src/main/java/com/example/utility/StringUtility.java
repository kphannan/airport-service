/* (C)2025 */

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
     * Iterate over the elements in the collection {@code messages} checking each
     * element for {@code desiredSubstring} as a substring within the element.
     *
     * @param messages      collection of strings to examine for the
     *                      {@code desiredString}
     * @param desiredString the string to look for.
     *
     * @return true if the {@code desiredString} is present.
     */
    public static boolean contains( final List<String> messages,
                                    final String desiredString )
    {
        if ( null != messages && !messages.isEmpty() )
        {
            return null != desiredString && messages.stream().anyMatch( e -> 0 <= e.indexOf( desiredString ) );
        }

        return false;
    }



    /**
     * Null safe check if a {@code substring} is found within (@code target}.
     *
     * @param target    the string to examine.
     * @param substring the possible sub-string.
     *
     * @return true if the substring is found, false otherwise.
     */
    public static boolean hasMatchingSubstring( @Nullable final String target,
                                                @Nullable final String substring )
    {
        if ( null == target || null == substring )
        {
            return false;
        }

        return target.contains( substring );
    }



    /**
     * Determine if the string contains any of the specified sub-strings.
     *
     * @param target     the string to examine.
     * @param substrings a list of possible sub-strings.
     *
     * @return true if one of the substrings is found, false otherwise.
     */
    public static boolean hasMatchingSubstring( @Nullable final String target,
                                                @Nullable final Collection<String> substrings )
    {
        if ( null == substrings )
        {
            return false;
        }

        for ( final String substring : substrings )
        {
            if ( hasMatchingSubstring( target, substring ) )
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
     *
     * @return true if one of the substrings is found, false otherwise.
     */
    public static boolean hasAllMatchingSubstring( @Nullable final String target,
                                                   @Nullable final Collection<String> substrings )
    {
        if ( null == substrings )
        {
            return false;
        }

        for ( final String substring : substrings )
        {
            if ( !hasMatchingSubstring( target, substring ) )
            {
                return false;
            }
        }

        return true;
    }

}
