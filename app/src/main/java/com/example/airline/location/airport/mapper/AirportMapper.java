/* (C) 2025 */

package com.example.airline.location.airport.mapper;


import java.util.List;

import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.persistence.model.airport.AirportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * MapStruct configuration for Airport, AirportEntity, AirportDTO.
 */
@Mapper( componentModel = "spring" )
public interface AirportMapper
{
//    AirportMapper INSTANCE = Mappers.getMapper( AirportMapper.class );

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

    // ------------------------
    // ----- Domain / API -----
    // --- Domain --> API ---
    // --- Instance
    /** Map a domain instance to an API instance. */
    AirportDTO domainToApi( Airport airport );

    // --- Collection
    /** Map a list of domain instances to a list of API instances. */
    List<AirportDTO> domainToApi( List<Airport> airports );

    // --- API --> Domain ---
    // --- Instance
    // --- Collection
}
