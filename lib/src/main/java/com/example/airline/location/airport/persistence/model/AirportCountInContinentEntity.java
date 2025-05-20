package com.example.airline.location.airport.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import org.jspecify.annotations.NonNull;



public interface AirportCountInContinentEntity
{
    String getContinentCode();
    String getName();
    long   getAirportCount();
}


//public class AirportCountInContinentEntity
//{
//    /**
//     * The code for the continent where the airport is (primarily) located. Allowed
//     * values are "AF" (Africa), "AN" (Antarctica), "AS" (Asia), "EU" (Europe), "NA"
//     * (North America), "OC" (Oceania), or "SA" (South America).
//     */
////    @Column( name = "continent", length = 2, nullable = false, columnDefinition = "char(2)" )
//    @NonNull
//    private String continentCode;
//    private long   airportCount;
//
//    public AirportCountInContinentEntity( final String continentCode, final long airportCount )
//    {
//        this.continentCode = continentCode;
//        this.airportCount  = airportCount;
//    }
//}
