/* (C) 2025 */

package com.example.airline.location.airport.mapper;


import java.util.List;

import com.example.airline.airport.AirportCountInContinentDTO;
import com.example.airline.airport.AirportCountInCountryDTO;
import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
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
    AirportCountInContinentDTO domainToApi( AirportCountInContinent airport );

    // --- Collection
    /** Map a list of domain instances to a list of API instances. */
    List<AirportDTO> domainToApi( List<Airport> airports );
    List<AirportCountInContinentDTO> domainToApiAirportsInContinent( List<AirportCountInContinent> entities );
    List<AirportCountInCountryDTO> domainToApiAirportsInCountry( List<AirportCountInCountry> entities );

    // --- API --> Domain ---
    // --- Instance
    // --- Collection
}
