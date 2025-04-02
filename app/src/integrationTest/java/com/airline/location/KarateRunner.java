package com.airline.location;

import com.intuit.karate.junit5.Karate;

public class KarateRunner {

    @Karate.Test
    public Karate fullRegression() {
        System.out.println( "Run Karate Integration Tests");
        return new Karate().relativeTo(getClass());
    }
}