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

    // ----- Class declaration -----

    // --- positive - class is final
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

    @Test
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

    /** Improper utility class: class is not final. */
    @SuppressWarnings( "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal" )
    private static class ClassNotFinal
    {
        @SuppressWarnings( { "PMD.UnnecessaryConstructor" } )
        private ClassNotFinal()
        {
        }
    }

    @Test
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

    // ----- Nature of the constructor -----

    // --- positive - constructor is private



    @Test
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



    @Test
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

    // --- - prevent instantiation

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

    /** Verify correct negative behavior. */
    @Test
    void validate_constructorThrowsCorrectException_returnTrue()
    {
        // --- when
        final boolean isUtility = ValidateUtilityClass.isInstantiationDenied( ConstructorThrowsCorrectException.class,
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
                           + "InvocationTargetException, expecting IllegalStateException]", reason.toString() ) );
    }



    @SuppressWarnings( { "LineLength" } )
    @Test
    void validate_constructorDeniedThrowsBadExceptionMesage_returnTrue()
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
    void validate_constructorDeniedThrowsNullPointerWithMesage_returnTrue()
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

    //

    // --- negative - constructor is not private

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

    @Test
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

    // - has arguments

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

    @Test
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



    @Test
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

    /** Improper utility class: a constructor with arguments. */
    private static final class ConstructorWithArguments
    {
        @SuppressWarnings( "PMD.UnusedFormalParameter" )
        private ConstructorWithArguments( int arg )
        {
            throw new IllegalStateException( "Test exception" );
        }
    }

    @Test
    void validate_constructorWithArgs_returnFalse()
    {
        // --- when
        final boolean isUtility = ValidateUtilityClass.isConstructorPrivate( ConstructorWithArguments.class, reason );

        // --- then
        assertAll( () -> assertFalse( isUtility ),
                   // log output
                   // n/a
                   // reason text
                   () -> assertTrue( 0 <= reason.toString().indexOf( "no-argument constructor is not present" ),
                                     "constructor with arguments not properly detected" ) );
    }



    @Test
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

    // - multiple constructors

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

    @Test
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

    // ============ isProperUtilityClass() ============



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
                   () -> assertTrue( StringUtility.contains( logCaptor.getLogs(), "class must be final" ) ),
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
                   () -> assertTrue( StringUtility.contains( logCaptor.getLogs(),
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
    void utilty_constructorWithArgs_returnFalse()
    {
        // --- when
        final boolean isUtility = ValidateUtilityClass.isProperUtilityClass( ConstructorWithArguments.class, reason );

        // --- then
        assertAll( () -> assertFalse( isUtility ),
                   // log output
                   () -> assertTrue( StringUtility.contains( logCaptor.getLogs(),
                                                             "no-argument constructor is not present" ) ),
                   () -> assertTrue( StringUtility.contains( logCaptor.getLogs(),
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
                   () -> assertTrue( StringUtility.contains( logCaptor.getLogs(),
                                                             "ValidateUtilityClassTest$AbstractForFailure" ) ),
                   () -> assertTrue( StringUtility.contains( logCaptor.getLogs(),
                                                             "InstantiationException from a utility constructor" ) ),

                   // reason text
                   () -> assertTrue( 0 <= reasonString.indexOf( "ValidateUtilityClassTest$AbstractForFailure" ),
                                     "Missing class name 'AbstractForFailure'" ),
                   () -> assertTrue( 0 <= reasonString.indexOf( "InstantiationException from a utility constructor" ),
                                     "Missing reason 'InstantiationException'" ) );
    }



    // assertAllMethodsAreStatic
    @Test
    void utilty_methodsAllStatic_returnTrue()
    {
        assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( MethodsAllStatic.class, reason ) ),
                   // log output
                   () -> assertTrue( logCaptor.getLogs().isEmpty() ),
                   // reason text
                   () -> assertEquals( 2, reason.length(), REASON_NOT_NEEDED ) );

    }



    @Test
    void utilty_methodsNotAllStatic_returnFalse()
    {
        // --- when
        final boolean      isUtility = ValidateUtilityClass.isProperUtilityClass( MethodsNotAllStatic.class, reason );
        final List<String> debugLogs = logCaptor.getLogs();

        // --- then
        assertAll( () -> assertFalse( isUtility ),
                   // log output
                   () -> assertTrue( StringUtility.contains( debugLogs, "A non-static method" ),
                                     "Non-static method was not detected" ),
                   () -> assertTrue( StringUtility.contains( debugLogs, "MethodsNotAllStatic.notStatic()' exists" ),
                                     "Identify the specific non-static method" ),
                   // reason text
                   () -> assertTrue( 0 <= reason.toString().indexOf( "A non-static method '" ),
                                     "non-static method was not identified'" ),
                   () -> assertTrue( 0 <= reason.toString().indexOf( "MethodsNotAllStatic.notStatic()' exists" ),
                                     "method not identified'" ) );
    }



    private boolean isReasonBlank( final StringJoiner reason )
    {
        final String text = reason.toString();

        return "[]".equals( text );
    }

}
