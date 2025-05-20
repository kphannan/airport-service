package com.example.airline.location.airport.persistence.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

@Data
@AllArgsConstructor
@Builder
public class AirportCountInRegionEntity
{
    /**
     * The code for the continent where the airport is (primarily) located. Allowed
     * values are "AF" (Africa), "AN" (Antarctica), "AS" (Asia), "EU" (Europe), "NA"
     * (North America), "OC" (Oceania), or "SA" (South America).
     */
    @Column( name = "continent", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull
    private String regionCode;
    private long   airportCount;
}
