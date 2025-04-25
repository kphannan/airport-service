/* (C) 2024 */

package com.example.utility;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.StringJoiner;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;



/**
 * Unit test for ValidateUtiltyClass.
 */
@SuppressWarnings( { "PMD.TooManyMethods" } )
/* package */
class ValidateUtilityClassTest
{
    private static final String REASON_NOT_NEEDED = "reason given when one should not exist";

    @SuppressWarnings( "initialization.field.uninitialized" )
    private static LogCaptor logCaptor; // will be set in @BeforeAll
    @SuppressWarnings( "initialization.field.uninitialized" )
    private StringJoiner     reason;    // will be set in @BeforeEach

    @BeforeAll
    static void setupOnce()
    {
        logCaptor = LogCaptor.forClass( ValidateUtilityClass.class );
    }



    @AfterAll
    static void teardownFixture()
    {
        logCaptor.close();
    }



    @BeforeEach
    void setup()
    {
        reason = new StringJoiner( "; ", "[", "]" );
    }



    @AfterEach
    void clearFixture()
    {
        logCaptor.clearLogs();
    }

    // ----- Boostrap the validation utility class -----



    /**
     * Use the ValidateUtility class to verify its own implementation.
     */
    @Test
    void boostrapTest_isTrue()
    {
        final StringJoiner reasons = new StringJoiner( "; ", "[", "]" );

        assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( ValidateUtilityClass.class, null ) ),
                   () -> assertEquals( "[]", reasons.toString() ) );
    }




    //================================= Test classes ====================================================
    /** Invalid utility class due to multiple constructors. */
    private static final class MultipleConstructors
    {
        private MultipleConstructors()
        {
            this( 1 );
        }



        /** condition the test is looking for as a cause of failure. */
        @SuppressWarnings( { "PMD.UnusedFormalParameter" } )
        private MultipleConstructors( int dummy )
        {
            // Nothing needed here.
        }
    }

    /** Test class to detect a non-private constructor. */
    private static final class ConstructorNotPrivate
    {
        /* package */
        @SuppressWarnings( "unused" )
        ConstructorNotPrivate()
        {
            // This constructor is intentionally empty. Nothing special is needed here.
        }
    }


    /** Improper utility class: a constructor with arguments. */
    private static final class ConstructorWithArguments
    {
        @SuppressWarnings( "PMD.UnusedFormalParameter" )
        private ConstructorWithArguments( int arg )
        {
            throw new IllegalStateException( "Test exception" );
        }
    }

    /**
     * Valid utility class: minimal (private no-arg constructor, no non-static
     * methods).
     */
    private static final class ClassIsFinal
    {
        @SuppressWarnings( { "PMD.UnnecessaryConstructor" } )
        private ClassIsFinal()
        {
            // Instances are not allowed for utility classes
            throw new IllegalStateException( "test - instance not allowed" );
        }
    }

    /** Improper utility class: class is not final. */
    @SuppressWarnings( "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal" )
    private static class ClassNotFinal
    {
        @SuppressWarnings( { "PMD.UnnecessaryConstructor" } )
        private ClassNotFinal()
        {
        }
    }

    /**
     * Proper utility class: constructor throws the desired exception
     * (IllegalStateException).
     */
    private static final class ConstructorThrows
    {
        private ConstructorThrows()
        {
            throw new IllegalStateException( "Test exception" );
        }
    }



    // -----------
    /** Improper utility class: constructor throws the wrong exception. */
    private static final class ConstructorThrowsCorrectException
    {
        private ConstructorThrowsCorrectException()
        {
            throw new IllegalStateException( "Test exception - cause failure" );
        }
    }

    /**
     * Input class to generate InstantationException when validating behavior of
     * constructor check.
     */
    @SuppressWarnings( "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal" )
    private abstract static class AbstractForFailure
    {
        /** Make a valid abstract class - single abstract method. */
        public abstract boolean foo();
    }


    //================================= Test classes ====================================================




    // ----- Class declaration -----
    @Nested
    @DisplayName( "Individual Criteria" )
    class IndividualCriteria
    {
        @Nested
        @DisplayName( "A class" )
        class ClassCriteria
        {
            @Nested
            @DisplayName( "Is final" )
            class ClassCriteriaFinal
            {

                // --- positive - class is final
                @Test
                @DisplayName( "ensure the class is final" )
                void validate_classIsFinal_returnTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isClassFinal( ClassIsFinal.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ),
                               // log output
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                               // reason text
                               () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );
                }

                // --- negative - class is not final

                @Test
                @DisplayName( "does detect a non-final class" )
                void validate_classNotFinal_returnFalse()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isClassFinal( ClassNotFinal.class, reason );

                    // --- then
                    assertAll( () -> assertFalse( isUtility ),
                               // log output
                               // n/a
                               // reason text
                               () -> assertTrue( 0 <= reason.toString().indexOf( "class must be final" ),
                                                 "Missing reason 'class must be final'" ) );
                }

            }

            @Nested
            @DisplayName( "Not be abstract" )
            class ClassCriteriaNotAbstract
            {

            }
        }

        @Nested
        @DisplayName( "Constructor shall" )
        class ClassCriteriaConstructor
        {



            @Nested
            @DisplayName( "Be only one" )
            class OnlyOneConstructor
            {
                // - multiple constructors

                @Test
                @DisplayName( "Rejects multiple constructors" )
                void validate_multipleConstructor_returnsFalse()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.hasOnlyOneConstructor( MultipleConstructors.class, reason );

                    // --- then
                    assertAll( () -> assertFalse( isUtility ),
                               // log output
                               // n/a
                               // reason text
                               () -> assertTrue( 0 <= reason.toString().indexOf( "There must only be one constructor" ),
                                                 "Missing reason 'There must only be one constructor'" ) );
                }

                // ----- Multiple constructors

                @Test
                @DisplayName( "Verifies a single constructor" )
                void validate_singleConstructor_returnsTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.hasOnlyOneConstructor( ClassIsFinal.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ),
                               // log output
                               // n/a
                               // reason text
                               () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );
                }

            }

            @Nested
            @DisplayName( "Be private" )
            class ConstructorIsPrivate
            {
                // ----- Nature of the constructor -----

                // --- positive - constructor is private



                @Test
                @DisplayName( "requires private constructor" )
                void validate_privateConstructor_returnsTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isConstructorPrivate( ClassIsFinal.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ),
                               // log output
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                               // reason text
                               () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );

                }

                // --- negative - constructor is not private

                @Test
                @DisplayName( "reject non-private constructor" )
                void validate_nonPrivateConstructor_returnsFalse()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isConstructorPrivate( ConstructorNotPrivate.class, reason );

                    // --- then
                    assertAll( () -> assertFalse( isUtility ),
                               // log output
                               // n/a
                               // constructor" ) ),
                               // reason text
                               () -> assertTrue( 0 <= reason.toString().indexOf( "constructor is not private" ),
                                                 "Missing reason 'constructor is not private'" ) );
                }


            }

            @Nested
            @DisplayName( "have no arguments" )
            class OnlyHasNoArgsConstructor
            {

                @Test
                @DisplayName( "Be rejected if it has arguments" )
                void validate_constructorWithArgs_returnFalse()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass
                            .isConstructorPrivate( ConstructorWithArguments.class, reason );

                    // --- then
                    assertAll( () -> assertFalse( isUtility ),
                               // log output
                               // n/a
                               // reason text
                               () -> assertTrue( 0 <= reason.toString().indexOf( "no-argument constructor is not present" ),
                                                 "constructor with arguments not properly detected" ) );
                }



                @Test
                @DisplayName( "be present" )
                void validate_noArgConstructorMissing_returnFalse()
                {
                    // TODO same for abstract class
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isInstantiationDenied( ConstructorWithArguments.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ),
                               // log output
                               // n/a
                               // reason text
                               () -> assertTrue( 0 <= reason.toString().indexOf( "ConstructorWithArguments" ),
                                                 "Invalid class not identified" ),
                               () -> assertTrue( 0 <= reason.toString().indexOf( "no-argument constructor does not exist" ),
                                                 "constructor with arguments not properly detected" ) );
                }

                @Test
                @DisplayName( "detect a constructor that does not properly throw" )
                void validate_classWithNoArgConstructorInstantiation_returnFalse()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isInstantiationDenied( ConstructorNotPrivate.class, reason );

                    // --- then
                    assertAll( () -> assertFalse( isUtility ),
                               // log output
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                               // reason text
                               // () -> assertTrue( isReasonBlank( reason ), REASON_NOT_NEEDED ) );
                               () -> assertTrue( 0 <= reason.toString()
                                                            .indexOf( "ConstructorNotPrivate': Must prevent instantiation" ),
                                                 "utiility constructor should be private" ) );
                }

            }


            @Nested
            @DisplayName( "prevent instantiation" )
            class PreventInstantiation
            {
                @Test
                @DisplayName( "fails to instantiate with a no-argument constructor" )
                void validate_classWithNoArgConstructorInstantiation_returnFalse()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isInstantiationDenied( ConstructorNotPrivate.class, reason );

                    // --- then
                    assertAll( () -> assertFalse( isUtility ),
                               // log output
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                               // reason text
                               // () -> assertTrue( isReasonBlank( reason ), REASON_NOT_NEEDED ) );
                               () -> assertTrue( 0 <= reason.toString()
                                                            .indexOf( "ConstructorNotPrivate': Must prevent instantiation" ),
                                                 "utiility constructor should be private" ) );
                }


                @Test
                @DisplayName( "abstract classes are not allowed" )
                void validate_abstractClassInstantiationDenied_returnTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass.isInstantiationDenied( AbstractForFailure.class, reason );

                    final String reasonString = reason.toString();

                    // --- then
                    assertAll( () -> assertTrue( isUtility ), // basic return code
                               // reason text
                               () -> assertTrue( 0 <= reasonString.indexOf( "ValidateUtilityClassTest$AbstractForFailure" ),
                                                 "Classname of failure mode test is missing. 'AbstractForFailure'" ),
                               () -> assertTrue( 0 <= reasonString.indexOf( "InstantiationException from a utility constructor" ),
                                                 "Missing reason 'InstantiationException'" ) );
                }

            }

            @Nested
            @DisplayName( "instantiation throws exception" )
            class InstantiationThrows
            {
                // --- - prevent instantiation


                /** Verify correct negative behavior. */
                @Test
                @DisplayName( "Accept a constructor throwing IllegalStateException" )
                void validate_constructorThrowsCorrectException_returnTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass
                            .isInstantiationDenied( ConstructorThrowsCorrectException.class,
                                                    reason );
                    // final boolean isUtility = ValidateUtilityClass
                    // .isProperUtilityClass( ConstructorThrowsCorrectException.class, reason );
                    // final List<String> debugLogs = logCaptor.getLogs();

                    // --- then
                    assertAll( () -> assertTrue( isUtility ), // method return value

                               // Verify log entries
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),

                               // Verify reason text
                               () -> assertTrue( isReasonBlank( reason ) ) );
                }
                // -----------

                /** Improper utility class: constructor throws the wrong exception. */
                private static final class ConstructorThrowsIllegalArgumentWithMessage
                {
                    private ConstructorThrowsIllegalArgumentWithMessage()
                    {
                        throw new IllegalArgumentException( "Test exception - cause failure" );
                    }
                }



                /**
                 * Fixture test: class intended to throw an incorrect exception from the
                 * constructor.
                 */
                @SuppressWarnings( { "PMD.AvoidThrowingNullPointerException" } )
                private static final class ConstructorThrowsNullPointerNoMessage
                {
                    private ConstructorThrowsNullPointerNoMessage()
                    {
                        throw new NullPointerException();
                    }
                }



                /**
                 * Fixture test: class intended to throw an incorrect exception from the
                 * constructor.
                 */
                @SuppressWarnings( { "PMD.AvoidThrowingNullPointerException" } )
                private static final class ConstructorThrowsNullPointerWithMessage
                {
                    private ConstructorThrowsNullPointerWithMessage()
                    {
                        throw new NullPointerException( "Test exception - cause failure" );
                    }
                }



                /**
                 * Fixture test class: used to verify a utility constructor prevents
                 * instantiation.
                 */
                private static final class ConstructorThrowsIllegalArgumentNoMessage
                {
                    private ConstructorThrowsIllegalArgumentNoMessage()
                    {
                        throw new IllegalArgumentException();
                    }
                }

                @SuppressWarnings( { "checkstyle:LineLength" } )
                @Test
                @DisplayName( "require IllegalStateException, reject IllegalArgumentException" )
                void validate_constructorDeniedThrowsBadException_returnTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass
                            .isInstantiationDenied( ConstructorThrowsIllegalArgumentNoMessage.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ), // method return value

                               // Verify log entries
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),

                               // Verify reason text
                               // exception without a message
                               () -> assertEquals( "[Unexpected cause 'java.lang.IllegalArgumentException' for "
                                                           + "InvocationTargetException, expecting IllegalStateException]",
                                                   reason.toString() ) );
                }



                @SuppressWarnings( { "LineLength" } )
                @Test
                @DisplayName( "reject IllegalArgumentException, want IllegalStateException" )
                void validate_constructorDeniedThrowsBadExceptionMessage_returnTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass
                            .isInstantiationDenied( ConstructorThrowsIllegalArgumentWithMessage.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ), // method return value

                               // Verify log entries
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),

                               // Verify reason text
                               // exception with a message
                               () -> assertEquals( "[Unexpected cause 'java.lang.IllegalArgumentException: Test exception "
                                                           + "- cause failure' for InvocationTargetException, expecting IllegalStateException]",
                                                   reason.toString() ) );
                }



                @SuppressWarnings( { "checkstyle:LineLength" } )
                @Test
                @DisplayName( "don't accept a NullPointerException" )
                void validate_constructorDeniedThrowsNullPointerNoMessage_returnTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass
                            .isInstantiationDenied( ConstructorThrowsNullPointerNoMessage.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ), // method return value

                               // Verify log entries
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),

                               // Verify reason text
                               // exception without a message
                               () -> assertEquals( "[Unexpected cause 'java.lang.NullPointerException' for "
                                                           + "InvocationTargetException, expecting IllegalStateException]", reason.toString() ) );
                }



                @SuppressWarnings( { "checkstyle:LineLength" } )
                @Test
                @DisplayName( "reject InvocationTargetException" )
                void validate_constructorDeniedThrowsNullPointerWithMessage_returnTrue()
                {
                    // --- when
                    final boolean isUtility = ValidateUtilityClass
                            .isInstantiationDenied( ConstructorThrowsNullPointerWithMessage.class, reason );

                    // --- then
                    assertAll( () -> assertTrue( isUtility ), // method return value

                               // Verify log entries
                               () -> assertTrue( logCaptor.getLogs().isEmpty() ),

                               // Verify reason text
                               // exception with a message
                               () -> assertEquals( "[Unexpected cause 'java.lang.NullPointerException: Test exception "
                                                           + "- cause failure' for InvocationTargetException, expecting IllegalStateException]",
                                                   reason.toString() ) );
                }
            }
        }

        @Nested
        @DisplayName( "Methods..." )
        class ClassCriteriaMethods
        {
            // ----- Static methods

            /** Test target of a valid utility class with a single method. */
            private static final class MethodsAllStatic
            {
                @SuppressWarnings( { "PMD.UnnecessaryConstructor" } )
                private MethodsAllStatic()
                {
                    // Instances are not allowed for utility classes
                    throw new IllegalStateException( "test - instance not allowed" );
                }



                /** Necessary static method to be a utility class. */
                @SuppressWarnings( "unused" )
                public static void isStatic()
                {
                    // method declaration only.
                }
            }



            /** Test class that is not a utility due to a non-static method. */
            private static final class MethodsNotAllStatic
            {
                @SuppressWarnings( { "PMD.UnnecessaryConstructor" } )
                private MethodsNotAllStatic()
                {
                }



                /** condition causing the failure, non-static method. */
                @SuppressWarnings( "unused" )
                public void notStatic()
                {
                    // method declaration only.
                }
            }

            // --- positive - all methods are static
            @Test
            @DisplayName( "are static" )
            void validate_allStaticMethods_returnTrue()
            {
                // --- when
                final boolean isUtility = ValidateUtilityClass.assertAllMethodsAreStatic( MethodsAllStatic.class, reason );

                // --- then
                assertAll( () -> assertTrue( isUtility ),
                           // log output
                           // n/a
                           // reason text
                           () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );
            }



            // - no declared methods
            @Test
            @DisplayName( "may have no methods" )
            void validate_noDeclaredMethods_returnTrue()
            {
                // --- when
                final boolean isUtility = ValidateUtilityClass.assertAllMethodsAreStatic( ClassIsFinal.class, reason );

                // --- then
                assertAll( () -> assertTrue( isUtility ),
                           // log output
                           () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                           // reason text
                           () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );
            }

            // - non-static method



            @Test
            @DisplayName( "non-static methods are properly located" )
            void validate_methodsNotAllStatic_returnFalse()
            {
                // --- when
                final boolean isUtility = ValidateUtilityClass.assertAllMethodsAreStatic( MethodsNotAllStatic.class, reason );

                // --- then
                assertAll( () -> assertFalse( isUtility ),
                           // log output
                           () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                           // reason text
                           () -> assertTrue( 0 <= reason.toString().indexOf( "A non-static method '" ),
                                             "non-static method was not identified'" ),
                           () -> assertTrue( 0 <= reason.toString().indexOf( "MethodsNotAllStatic.notStatic()' exists" ),
                                             "method not identified'" ) );
            }

            // assertAllMethodsAreStatic

        }

    }






    //


    // - has arguments





    // ============ isProperUtilityClass() ============


    @Nested
    @DisplayName( "Overall validation" )
    class ValidateOverall
    {
        // isClassFinal
        @Test
        void utilty_classIsFinal_returnTrue()
        {
            final boolean isUtility = ValidateUtilityClass.isProperUtilityClass( ClassIsFinal.class, reason );

            assertAll( () -> assertTrue( isUtility ),
                       // log output
                       () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                       // reason text
                       () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );
        }


        @Test
        void utilty_classIsNotFinal_returnFalse()
        {
            // --- when
            final boolean isUtility = ValidateUtilityClass.isProperUtilityClass( ClassNotFinal.class, reason );

            // --- then
            assertAll( () -> assertFalse( isUtility ),
                       // log output
                       () -> assertTrue( StringUtility.inAnyOf( logCaptor.getLogs(), "class must be final" ) ),
                       // reason text
                       () -> assertTrue( 0 <= reason.toString().indexOf( "class must be final" ),
                                         "Missing reason 'class must be final'" ) );
        }


        // hasOnlyOneConstructor
        @Test
        void utilty_classHasMultipleConstructors_returnFalse()
        {
            // There must only be one constructor

            // --- when
            final boolean isUtility = ValidateUtilityClass.isProperUtilityClass( MultipleConstructors.class, reason );

            // --- then
            assertAll( () -> assertFalse( isUtility ),
                       // log output
                       () -> assertTrue( StringUtility.inAnyOf( logCaptor.getLogs(),
                                                                "There must only be one constructor" ) ),
                       // reason text
                       () -> assertTrue( 0 <= reason.toString().indexOf( "There must only be one constructor" ),
                                         "Missing reason 'There must only be one constructor'" ) );
        }


        // isConstructorPrivate
        @Test
        void utilty_classConstructorNotPrivate_returnFalse()
        {
            assertAll( () -> assertFalse( ValidateUtilityClass.isProperUtilityClass( ConstructorNotPrivate.class,
                                                                                     reason ) ),
                       // log output
                       // reason text
                       // log output
                       // reason text
                       () -> assertTrue( 0 <= reason.toString().indexOf( "constructor is not private" ),
                                         "Missing expected reason" ) );
        }


        @Test
        @DisplayName( "reject constructor with arguments" )
        void utilty_constructorWithArgs_returnFalse()
        {
            // --- when
            final boolean isUtility = ValidateUtilityClass.isProperUtilityClass( ConstructorWithArguments.class, reason );

            // --- then
            assertAll( () -> assertFalse( isUtility ),
                       // log output
                       () -> assertTrue( StringUtility.inAnyOf( logCaptor.getLogs(),
                                                                "no-argument constructor is not present" ) ),
                       () -> assertTrue( StringUtility.inAnyOf( logCaptor.getLogs(),
                                                                "The no-argument constructor does not exist" ) ),
                       // reason text
                       () -> assertTrue( 0 <= reason.toString().indexOf( "no-argument constructor is not present" ),
                                         "constructor with arguments not properly detected" ) );
        }


        // isInstantiationDenied
        @Test
        void utilty_constructorThrows_returnTrue()
        {
            // --- when
            final boolean      isUtility = ValidateUtilityClass.isProperUtilityClass( ConstructorThrows.class, reason );
            final List<String> debugLogs = logCaptor.getLogs();

            // --- then
            assertAll( () -> assertTrue( isUtility ), // method return value

                       // Verify log entries
                       () -> assertTrue( debugLogs.isEmpty(), "Log should be empty" ),

                       // reason text
                       () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );
        }


        @Test
        void utilty_abstractClass_returnFalse()
        {
            // --- when
            final boolean isUtility = ValidateUtilityClass.isProperUtilityClass( AbstractForFailure.class, reason );

            final String reasonString = reason.toString();
            // preview feature in Java 21 (removed from Java 23) - Template processors
            // STR."Some text \{ variable } and more later"
            // --- then
            assertAll( () -> assertFalse( isUtility ), // basic return code
                       () -> assertTrue( StringUtility.inAnyOf( logCaptor.getLogs(),
                                                                "ValidateUtilityClassTest$AbstractForFailure" ) ),
                       () -> assertTrue( StringUtility.inAnyOf( logCaptor.getLogs(),
                                                                "InstantiationException from a utility constructor" ) ),

                       // reason text
                       () -> assertTrue( 0 <= reasonString.indexOf( "ValidateUtilityClassTest$AbstractForFailure" ),
                                         "Missing class name 'AbstractForFailure'" ),
                       () -> assertTrue( 0 <= reasonString.indexOf( "InstantiationException from a utility constructor" ),
                                         "Missing reason 'InstantiationException'" ) );
        }


        @Test
        void utilty_methodsAllStatic_returnTrue()
        {
            assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( IndividualCriteria
                                                                                            .ClassCriteriaMethods
                                                                                            .MethodsAllStatic.class, reason ) ),
                       // log output
                       () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                       // reason text
                       () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );

        }



        @Test
        void utilty_methodsNotAllStatic_returnFalse()
        {
            // --- when
            final boolean      isUtility =
                    ValidateUtilityClass
                            .isProperUtilityClass( IndividualCriteria.ClassCriteriaMethods.MethodsNotAllStatic.class, reason );
            final List<String> debugLogs = logCaptor.getLogs();

            // --- then
            assertAll( () -> assertFalse( isUtility ),
                       // log output
                       () -> assertTrue( StringUtility.inAnyOf( debugLogs, "A non-static method" ),
                                         "Non-static method was not detected" ),
                       () -> assertTrue( StringUtility.inAnyOf( debugLogs, "MethodsNotAllStatic.notStatic()' exists" ),
                                         "Identify the specific non-static method" ),
                       // reason text
                       () -> assertTrue( 0 <= reason.toString().indexOf( "A non-static method '" ),
                                         "non-static method was not identified'" ),
                       () -> assertTrue( 0 <= reason.toString().indexOf( "MethodsNotAllStatic.notStatic()' exists" ),
                                         "method not identified'" ) );
        }


    }





    private boolean isReasonBlank( final StringJoiner reason )
    {
        final String text = reason.toString();

        return "[]".equals( text );
    }

}
