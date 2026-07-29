/**
 * AI generated tests.  Some manual changes were required, such as:
 * - making test classes final.
 * - changing the expected reason text.
 */

package com.example.utility;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.StringJoiner;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for {@link ValidateUtilityClass}. Designed for 100% line/branch coverage and mutation
 * testing compliance.
 *
 * <p>Note: Placed in the same package to access package-private helper methods.
 */
class ValidateUtilityClassAI02Test
{

    // ==================== TEST FIXTURES ====================


    // @Test
    // void constructorPreventsInstantiation()
    // {
    //     assertThatThrownBy( () -> new ValidateUtilityClass() )
    //         .isInstanceOf( IllegalStateException.class )
    //         .hasMessage( "Utility classes may not be instantiated" );
    // }


    public enum EnumUtility
    {
        INSTANCE;

        public static void doWork() {}
    }


    public interface InterfaceUtility
    {
        static void doWork() {}
    }


    public static final class ValidUtility
    {
        private ValidUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    public static class NotFinalUtility
    {
        private NotFinalUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    public static final class MultiCtorUtility
    {
        private MultiCtorUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        MultiCtorUtility( int x ) {}

        public static void doWork() {}
    }


    public static final class PublicCtorUtility
    {
        public PublicCtorUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    public static final class NoNoArgCtorUtility
    {
        private NoNoArgCtorUtility( int x )
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    public static final class InstantiableUtility
    {
        private InstantiableUtility() {}

        public static void doWork() {}
    }


    public static abstract class AbstractUtility
    {
        private AbstractUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void doWork() {}
    }


    public static final class NonStaticMethodUtility
    {
        private NonStaticMethodUtility()
        {
            throw new IllegalStateException( "Utility class" );
        }

        public static void staticMethod() {}

        public void instanceMethod() {}
    }

    // ==================== CONSTRUCTOR TEST ====================


    public static class BrokenUtility
    {
        public BrokenUtility() {}

        public BrokenUtility( int x ) {}

        public static void staticMethod() {}

        public void instanceMethod() {}
    }

    // ==================== HELPER METHOD TESTS ====================


    @Nested
    class IsClassFinal
    {
        @Test
        void validUtility()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isClassFinal - valid",
                       () -> assertThat( ValidateUtilityClass.isClassFinal( ValidUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).isEmpty()
            );
        }

        @Test
        void notFinal()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isClassFinal - not final",
                       () -> assertThat( ValidateUtilityClass.isClassFinal( NotFinalUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "class must be final" )
            );
        }

        @Test
        void abstractClass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertThat( ValidateUtilityClass.isClassFinal( AbstractUtility.class, reason ) )
                .isFalse();
        }

        @Test
        void interfaceClass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertThat( ValidateUtilityClass.isClassFinal( InterfaceUtility.class, reason ) )
                .isFalse();
        }
    }


    @Nested
    class HasOnlyOneConstructor
    {
        @Test
        void validUtility()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "hasOnlyOneConstructor - valid",
                       () -> assertThat( ValidateUtilityClass.hasOnlyOneConstructor( ValidUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).isEmpty()
            );
        }

