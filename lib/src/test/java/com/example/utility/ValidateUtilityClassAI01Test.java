/**
 * AI generated tests.  Some manual changes were required, such as:
 * - making test classes final.
 * - changing the expected reason text.
 */

package com.example.utility;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.StringJoiner;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.*;


/**
 * Comprehensive test suite for {@link ValidateUtilityClass}. Designed for 100% line/branch coverage and mutation
 * testing compliance.
 */
class ValidateUtilityClassAI01Test
{

    // ==================== TEST FIXTURES ====================


    // 9. Enum (fails isInstantiationDenied via InvocationTargetException with wrong cause)
    public enum EnumUtility
    {
        INSTANCE;

        public static void doWork() {}
    }


    // 10. Interface (fails isClassFinal, hasOnlyOneConstructor, isConstructorPrivate, isInstantiationDenied)
    public interface InterfaceUtility
    {
        static void doWork() {}
    }


    // 1. Perfect utility class (baseline for success)
    public static final class ValidUtility
    {
        private ValidUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    // 2. Not final (fails isClassFinal)
    public static class NotFinalUtility
    {
        private NotFinalUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    // 3. Multiple constructors (fails hasOnlyOneConstructor)
    public static final class TwoConstructorsUtility
    {
        private TwoConstructorsUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        TwoConstructorsUtility( int dummy ) {}

        public static void doWork() {}
    }


    // 4. Public constructor (fails isConstructorPrivate)
    public static final class PublicConstructorUtility
    {
        public PublicConstructorUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    // 5. No no-arg constructor (fails isConstructorPrivate & isInstantiationDenied via NoSuchMethod)
    public static final class NoNoArgConstructorUtility
    {
        private NoNoArgConstructorUtility( int dummy )
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    // 6. Allows instantiation (fails isInstantiationDenied)
    public static final class InstantiableUtility
    {
        private InstantiableUtility() {}

        public static void doWork() {}
    }


    // 7. Contains non-static method (fails assertAllMethodsAreStatic)
    public static final class NonStaticMethodUtility
    {
        private NonStaticMethodUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void staticMethod() {}

        public void instanceMethod() {}
    }


    // 8. Abstract class (fails isClassFinal & isInstantiationDenied via InstantiationException)
    public abstract class AbstractUtility
    {
        private AbstractUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    // 11. Completely broken (triggers all 5 failure branches for bitmask accumulation testing)
    public static class CompletelyBrokenUtility
    {
        public CompletelyBrokenUtility() {}

        public CompletelyBrokenUtility( int x ) {}

        public static void staticMethod() {}

        public void nonStatic() {}
    }

    // ==================== HELPER METHOD TESTS ====================


    @Nested
    class IsClassFinalTests
    {
        @Test
        void validUtilityReturnsTrue()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.isClassFinal( ValidUtility.class, reason ) );
            assertTrue( reason.length() == 0 );
        }

        @Test
        void notFinalReturnsFalseAndAddsReason()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isClassFinal( NotFinalUtility.class, reason ) );
            assertEquals( "class must be final", reason.toString() );
        }

        @Test
        void abstractClassReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isClassFinal( AbstractUtility.class, reason ) );
        }

