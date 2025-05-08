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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName( "Is a proper utility" )
    class MeetsDesignCriteria
    {
        /** Ensure the {@code StringUtiility} satisfies the Utility class design. */
        @Test
        @DisplayName( "Conforms to utility class standard" )
        void stringUtility_isProperUtilityClass()
        {
            final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

            assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( StringUtility.class, reason ) ),
                       () -> assertEquals( "[]", reason.toString() ) );
        }
    }

    // Substring string in string - nullSafeContains
    // In any    string in list   - inAnyOf
    // Any of    list in string   - hasAnyOf
    // All of    list in string   - hasAllOf
    @Nested
    @DisplayName( "contains - string in string" ) // nullSafeContains
    class SimpleStrings
    {
        /** Validate null-safe contains(). */
        @DisplayName( "Not found" )
        @ParameterizedTest( name = "{1} not found in {0}" )
        @ArgumentsSource( NoMatchesProvider.class )
        void matcher_withStrings_noMatch( @Nullable final String target,
                                          @Nullable final String criteria,
                                          final boolean expected )
        {
            assertEquals( expected, StringUtility.nullSafeContains( target, criteria ) );
        }

        // --- basic string checks
        /** Parameterized test input for {@code hasMatchSubstring()}. */
        private static final class NoMatchesProvider implements ArgumentsProvider
        {
            @Override
            // Ignore null args to Arguments.of(...)
            @SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "argument" } )
            public Stream<? extends Arguments> provideArguments( ExtensionContext context ) throws Exception
            {
                return Stream.of( Arguments.of( null, null, false ), //
                                  Arguments.of( null, "foo", false ), //
                                  Arguments.of( "bar", null, false ), //
                                  Arguments.of( "bar", "foo", false ) );
            }
        }

        /** Validate null-safe contains(). */
        @DisplayName( "found in" )
        @ParameterizedTest( name = "{1} is in {0}" )
        @ArgumentsSource( HasMatchesProvider.class )
        void matcher_withStrings_hasMatch( @Nullable final String target,
                                           @Nullable final String criteria,
                                           final boolean expected )
        {
            assertEquals( expected, StringUtility.nullSafeContains( target, criteria ) );
        }

        // --- basic string checks
        /** Parameterized test input for {@code hasMatchSubstring()}. */
        private static final class HasMatchesProvider implements ArgumentsProvider
        {
            @Override
            // Ignore null args to Arguments.of(...)
            @SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "argument" } )
            public Stream<? extends Arguments> provideArguments( ExtensionContext context ) throws Exception
            {
                return Stream.of( Arguments.of( "baz", "baz", true ),
                                  Arguments.of( "foobar_baz_andmore", "baz", true )
                                );
            }
        }


    }

    @Nested
    @DisplayName( "has any of..." )  //  list in string - hasAnyOf
    class ListOfStrings
    {
        /** Is the target string one of a set of possibilities. */
        @DisplayName( "null or blank value never matches:" )
        @ParameterizedTest( name = "({0}) is not one of ({1})" )
        @ArgumentsSource( NullOrBlankTarget.class )
        void matcher_nullOrEmptyTarget_returnFalse( @Nullable final String target,
                                                    @Nullable final List<String> criteria,
                                                    final boolean expected )
        {
            assertEquals( expected, StringUtility.hasAnyOf( target, criteria ) );
        }

        /** Parameterized test input for {@code hasMatchSubstring()}. */
        private static final class NullOrBlankTarget implements ArgumentsProvider
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
                                  Arguments.of( "   ", nonEmptyList, false ) ); //
            }
        }

        /** Is the target string one of a set of possibilities? */
        @DisplayName( "value not in collection:" )
        @ParameterizedTest( name = "({0}) not in: ({1})" )
        @ArgumentsSource( SubstringNotIncluded.class )
        void matcher_nullOrEmptyCriteria_returnFalse( @Nullable final String target,
                                                      @Nullable final List<String> criteria,
                                                      final boolean expected )
        {
            assertEquals( expected, StringUtility.hasAnyOf( target, criteria ) );
        }

        /** Parameterized test input for {@code hasMatchSubstring()}. */
        private static final class SubstringNotIncluded implements ArgumentsProvider
        {
            @Override
            // Ignore null args to Arguments.of(...)
            @SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "argument" } )
            public Stream<? extends Arguments> provideArguments( ExtensionContext context ) throws Exception
            {
                final List<String> nonEmptyList = Arrays.asList( "xyz", "123" );

                return Stream.of( Arguments.of( "abc", (List<String>)null, false ),
                                  Arguments.of( "abc", Collections.emptyList(), false ),
                                  Arguments.of( "abc", nonEmptyList, false ),
                                  Arguments.of( "xy",  nonEmptyList, false ),
                                  Arguments.of( "23",  nonEmptyList, false )
                                );
            }
        }

        @DisplayName( "value is any of" )
        @ParameterizedTest( name = "({0}) is any of ({1})" )
        @ArgumentsSource( SubstringIsIncluded.class )
        void matcher_nullOrEmptyArgs_returnTrue( @Nullable final String target,
                                                 @Nullable final List<String> criteria,
                                                 final boolean expected )
        {
            assertEquals( expected, StringUtility.hasAnyOf( target, criteria ) );
        }

        private static final class SubstringIsIncluded implements ArgumentsProvider
        {
            @Override
            // Ignore null args to Arguments.of(...)
            @SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "argument" } )
            public Stream<? extends Arguments> provideArguments( ExtensionContext context ) throws Exception
            {
                final List<String> nonEmptyList = Arrays.asList( "xyz", "123" );

                return Stream.of( Arguments.of( "xyz", nonEmptyList, true ),
                                  Arguments.of( "123", nonEmptyList, true )
                                );
            }
        }
    }






    @Nested
    @DisplayName( "in any of" )  // string in list - inAnyOf
    class OneOfMany
    {
        @Nested
        @DisplayName( "null or blank collection" )
        class NullOrBlankCollection
        {
            /**
             * Case 1.
             */
            @Test
            @DisplayName( "does not contain a null value" )
            void string_containsNulls_returnsFalse()
            {
                assertFalse( StringUtility.inAnyOf( null, null ) );
            }

            // ===== Null inputs =====
            /**
             * Case 2.
             */
            @Test
            @DisplayName( "does not contain any value" )
            void string_nullListAndNonNullTarget_returnsFalse()
            {
                assertFalse( StringUtility.inAnyOf( null, "Desired string" ) );
            }
        }


        @Nested
        @DisplayName( "null or blank is..." )
        class EmptyCriteria
        {
            /**
             * Case 3.
             */
            @Test
            @DisplayName( "not in empty list" )
            void string_emptyListAndNullTarget_returnsFalse()
            {
                assertFalse( StringUtility.inAnyOf( Collections.emptyList(), null ) );
            }

            /** Case 5. */
            @Test
            @DisplayName( "not in a list" )
            void string_onNullListAndNullTarget_returnsFalse()
            {
                assertFalse( StringUtility.inAnyOf( Arrays.asList( "One", "Two", "Three" ), null ) );
            }
        }


        @Nested
        @DisplayName( "does not" )
        class StringHasValue
        {
            /** Case 6. */
            @Test
            @DisplayName( "have any" )
            void string_ListAndNonNullTargetWithoutMatch_returnsFalse()
            {
                assertFalse( StringUtility.inAnyOf( Arrays.asList( "One", "Two", "Three" ), "Desired string" ) );
            }

            /**
             * Case 4.
             */
            @Test
            @DisplayName( "have string in empty collection" )
            void string_emptyListAndNonNullTarget_returnsFalse()
            {
                assertFalse( StringUtility.inAnyOf( Collections.emptyList(), "Desired string" ) );
            }
        }

        // ===== Values for all args =====

        @Nested
        @DisplayName( "does..." )
        class EmptySearchTarget
        {
            /**
             * Case 7a.
             */
            @Test
            @DisplayName( "contain a whole word" )
            void string_ListAndNullTargetWithFullMatch_returnsTrue()
            {
                assertTrue( StringUtility.inAnyOf( Arrays.asList( "One", "Two", "Three" ), "Two" ) );
            }


            /**
             * Case 7b.
             */
            @Test
            @DisplayName( "contain a substring" )
            void string_ListAndNullTargetWithPartialMatch_returnsTrue()
            {
                assertTrue( StringUtility.inAnyOf( Arrays.asList( "One", "Two", "Three" ), "ree" ) );
            }
        }

    }




    @Nested
    @DisplayName( "Any of in any" )  // - hasAllOf
    class AnyListWithList
    {
        @Nested
        @DisplayName( "null or blank collection" )
        class NullOrBlankCollection
        {}

        @Test
        @DisplayName( "Desired strings may occur anywhere in the target" )
        void string_targetCollection_fullMatch_returnsTrue()
        {
            final Collection<String> desired = Arrays.asList( "One", "Two", "Three" );

            assertTrue( StringUtility.hasAllOf( "ThreeTwoOne", desired ) );
        }

        @Test
        @DisplayName( "Nothing will match an empty string" )
        void string_targetCollection_blankTarget_returnsFalse()
        {
            final Collection<String> desired = Arrays.asList( "One", "Two", "Three" );

            assertFalse( StringUtility.hasAllOf( "", desired ) );
        }

        @Test
        @DisplayName( "Not possible to locate anything in non-existent list" )
        void string_targetCollection_nullTarget_returnsFalse()
        {
            final Collection<String> desired = Arrays.asList( "One", "Two", "Three" );

            assertFalse( StringUtility.hasAllOf( null, desired ) );
        }










        @Nested
        @DisplayName( "all Of" )
        class AllOf
        {
            @Test
            @DisplayName( "null string never matches" )
            void string_targetCollection_nullSubstring_returnsFalse()
            {
                assertFalse( StringUtility.hasAllOf( "Kilroy", null ) );
            }
        }
    }


}
