/* (C) 2025 */

package com.example.airline.location.airport.mapper;


import java.util.List;

import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInCountryEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInRegionEntity;
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

    /** Map a AirportCountInContinentEntity DB entity to a domain instance. **/
    AirportCountInContinent entityToDomain( AirportCountInContinentEntity entity );

    /** Map a AirportCountInCountryEntity DB entity to a domain instance. **/
    AirportCountInCountry entityToDomain( AirportCountInCountryEntity entity );

    /** Map a AirportCountInRegionEntity DB entity to a domain instance. **/
    AirportCountInRegion entityToDomain( AirportCountInRegionEntity entity );

    // --- Collection
    /** Map a list of domain instances to a list of db entity instances. */
    List<Airport> entityToDomain( List<AirportEntity> entities );

    List<AirportCountInContinent> entityToDomainAirportsInContinent( List<AirportCountInContinentEntity> entities );

    List<AirportCountInCountry> entityToDomainAirportsInCountry( List<AirportCountInCountryEntity> entities );

    List<AirportCountInRegion> entityToDomainAirportsInRegion( List<AirportCountInRegionEntity> entities );

    // --- Domain --> Persistence ---
    // --- Instance
    // --- Collection
}