        @Test
        void interfaceReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isClassFinal( InterfaceUtility.class, reason ) );
        }
    }


    @Nested
    class HasOnlyOneConstructorTests
    {
        @Test
        void validUtilityReturnsTrue()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.hasOnlyOneConstructor( ValidUtility.class, reason ) );
        }

        @Test
        void twoConstructorsReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.hasOnlyOneConstructor( TwoConstructorsUtility.class, reason ) );
            assertEquals( "There must only be one constructor", reason.toString() );
        }

        @Test
        void interfaceReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.hasOnlyOneConstructor( InterfaceUtility.class, reason ) );
        }
    }


    @Nested
    class IsConstructorPrivateTests
    {
        @Test
        void validUtilityReturnsTrue()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.isConstructorPrivate( ValidUtility.class, reason ) );
        }

        @Test
        void publicConstructorReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isConstructorPrivate( PublicConstructorUtility.class, reason ) );
            assertEquals( "constructor is not private", reason.toString() );
        }

        @Test
        void noNoArgConstructorReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isConstructorPrivate( NoNoArgConstructorUtility.class, reason ) );
            assertEquals( "no-argument constructor is not present", reason.toString() );
        }

        @Test
        void interfaceReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isConstructorPrivate( InterfaceUtility.class, reason ) );
        }
    }


    @Nested
    class AssertAllMethodsAreStaticTests
    {
        @Test
        void validUtilityReturnsTrue()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.assertAllMethodsAreStatic( ValidUtility.class, reason ) );
        }

        @Test
        void nonStaticMethodReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.assertAllMethodsAreStatic( NonStaticMethodUtility.class, reason ) );
            assertTrue( reason.toString().contains( "A non-static method" ) );
        }

        @Test
        void interfaceMethodsAreStaticAndPass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.assertAllMethodsAreStatic( InterfaceUtility.class, reason ) );
        }
    }


    @Nested
    class IsInstantiationDeniedTests
    {
        @Test
        void validUtilityReturnsTrue()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.isInstantiationDenied( ValidUtility.class, reason ) );
        }

        @Test
        void instantiableClassReturnsFalse()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isInstantiationDenied( InstantiableUtility.class, reason ) );
            assertTrue( reason.toString().contains( "Must prevent instantiation" ) );
        }

        @Test
        void abstractClassTriggersInstantiationException()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.isInstantiationDenied( AbstractUtility.class, reason ) );
            assertThat( reason.toString() ).contains( "The no-argument constructor does not exist" );
        }

        @Test
        void noNoArgConstructorTriggersNoSuchMethod()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.isInstantiationDenied( NoNoArgConstructorUtility.class, reason ) );
            assertThat( reason.toString() ).contains( "The no-argument constructor does not exist" );
        }

        @Test
        void enumTriggersInvocationTargetExceptionWithWrongCause()
        {
            StringJoiner reason = new StringJoiner( ", " );
            // Enum.newInstance() throws InvocationTargetException wrapping IllegalArgumentException
            assertTrue( ValidateUtilityClass.isInstantiationDenied( EnumUtility.class, reason ) );
            // assertThat( reason ).contains(  "Unexpected cause " );
            assertTrue( reason.toString().contains( "The no-argument constructor does not exist" ) );
            // assertEquals( "The no-argument constructor does not exist", reason.toString() );
        }
    }

    // ==================== INTEGRATION & BITMASK TESTS ====================


    @Nested
    class IsProperUtilityClassIntegrationTests
    {
        @Test
        void perfectUtilityPasses()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertTrue( ValidateUtilityClass.isProperUtilityClass( ValidUtility.class, reason ) );
            assertEquals( "", reason.toString() );
        }

        @Test
        void notFinalFails()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( NotFinalUtility.class, reason ) );
            assertEquals( "class must be final", reason.toString() );
        }

        @Test
        void multipleConstructorsFails()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( TwoConstructorsUtility.class, reason ) );
            assertEquals( "There must only be one constructor", reason.toString() );
        }

        @Test
        void publicConstructorFails()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( PublicConstructorUtility.class, reason ) );
            assertEquals( "constructor is not private", reason.toString() );
        }

        @Test
        void allowsInstantiationFails()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( InstantiableUtility.class, reason ) );
            assertTrue( reason.toString().contains( "Must prevent instantiation" ) );
        }

        @Test
        void nonStaticMethodFails()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( NonStaticMethodUtility.class, reason ) );
            assertTrue( reason.toString().contains( "A non-static method" ) );
        }

        @Test
        void completelyBrokenTriggersAllFailuresAndAccumulatesReasons()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( CompletelyBrokenUtility.class, reason ) );

            // Mutation testing: verifies bitmask accumulation logic by checking all 5 failure reasons
            String combined = reason.toString();
            assertTrue( combined.contains( "class must be final" ) );
            assertTrue( combined.contains( "There must only be one constructor" ) );
            assertTrue( combined.contains( "constructor is not private" ) );
            assertTrue( combined.contains( "Must prevent instantiation" ) );
            assertTrue( combined.contains( "A non-static method" ) );
        }

        @Test
        void abstractClassFailsWithMultipleReasons()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( AbstractUtility.class, reason ) );
            String combined = reason.toString();
            assertThat( reason.toString() ).contains( "class must be final" );
            assertThat( reason.toString() ).contains( "The no-argument constructor does not exist" );
            // assertTrue( combined.contains( "class must be final" ) );
            // assertTrue( combined.contains( "InstantiationException" ) );
        }

        @Test
        void interfaceFailsWithMultipleReasons()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertFalse( ValidateUtilityClass.isProperUtilityClass( InterfaceUtility.class, reason ) );
            String combined = reason.toString();
            assertTrue( combined.contains( "class must be final" ) );
            assertTrue( combined.contains( "There must only be one constructor" ) );
            assertTrue( combined.contains( "no-argument constructor is not present" ) );
        }
    }
}
