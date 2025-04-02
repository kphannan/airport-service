/* (C)2025 */

package com.example.airline.location.api;


import java.util.Locale;
import java.util.Optional;

import com.example.airline.airport.AirportDTO;
import com.example.airline.location.Airport;
import com.example.airline.location.mapper.AirportMapper;
import com.example.airline.location.service.AirportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping( "/v1/location/airport" )
public class AirportController
{
    // Autowired via constructor
    private AirportService service;
    private AirportMapper     mapper;


    public AirportController( AirportService service, AirportMapper mapper  )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    @GetMapping( "" )
    public Page<AirportDTO> restGetFindAll( final Pageable pageable )
    {
        Page<Airport> regions = service.findAll( pageable );

        return regions.map( entity -> mapper.airportDomainToApi(entity));
    }



    @GetMapping( "/{id}" )
    public ResponseEntity<AirportDTO> restGetFindAirportById( @PathVariable final Long id )
    {
        final Optional<Airport> optionalAirport = service.findAirportById( id );

        if ( optionalAirport.isPresent() )
        {
            AirportDTO dto = mapper.airportDomainToApi( optionalAirport.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping( "/code/{code}" )
    public ResponseEntity<AirportDTO> restGetFindAirportByCode( @PathVariable final String code )
    {
        Optional<Airport> optionalEntity = service.findAirportByIdent( code );

        if ( optionalEntity.isPresent() )
        {
            AirportDTO dto = mapper.airportDomainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }


        /**
     * Search for {@code Airport} records that contain any of the query parameters.
     *
     * @param iataCode optional IATA code to search on.
     * @param icaoCode optional ICAO code to search on.
     * @param ident optional identifier which may be the ICAO code to search on.
     * @param name optional airport name string.
     * @param paging current {@code Page} specification.
     * @return the target page with {@code Airport} records if any match the criteria.
     */
    @GetMapping( path = "/search" )
    public Page<AirportDTO> advancedQuery(  @RequestParam( name = "iataCode", required = false )
                                                         final String iataCode
                                                       , @RequestParam( name = "icaoCode", required = false )
                                                         final String icaoCode
                                                       , @RequestParam( name = "ident", required = false )
                                                         final String ident
                                                       , @RequestParam( name = "name", required = false )
                                                         final String name
                                                       , final Pageable paging )
    {
        final Page<Airport> result = service.advancedQuery(  null == iataCode
                                                                    ? "" : iataCode.toUpperCase( Locale.US )
                                                              , null == icaoCode
                                                                    ? "" : icaoCode.toUpperCase( Locale.US )
                                                              , null == ident
                                                                    ? "" : ident.toUpperCase( Locale.US )
                                                              , null == name
                                                                    ? "" : name.toUpperCase( Locale.US )
                                                              , paging );

        return result.map( entity -> mapper.airportDomainToApi(entity));
    }

}
