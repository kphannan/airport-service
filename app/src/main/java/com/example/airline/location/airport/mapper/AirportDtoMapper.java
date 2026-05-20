/* (C) 2025 */

package com.example.airline.location.airport.mapper;


import java.util.List;

import com.example.airline.airport.AirportCountInContinentDTO;
import com.example.airline.airport.AirportCountInCountryDTO;
import com.example.airline.airport.AirportCountInRegionDTO;
import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
import com.example.airline.location.airport.model.AirportCountInRegion;
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
    /**
     * Map a domain instance to an API instance.
     *
     * @param airport domain instance of an airport.
     * @return the API representation of an airport.
     */
    AirportDTO domainToApi( Airport airport );

    /**
     * Map a domain instance to a DTO instance.\
     *
     * @param airport the domain instance.
     * @return the DTO version of the domain object.
     */
    AirportCountInContinentDTO domainToApi( AirportCountInContinent airport );

    // --- Collection
    /**
     * Map a list of domain instances to a list of API instances.
     *
     * @param airports list of domain instances of an airport.
     * @return the list of API representations of a airports.
     */
    List<AirportDTO> domainToApi( List<Airport> airports );

    /**
     * Map a list of domain instances to DTO instances.
     *
     * @param entities list of domain instances to map.
     * @return list of DTO instances.
     */
    List<AirportCountInContinentDTO> domainToApiAirportsInContinent( List<AirportCountInContinent> entities );

    /**
     * Map a list of domain instances to DTO instances.
     *
     * @param entities list of domain instances to map.
     * @return list of DTO instances.
     */
    List<AirportCountInCountryDTO> domainToApiAirportsInCountry( List<AirportCountInCountry> entities );

    /**
     * Map a list of domain instances to DTO instances.
     *
     * @param entities list of domain instances to map.
     * @return list of DTO instances.
     */
    List<AirportCountInRegionDTO> domainToApiAirportsInRegion( List<AirportCountInRegion> entities );

    // --- API --> Domain ---
    // --- Instance
    // --- Collection
}
