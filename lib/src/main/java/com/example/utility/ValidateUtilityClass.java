/* (C)2024 */

package com.example.utility;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.StringJoiner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO examine parent classes...
// a true utility can not be used as a parent (it is final)
// does there need to be a variant of this check to allow for a non-final class
// if it is a parent class

// internal methods would be better as private, but that makes testing them
// more difficult as they are only accessible via the one public method and
// can not be exercised sufficiently to survive mutation tests.



/**
 * Utility classes are supposed to be static and should not be instantiated.
 */
@SuppressWarnings( { "PMD.AvoidAccessibilityAlteration" } )
public final class ValidateUtilityClass
{
    // Bit patterns for quick evaluation of criteria to be a utility class
    // see {@code isProperUtilityClass}
    private static final int RESULT_BITS = 0b11111111111111111111;
    // 1 3 2 6 7 // success bit codes
    private static final int TARGET_MASK = 0b00010011001001100111; // success bit mask

    private static final Logger log = LogManager.getLogger( ValidateUtilityClass.class );

    private ValidateUtilityClass()
    {
        // Instances are not allowed for utility classes
        throw new IllegalStateException( "Utility classes may not be instantiated" );
    }



    /**
     * Examine elements of the class declaration to ensure it properly implements a
     * utility class.
     *
     * <ul>
     * <li>All methods are static</li>
     * <li>private no argument constructor</li>
     * <li>only one constructor</li>
     * <li>class is final</li>
     * </ul>
     *
     * @param clazz  the class to test for proper implementation
     * @param reason on a false response, this contains a list of reasons why the
     *               class does not meet the criteria to be a 'utility' class.
     *
     * @return true if the class meets the requirements of a utility class, false
     *         otherwise.
     *
     * @throws IllegalArgumentException    if the number of actual and formal
     *                                     parameters differ; if an unwrapping
     *                                     conversion for primitive arguments fails;
     *                                     or if, after possible unwrapping, a
     *                                     parameter value cannot be converted to
     *                                     the corresponding formal parameter type
     *                                     by a method invocation conversion; if
     *                                     this constructor pertains to an enum
     *                                     class.
     * @throws ExceptionInInitializerError if the initialization provoked by this
     *                                     method fails.
     */
    public static boolean isProperUtilityClass( final Class<?> clazz,
                                                final StringJoiner reason )
    {
        // isUtility &= assertAllMethodsAreStatic( clazz, reason );
        // int mask = 0;
        // System.out.println( String.format( "0: %s %x %d", intToString( mask, 4, 20 ),
        // mask, mask ) );
        int mask = isClassFinal( clazz, reason ) ? 0b0001 : 0b0101; // 1 or 5
        // System.out.println( String.format( "1: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask <<= 4;
        // System.out.println( String.format( "2: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask |= ( hasOnlyOneConstructor( clazz, reason ) ? 0b0011 : 0b0100 ); // 3 or 4
        // System.out.println( String.format( "3: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask <<= 4;
        // System.out.println( String.format( "4: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask |= ( isConstructorPrivate( clazz, reason ) ? 0b0010 : 0b1100 ); // 2 or 12(C)
        // System.out.println( String.format( "5: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask <<= 4;
        // System.out.println( String.format( "6: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask |= ( isInstantiationDenied( clazz, reason ) ? 0b0110 : 0b1101 ); // 6 or 13(D)
        // System.out.println( String.format( "7: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask <<= 4;
        // System.out.println( String.format( "8: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );
        mask |= ( assertAllMethodsAreStatic( clazz, reason ) ? 0b0111 : 0b1111 ); // 7 or 15(F)
        // System.out.println( String.format( "9: %s %x %d\"", intToString( mask, 4, 20
        // ), mask, mask ) );

        // XOR will turn off all the desired bits, the AND strips upper irrelevant bits
        final boolean isUtility = ( ( mask ^ TARGET_MASK ) & RESULT_BITS ) == 0;
        // final int reasonCode = ( -1 ^ ( mask ^ TARGET_MASK ) );
        final int reasonCode = mask ^ TARGET_MASK;

        if ( !isUtility )
        {
            log.error( () -> String.format( "%s : %s code: %x", clazz.toString(), reason.toString(), reasonCode ) );
        }

        return isUtility;
    }

