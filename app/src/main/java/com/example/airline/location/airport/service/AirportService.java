/* (C) 2025 */

package com.example.airline.location.airport.service;


import java.util.Optional;

import com.example.airline.location.airport.mapper.AirportEntityMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



/**
 * Spring Service (business logic) supporting the {@code Airport} domain object.
 */
@Service
public class AirportService
{
    private final AirportRepository repository;

    private final AirportEntityMapper mapper;

    /**
     * Create a AirportService supported by autowire.
     *
     * @param repository jpa repository of Airports
     * @param mapper maps entities to/from the domain model
     */
    public AirportService( final AirportRepository repository, final AirportEntityMapper mapper )
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
        final Page<AirportEntity> entities = repository.advancedQuery( iataCode, icaoCode, ident, name, paging );
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

}
