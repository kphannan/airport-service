/* (C) 2025 */

package com.example.airline.location.service;


import java.util.Optional;

import com.example.airline.location.Airport;
import com.example.airline.location.mapper.AirportMapper;
import com.example.airline.location.persistence.model.airport.AirportEntity;
import com.example.airline.location.persistence.repository.airport.AirportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;



@Component
// @RefreshScope
public class AirportService
{
    private final AirportRepository repository;

    private final AirportMapper mapper;

    public AirportService( final AirportRepository repository, final AirportMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    public Page<Airport> findAll( final Pageable pageable )
    {
        final Page<AirportEntity> airports = repository.findAll( pageable );

        return airports.map( mapper::airportEntityToDomain );
    }



    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Airport> findAirportById( final Long id )
    {
        final Optional<AirportEntity> airportEntity = repository.findAirportById( id );

        return mapEntityToDomain( airportEntity );
    }



    public Optional<Airport> findAirportByIdent( final String code )
    {
        final Optional<AirportEntity> airportEntity = repository.findAirportByIdent( code );

        return mapEntityToDomain( airportEntity );
    }



    public Page<Airport> advancedQuery( final String iataCode,
                                        final String icaoCode,
                                        final String ident,
                                        final String name,
                                        final Pageable paging )
    {
        final Page<AirportEntity> entities = repository.advancedQuery( iataCode, icaoCode, ident, name, paging );

        return entities.map( mapper::airportEntityToDomain );
    }



    private Optional<Airport> mapEntityToDomain( Optional<AirportEntity> entity )
    {
        if ( entity.isPresent() )
        {
            return Optional.of( mapper.airportEntityToDomain( entity.get() ) );
        }

        return Optional.ofNullable( null );
    }

}
