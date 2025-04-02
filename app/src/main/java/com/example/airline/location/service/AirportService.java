/* (C)2025 */

package com.example.airline.location.service;


import java.util.Locale;
import java.util.Optional;

import com.example.airline.location.Airport;
import com.example.airline.location.mapper.AirportMapper;
import com.example.airline.location.persistence.model.airport.AirportEntity;
import com.example.airline.location.persistence.repository.airport.AirportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Component
// @RefreshScope
public class AirportService
{
    private AirportRepository repository;

    private AirportMapper     mapper;



    public AirportService( AirportRepository repository, AirportMapper mapper )
    {
        this.repository = repository;
        this.mapper = mapper;
    }



    public Page<Airport> findAll( final Pageable pageable )
    {
        Page<AirportEntity> airports = repository.findAll( pageable );

        return airports.map( entity -> mapper.airportEntityToDomain(entity));
    }



    public Optional<Airport> findAirportById( final Long id )
    {
        Optional<AirportEntity> airportEntity = repository.findAirportById( id );

        return mapEntityToDomain( airportEntity );
    }


    public Optional<Airport> findAirportByIdent( final String code )
    {
        Optional<AirportEntity> airportEntity = repository.findAirportByIdent( code );

        return mapEntityToDomain( airportEntity );
    }



    public Page<Airport> advancedQuery( final String iataCode,
                                        final String icaoCode,
                                        final String ident,
                                        final String name,
                                        final Pageable paging)
    {
        Page<AirportEntity> entities = repository.advancedQuery( iataCode, icaoCode, ident, name, paging );

        return entities.map( entity -> mapper.airportEntityToDomain(entity));
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
