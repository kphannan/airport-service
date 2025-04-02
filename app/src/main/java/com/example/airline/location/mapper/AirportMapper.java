/* (C) 2025 */

package com.example.airline.location.mapper;


import java.util.List;

import com.example.airline.airport.AirportDTO;
import com.example.airline.location.Airport;
import com.example.airline.location.persistence.model.airport.AirportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



@Mapper( componentModel = "spring" )
public interface AirportMapper
{
    AirportMapper INSTANCE = Mappers.getMapper( AirportMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Instance
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    Airport airportEntityToDomain( AirportEntity entity );

    // --- Collection
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    List<Airport> airportEntityToDomain( List<AirportEntity> entities );

    // ------------------------
    // ----- Domain / API -----
    // Domain --> API
    AirportDTO airportDomainToApi( Airport airport );

    // API --> Domain
    // --- n/a
    // --- Collection
    // Domain --> API
    List<AirportDTO> airportDomainToApi( List<Airport> airports );

    // API --> Domain
}
