/* (C) 2025 */

package com.example.airline.location.airport.api;


import java.util.Locale;
import java.util.Optional;

import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.mapper.AirportMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.service.AirportService;
import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping( "/location/airport" )
@Tag( name = "Airports" )
@GlobalApiResponses
@GlobalApiSecurityResponses
public class AirportController
{
    // Autowired via constructor
    private final AirportService service;
    private final AirportMapper  mapper;

    public AirportController( final AirportService service, final AirportMapper mapper )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    @GetMapping( "" )
    public Page<AirportDTO> restGetFindAll( final Pageable pageable )
    {
        final Page<Airport> regions = service.findAll( pageable );

        return regions.map( mapper::domainToApi );
    }



    @Operation( method = "GET",
            summary = "Find a Continent by Id",
            description = "Find a Continent by Id",
            requestBody = @RequestBody( required = false
//                                            content = { @Content( mediaType = "application/json",
//                                                                  schema = @Schema( implementation = ContinentDTO.class ) )
//                                                      }
            ),
            responses = { @ApiResponse( description = "Success",
                    responseCode = "200",
                    content = { @Content( mediaType = "application/json", schema = @Schema( implementation = AirportDTO.class ) ),
                                @Content( mediaType = "application/yaml", schema = @Schema( implementation = AirportDTO.class ) ),
                                @Content( mediaType = "application/xml", schema = @Schema( implementation = AirportDTO.class ) )
                    }
            )
            },
            parameters = { @Parameter( name = "id", required = true, in = ParameterIn.PATH, description = "Primary Key"),
                           @Parameter( name = "Bearer", required = false,
                                   schema = @Schema( implementation = String.class ),
                                   in = ParameterIn.HEADER,
                                   description = "Authentication / Authorization token" ),
                           @Parameter( name = "TRACEPARENT", required = false,
                                   schema = @Schema( implementation = String.class ),
                                   in = ParameterIn.HEADER,
                                   description = "Distributed tracing identifier" ),
                           @Parameter( name = "TRACESTATE", required = false,
                                   schema = @Schema( implementation = String.class ),
                                   in = ParameterIn.HEADER,
                                   description = "Vendor specific trace identification" )
            }
    )
    @GetMapping( "/{id}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<AirportDTO> restGetFindAirportById( @PathVariable final Long id )
    {
        final Optional<Airport> optionalAirport = service.findAirportById( id );

        if ( optionalAirport.isPresent() )
        {
            final AirportDTO dto = mapper.domainToApi( optionalAirport.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping( "/code/{code}" )
    public ResponseEntity<AirportDTO> restGetFindAirportByCode( @PathVariable final String code )
    {
        final Optional<Airport> optionalEntity = service.findAirportByIdent( code );

        if ( optionalEntity.isPresent() )
        {
            final AirportDTO dto = mapper.domainToApi( optionalEntity.get() );

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
     * @param ident    optional identifier which may be the ICAO code to search on.
     * @param name     optional airport name string.
     * @param paging   current {@code Page} specification.
     *
     * @return the target page with {@code Airport} records if any match the
     *         criteria.
     */
    @GetMapping( path = "/search" )
    public Page<AirportDTO> advancedQuery( @RequestParam( name = "iataCode", required = false ) final String iataCode,
                                           @RequestParam( name = "icaoCode", required = false ) final String icaoCode,
                                           @RequestParam( name = "ident", required = false ) final String ident,
                                           @RequestParam( name = "name", required = false ) final String name,
                                           final Pageable paging )
    {
        final Page<Airport> result = service.advancedQuery( null == iataCode ? "" : iataCode.toUpperCase( Locale.US ),
                                                            null == icaoCode ? "" : icaoCode.toUpperCase( Locale.US ),
                                                            null == ident ? "" : ident.toUpperCase( Locale.US ),
                                                            null == name ? "" : name.toUpperCase( Locale.US ), paging );

        return result.map( mapper::domainToApi );
    }

}
