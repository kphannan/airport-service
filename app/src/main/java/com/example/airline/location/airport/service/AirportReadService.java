/* (C) 2025 */

package com.example.airline.location.airport.service;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.airport.mapper.AirportEntityMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInRegionEntity;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import com.example.airline.location.airport.persistence.model.AirportSummaryEntity;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



/**
 * Spring Service (business logic) supporting the {@code Airport} domain object.
 */
@Service
@Log4j2
public class AirportReadService
{
    private final AirportRepository repository;

    private final AirportEntityMapper mapper;

    /**
     * Create an AirportService supported by autowire.
     *
     * @param repository jpa repository of Airports
     * @param mapper maps entities to/from the domain model
     */
    public AirportReadService( final AirportRepository repository, final AirportEntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    /**
     * Retrieve all {@code Airport} records by {@code Page}.
     *
     * @param pageable the {@code Page} criteria.
     *
     * @return the located page, whose body contains the found records.
     */
    public Page<Airport> findAll( final Pageable pageable )
    {
        final Page<AirportEntity> airports = repository.findAll( pageable );

        return airports.map( mapper::entityToDomain );
    }



    /**
     * Lookup a {@code Airport} by its unique identifier.
     *
     * @param id the unique identifier.
     *
     * @return the record if found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Airport> findAirportById( final Long id )
    {
        final Optional<AirportEntity> airportEntity = repository.findById( id );

        return mapEntityToDomain( airportEntity );
    }



    /**
     * Find an airport from its commonly used identifier, often this is the iata
     * airport code.
     *
     * @param code the airport identifier.
     *
     * @return the DB entry for the target airport.
     */
    public Optional<Airport> findAirportByIdent( final String code )
    {
        final Optional<AirportEntity> airportEntity = repository.findByIdent( code );

        return mapEntityToDomain( airportEntity );
    }

    /**
     * Find all airports within a specified Region.
     *
     * @param code the unique identifying code for the desired reqion.
     * @return the list of Airports within the region or an empty list.
     */
    public List<Airport> findAirportsByRegion( final String code )
    {
        final List<AirportEntity> airportEntity = repository.findByIsoRegion( code );

        return mapEntityToDomain( airportEntity );
    }


    // ===== Counts =====
    // --- by Continent ---

    /**
     * Get a list of continents including a count of the number of airports in that continent.
     *
     * @return list of continents with the number of airports on the continent.
     */
    public List<AirportCountInContinent> countAirportsByContinent()
    {
        final List<AirportCountInContinentEntity> entities = repository.countAirportsByContinent();

        return mapper.entityToDomainAirportsInContinent( entities );
    }


    public List<AirportCountInCountry> countCountryAirportsByContinent( final String countryCode )
    {
        return mapper.entityToDomainAirportsInCountry( repository.countCountryAirportsByContinent( countryCode ) );
    }

    // --- by Country ---

    /**
     * Get the number of airports in a country.
     *
     * @param countryCode unique ISO 3166 code of the country.
     * @return a collection of Airport count in a country
     */
    public List<AirportCountInCountry> countAirportsByCountry( final String countryCode )
    {
        return mapper.entityToDomainAirportsInCountry( repository.countAirportsByCountry( countryCode ) );
    }

    /**
     * Get the number of airports by region within a country.
     *
     * @param countryCode  the target country.
     * @return list of counts of airports in a region.
     */
    public List<AirportCountInRegion> countRegionAirportsByCountry( final String countryCode )
    {
        return mapper.entityToDomainAirportsInRegion( repository.countRegionAirportsByCountry( countryCode ) );
    }

    // --- by Region ---

    /**
     * Get the number of airports in a specific region.
     *
     * @param regionCode the desired region
     * @return list of airport counts.
     */
    public List<AirportCountInRegion> countAirportsByRegion( final String regionCode )
    {
        final List<AirportCountInRegionEntity> entities = repository.countAirportsByRegion( regionCode );

        return mapper.entityToDomainAirportsInRegion( entities );
    }


    // ===== Counts =====
    // --- by Continent ---
    public List<AirportSummaryEntity> findSummaryByContinent( String continent )
    {
        return List.of();
    }

    // --- by Country ---
    public List<AirportSummaryEntity> findSummaryByCountry( String isoCountry )
    {
        return List.of();
    }

    // --- by Region ---
    public List<AirportSummaryEntity> findSummaryByRegion( String isoRegion )
    {
        return List.of();
    }




    /**
     * Search for {@code Airport} records that contain any of the query parameters.
     *
     * @param iataCode optional IATA code to search on.
     * @param icaoCode optional ICAO code to search on.
     * @param ident    optional identifier which may be the ICAO code to search on.
     * @param name     optional airport name string.
     * @param paging   current {@code Page} specification.
     *
     * @return the target page with {@code Airport} records if any match the
     *         criteria.
     */
    public Page<Airport> advancedQuery( final String iataCode,
                                        final String icaoCode,
                                        final String ident,
                                        final String name,
                                        final Pageable paging )
    {
        final String criteriaIataCode = null == iataCode ? "" : iataCode.toUpperCase();
        final String criteriaIcaoCode = null == icaoCode ? "" : icaoCode.toUpperCase();
        final String criteriaIdent    = null == ident    ? "" : ident.toUpperCase();
        final String criteriaName     = null == name     ? "" : name.toUpperCase();

        final Page<AirportEntity> entities = repository.advancedQuery( criteriaIataCode,
                                                                       criteriaIcaoCode,
                                                                       criteriaIdent,
                                                                       criteriaName,
                                                                       paging );

        // TODO should probably think about handling null here though it shouldn't ever happen.
        return entities.map( mapper::entityToDomain );
    }






    private Optional<Airport> mapEntityToDomain( final Optional<AirportEntity> entity )
    {
        if ( entity.isPresent() )
        {
            return Optional.of( mapper.entityToDomain( entity.get() ) );
        }

        return Optional.empty();
    }

    private List<Airport> mapEntityToDomain( final List<AirportEntity> entity )
    {
        return mapper.entityToDomain( entity );
    }
}
