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

    /**
     * Map Airport counts by continent persistence layer entities to service layer entities.
     *
     * @param entities list of persistence entities
     * @return equivalent list of service layer entities.
     */
    List<AirportCountInContinent> entityToDomainAirportsInContinent( List<AirportCountInContinentEntity> entities );

    /**
     * Map Airport counts within a country persistence layer entities to service layer entities.
     *
     * @param entities list of persistence entities
     * @return equivalent list of service layer entities.
     */
    List<AirportCountInCountry> entityToDomainAirportsInCountry( List<AirportCountInCountryEntity> entities );

    /**
     * Map airport counts in a region persistence layer entities to service layer entities.
     *
     * @param entities list of persistence entities
     * @return equivalent list of service layer entities.
     */
    List<AirportCountInRegion> entityToDomainAirportsInRegion( List<AirportCountInRegionEntity> entities );

    // --- Domain --> Persistence ---
    // --- Instance

    /**
     * Map a service layer {@see Airport} instance to an equivalent {@see AirportEntity}.
     *
     * @param entity the {@see Airport} domain instance
     * @return the {@see AirportEntity} persistence layer instance.
     */
    AirportEntity domainToEntity( Airport entity );
    // --- Collection
}