    /*
     * A: 0101 0011 0010 0110 1111 bits B: 0001 0011 0010 0110 0111 expected C: 1011
     * 1111 1111 1111 0111 : 0100 0000 0000 0000 1000
     *
     *
     * bits 0101 0011 0010 0110 1111 = ~bits 1010 1100 1101 1001 0000 desired 0001
     * 0011 0010 0110 0111 ~desired 1110 1100 1101 1001 1000
     * ------------------------------------------------------------- x = ~bits ^
     * desired 1011 1111 1111 1111 0111 = x ^ ~desired 0101 0011 0010 0110 1111
     *
     * 0100 0000 0000 0000 1000 0100 0000 0000 0000 1000
     *
     *
     *
     * 0101 bits 1111 0011 1010 = ~bits 0000 1100 0001 desired 0111 0011 1110
     * ~desired 1000 1100 x 1011 = ~bits ^ desired 0111 1111 0101 = x ^ ~desired
     * 1111 0011
     */



    /* default */
    static boolean isClassFinal( final Class<?> clazz,
                                 final StringJoiner reason )
    {
        final boolean isFinal = Modifier.isFinal( clazz.getModifiers() );
        if ( !isFinal )
        {
            reason.add( "class must be final" );
        }

        return isFinal;
    }



    /* default */
    static boolean hasOnlyOneConstructor( final Class<?> clazz,
                                          final StringJoiner reason )
    {
        final boolean hasSingleConstructor = 1 == clazz.getDeclaredConstructors().length;

        if ( !hasSingleConstructor )
        {
            reason.add( "There must only be one constructor" );
        }

        return hasSingleConstructor;
    }



    /* default */
    static boolean assertAllMethodsAreStatic( final Class<?> clazz,
                                              final StringJoiner reason )
    {
        boolean hasAllStaticMethods = true;

        for ( final Method method : clazz.getMethods() )
        {
            // equals(), hashCode(), toString(), getClass(), notify(), notifyAll(), wait()
            // Methods inherited from Object are exempt from being static.
            // if ( !method.getDeclaringClass().equals( clazz ) )
            if ( method.getDeclaringClass() != clazz )
            {
                continue;
            }

            final boolean isMethodStatic = Modifier.isStatic( method.getModifiers() );
            hasAllStaticMethods &= isMethodStatic;
            if ( !isMethodStatic )
            {
                reason.add( String.format( "A non-static method '%s' exists", method ) );
            }
        }

        return hasAllStaticMethods;
    }



    /* default */
    static boolean isConstructorPrivate( final Class<?> clazz,
                                         final StringJoiner reason )
    {
        try
        {
            final Constructor<?> constructor          = clazz.getDeclaredConstructor();
            final boolean        isConstructorPrivate = Modifier.isPrivate( constructor.getModifiers() );

            if ( !isConstructorPrivate )
            {
                reason.add( "constructor is not private" );
            }

            return isConstructorPrivate;
        }
        catch ( NoSuchMethodException | SecurityException e )
        {
            reason.add( "no-argument constructor is not present" );
        }

        return false;
    }



    /**
     * Determine proper implementation of a constructor in a utility class.
     *
     * @param clazz  the class under test
     * @param reason accumulated list of discovered faults
     *
     * @return true if there is a no-argument constructor, false otherwise.
     */
    @SuppressWarnings( { "PMD.UnusedPrivateField" } )
    /* default */
    static boolean isInstantiationDenied( final Class<?> clazz,
                                          final StringJoiner reason )
    {
        Object instance = null;

        try
        {
            final Constructor<?> constructor = clazz.getDeclaredConstructor();

            constructor.setAccessible( true );

            instance = constructor.newInstance();

            // If the class is instantiated, the is an error. Explain why.
            reason.add( String.format( "'%s': Must prevent instantiation", clazz.getName() ) );
        }
        catch ( NoSuchMethodException ex )
        {
            reason.add( String.format( "'%s': The no-argument constructor does not exist", clazz.getName() ) );
        }
        // catch ( IllegalStateException | IllegalAccessException ex )
        catch ( IllegalAccessException ex )
        {
            reason.add( String.format( "%s with cause '%s'", ex.getClass().getName(),
                                       null == ex.getCause() ? "" : ex.getCause().toString() ) );
        }
        catch ( InstantiationException ex )
        {
            // Thrown when attempting to instantiate an abstract class.
            reason.add( String.format( "'%s': InstantiationException from a utility constructor", clazz.getName() ) );
        }
        catch ( InvocationTargetException ex )
        {
            // Expected exception

            if ( ex.getCause().getClass() != IllegalStateException.class )
            {
                reason.add( String
                        .format( "Unexpected cause '%s' for InvocationTargetException, expecting IllegalStateException",
                                 ex.getCause().toString() ) );

                return true; // force an error condition
            }
        }

        return null == instance;
    }

}
