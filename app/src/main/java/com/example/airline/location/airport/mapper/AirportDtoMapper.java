/* (C) 2025 */

package com.example.airline.location.airport.mapper;


import java.util.List;

import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.model.Airport;
import org.mapstruct.Mapper;


/**
 * MapStruct configuration for Airport, AirportDTO.
 */
@Mapper( componentModel = "spring" )
public interface AirportDtoMapper
{
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
