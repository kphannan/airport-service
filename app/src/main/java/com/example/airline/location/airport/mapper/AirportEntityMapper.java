/* (C) 2025 */

package com.example.airline.location.airport.mapper;


import java.util.List;

import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import org.mapstruct.Mapper;


/**
 * MapStruct configuration for Airport, AirportEntity.
 */
@Mapper( componentModel = "spring" )
public interface AirportEntityMapper
{
    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Persistence --> Domain ---
    // --- Instance
    /** Map a single db entity instance to a domain instance. */
    Airport entityToDomain( AirportEntity entity );

    // --- Collection
    /** Map a list of domain instances to a list of db entity instances. */
    List<Airport> entityToDomain( List<AirportEntity> entities );

    // --- Domain --> Persistence ---
    // --- Instance
    // --- Collection
}
