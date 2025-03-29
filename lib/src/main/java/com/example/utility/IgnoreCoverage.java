/* (C)2024 */

package com.example.utility;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;



/**
 * Custom annotation which can be applied to a class or method to ignore the
 * annotated element from code coverage analysis. Use only when absolutely
 * necessary. For example on Application.main()
 */
@Documented
@Retention( RUNTIME )
@Target( { TYPE, METHOD } )
public @interface IgnoreCoverage
{
    // no content needed
}
