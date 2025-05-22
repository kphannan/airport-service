package com.example.airline.location.airport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.NonNull;


@Data
@AllArgsConstructor
public class AirportCountInRegion
{
    /**
     * The code for the continent where the airport is (primarily) located. Allowed
     * values are "AF" (Africa), "AN" (Antarctica), "AS" (Asia), "EU" (Europe), "NA"
     * (North America), "OC" (Oceania), or "SA" (South America).
     */
    @NonNull
    private String regionCode;
    private String name;
    private long   airportCount;
}