        @Test
        void multipleConstructors()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "hasOnlyOneConstructor - multiple",
                       () -> assertThat( ValidateUtilityClass.hasOnlyOneConstructor( MultiCtorUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "There must only be one constructor" )
            );
        }

        @Test
        void interfaceClass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertThat( ValidateUtilityClass.hasOnlyOneConstructor( InterfaceUtility.class, reason ) )
                .isFalse();
        }
    }


    @Nested
    class IsConstructorPrivate
    {
        @Test
        void validUtility()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isConstructorPrivate - valid",
                       () -> assertThat( ValidateUtilityClass.isConstructorPrivate( ValidUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).isEmpty()
            );
        }

        @Test
        void publicConstructor()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isConstructorPrivate - public",
                       () -> assertThat( ValidateUtilityClass.isConstructorPrivate( PublicCtorUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "constructor is not private" )
            );
        }

        @Test
        void noNoArgConstructor()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isConstructorPrivate - no no-arg",
                       () -> assertThat( ValidateUtilityClass.isConstructorPrivate( NoNoArgCtorUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "no-argument constructor is not present" )
            );
        }

        @Test
        void interfaceClass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertThat( ValidateUtilityClass.isConstructorPrivate( InterfaceUtility.class, reason ) )
                .isFalse();
        }
    }


    @Nested
    class AssertAllMethodsAreStatic
    {
        @Test
        void validUtility()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "assertAllMethodsAreStatic - valid",
                       () -> assertThat( ValidateUtilityClass.assertAllMethodsAreStatic( ValidUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).isEmpty()
            );
        }

        @Test
        void nonStaticMethod()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "assertAllMethodsAreStatic - non-static",
                       () -> assertThat( ValidateUtilityClass
                                             .assertAllMethodsAreStatic( NonStaticMethodUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "A non-static method" )
            );
        }

        @Test
        void interfaceClass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertThat( ValidateUtilityClass.assertAllMethodsAreStatic( InterfaceUtility.class, reason ) )
                .isTrue();
        }
    }


    @Nested
    class IsInstantiationDenied
    {
        @Test
        void validUtility()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isInstantiationDenied - valid",
                       () -> assertThat( ValidateUtilityClass.isInstantiationDenied( ValidUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).isEmpty()
            );
        }

        @Test
        void instantiable()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isInstantiationDenied - instantiable",
                       () -> assertThat( ValidateUtilityClass.isInstantiationDenied( InstantiableUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "Must prevent instantiation" )
            );
        }

        @Test
        void abstractClass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isInstantiationDenied - abstract",
                       () -> assertThat( ValidateUtilityClass.isInstantiationDenied( AbstractUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).contains( "InstantiationException from a utility constructor" )
            );
        }

        @Test
        void noNoArgConstructor()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isInstantiationDenied - no no-arg",
                       () -> assertThat( ValidateUtilityClass.isInstantiationDenied( NoNoArgCtorUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).contains( "The no-argument constructor does not exist" )
            );
        }

        @Test
        void enumClass()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isInstantiationDenied - enum",
                       () -> assertThat( ValidateUtilityClass.isInstantiationDenied( EnumUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).contains( "The no-argument constructor does not exist" )
            );
        }
    }

    // ==================== INTEGRATION TESTS ====================


    @Nested
    class IsProperUtilityClass
    {
        @Test
        void validUtility()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - valid",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( ValidUtility.class, reason ) )
                                 .isTrue(),
                       () -> assertThat( reason.toString() ).isEmpty()
            );
        }

        @Test
        void notFinal()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - not final",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( NotFinalUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "class must be final" )
            );
        }

        @Test
        void multipleConstructors()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - multiple ctors",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( MultiCtorUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "There must only be one constructor" )
            );
        }

        @Test
        void publicConstructor()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - public ctor",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( PublicCtorUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "constructor is not private" )
            );
        }

        @Test
        void instantiable()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - instantiable",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( InstantiableUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "Must prevent instantiation" )
            );
        }

        @Test
        void nonStaticMethod()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - non-static method",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( NonStaticMethodUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "A non-static method" )
            );
        }

        @Test
        void brokenUtilityTriggersAllFailures()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - broken",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( BrokenUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() )
                                 .contains( "class must be final",
                                            "There must only be one constructor",
                                            // "no-argument constructor is not present",
                                            // "The no-argument constructor does not exist",
                                            "constructor is not private",
                                            "Must prevent instantiation",
                                            "A non-static method",
                                            "instanceMethod()\' exists" )
            );
        }

        @Test
        void abstractUtility()
        {
            StringJoiner reason = new StringJoiner( ", " );
            assertAll( "isProperUtilityClass - abstract",
                       () -> assertThat( ValidateUtilityClass.isProperUtilityClass( AbstractUtility.class, reason ) )
                                 .isFalse(),
                       () -> assertThat( reason.toString() ).contains( "class must be final",
                                                                       "InstantiationException from a utility constructor"
                                                                     )
            );
        }
    }
}
