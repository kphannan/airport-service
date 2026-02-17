/* (C) 2025 */

package com.airline.location;


import com.intuit.karate.junit5.Karate;
//import org.junit.jupiter.api.Test;


public class KarateRunner
{
//    @Test
//    public void foo()
//    {
//
//    }
     @Karate.Test
     public Karate fullRegression()
     {
         System.out.println( "Run Karate Integration Tests" );
         return new Karate().relativeTo( getClass() );
     }
}
