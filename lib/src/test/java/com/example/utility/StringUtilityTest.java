/* (C) 2025 */

package com.example.utility;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;



/**
 * Unit tests for the {@code StringUtilty} utility class.
 *
 * <pre>
 * case  list       target
 * ----  ---------  ---------
 *   1   null       null
 *   2   null       non-null
 *   3   empty      null
 *   4   empty      non-null
 *   5   non-empty  null
 *   6   non-empty  non-null - no match
 *   7   non-empty  non-null - match (a: full, b: substring)
 * </pre>
 */
// intentional practice to use literals in tests for clarity instead of chasing
// down constant symbols
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class StringUtilityTest
{

    /** Ensure the {@code StringUtiility} satisfies the Utility class design. */
    @Test
    void stringUtility_isProperUtilityClass()
    {
        final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

        assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( StringUtility.class, reason ) ),
                   () -> assertEquals( "[]", reason.toString() ) );
    }

    // ===== Null inputs =====



    /** Case 1. */
    @Test
    void string_containsNulls_returnsFalse()
    {
        assertFalse( StringUtility.contains( null, null ) );
    }



    /** Case 2. */
    @Test
    void string_nullListAndNonNullTarget_returnsFalse()
    {
        assertFalse( StringUtility.contains( null, "Desired string" ) );
    }



    /** Case 3. */
    @Test
    void string_emptyListAndNullTarget_returnsFalse()
    {
        assertFalse( StringUtility.contains( Collections.emptyList(), null ) );
    }



    /** Case 4. */
    @Test
    void string_emptyListAndNonNullTarget_returnsFalse()
    {
        assertFalse( StringUtility.contains( Collections.emptyList(), "Desired string" ) );
    }



    /** Case 5. */
    @Test
    void string_onNullListAndNullTarget_returnsFalse()
    {
        assertFalse( StringUtility.contains( Arrays.asList( "One", "Two", "Three" ), null ) );
    }



    // ===== Values for all args =====
    /** Case 6. */
    @Test
    void string_ListAndNonNullTargetWithoutMatch_returnsFalse()
    {
        assertFalse( StringUtility.contains( Arrays.asList( "One", "Two", "Three" ), "Desired string" ) );
    }



    /** Case 7a. */
    @Test
    void string_ListAndNullTargetWithFullMatch_returnsTrue()
    {
        assertTrue( StringUtility.contains( Arrays.asList( "One", "Two", "Three" ), "Two" ) );
    }



    /** Case 7b. */
    @Test
    void string_ListAndNullTargetWithPartialMatch_returnsTrue()
    {
        assertTrue( StringUtility.contains( Arrays.asList( "One", "Two", "Three" ), "ree" ) );
    }



    /** Is the target string one of a set of possibilities. */
    @ParameterizedTest
    @ArgumentsSource( MatcherArgumentsProvider.class )
    void matcher_nullOrEmptyArgs_returnFalse( @Nullable final String target,
                                              @Nullable final List<String> source,
                                              final boolean expected )
    {
        assertEquals( expected, StringUtility.hasMatchingSubstring( target, source ) );
    }

    /** Parameterized test input for {@code hasMatchSubstring()}. */
    private static final class MatcherArgumentsProvider implements ArgumentsProvider
    {
        @Override
        // Ignore null args to Arguments.of(...)
        @SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "argument" } )
        public Stream<? extends Arguments> provideArguments( ExtensionContext context ) throws Exception
        {
            final List<String> nonEmptyList = Arrays.asList( "xyz", "123" );

            return Stream.of( Arguments.of( null, (List<String>)null, false ), //
                              Arguments.of( "", (List<String>)null, false ), //
                              Arguments.of( "   ", (List<String>)null, false ), //
                              Arguments.of( null, Collections.emptyList(), false ), //
                              Arguments.of( "", Collections.emptyList(), false ), //
                              Arguments.of( "   ", Collections.emptyList(), false ), //

                              Arguments.of( null, nonEmptyList, false ), //
                              Arguments.of( "", nonEmptyList, false ), //
                              Arguments.of( "   ", nonEmptyList, false ), //

                              // Non-blank
                              Arguments.of( "abc", (List<String>)null, false ), //
                              Arguments.of( "abc", Collections.emptyList(), false ), //
                              Arguments.of( "abc", nonEmptyList, false ), //
                              Arguments.of( "xyz", nonEmptyList, true ) );
        }
    }



    // --- basic string checks
    /** Parameterized test input for {@code hasMatchSubstring()}. */
    private static final class StringMatchingProvider implements ArgumentsProvider
    {
        @Override
        // Ignore null args to Arguments.of(...)
        @SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "argument" } )
        public Stream<? extends Arguments> provideArguments( ExtensionContext context ) throws Exception
        {
            return Stream.of( Arguments.of( null, null, false ), //
                              Arguments.of( null, null, false ), //
                              Arguments.of( null, "foo", false ), //
                              Arguments.of( "bar", null, false ), //
                              Arguments.of( "bar", "foo", false ), //
                              Arguments.of( "baz", "baz", true ) );
        }
    }

    /** Validate null-safe contains(). */
    @ParameterizedTest
    @ArgumentsSource( StringMatchingProvider.class )
    void matcher_withStrings( @Nullable final String target,
                              @Nullable final String source,
                              final boolean expected )
    {
        assertEquals( expected, StringUtility.hasMatchingSubstring( target, source ) );
    }



    @Test
    void string_targetCollection_nullTarget_returnsFalse()
    {
        Collection<String> desired = Arrays.asList( "One", "Two", "Three" );

        assertFalse( StringUtility.hasAllMatchingSubstring( null, desired ) );
    }



    @Test
    void string_targetCollection_nullSubstring_returnsFalse()
    {
        assertFalse( StringUtility.hasAllMatchingSubstring( "Kilroy", null ) );
    }



    @Test
    void string_targetCollection_blankTarget_returnsFalse()
    {
        Collection<String> desired = Arrays.asList( "One", "Two", "Three" );

        assertFalse( StringUtility.hasAllMatchingSubstring( "", desired ) );
    }



    @Test
    void string_targetCollection_fullMatch_returnsTrue()
    {
        Collection<String> desired = Arrays.asList( "One", "Two", "Three" );

        assertTrue( StringUtility.hasAllMatchingSubstring( "ThreeTwoOne", desired ) );
    }
}
