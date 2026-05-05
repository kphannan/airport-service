/* (C) 2025 */

package com.example.airline.location.airport.api;


import java.util.List;
import java.util.Optional;

import com.example.airline.airport.AirportCountInContinentDTO;
import com.example.airline.airport.AirportCountInCountryDTO;
import com.example.airline.airport.AirportCountInRegionDTO;
import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.mapper.AirportDtoMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.service.AirportCreateService;
import com.example.airline.location.airport.service.AirportDeleteService;
import com.example.airline.location.airport.service.AirportReadService;
import com.example.airline.location.airport.service.AirportUpdateService;
import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.utility.HeaderUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/*
 *   API (Controller)
 *       Create
 *           Post
 *       Read
 *           Get
 *       Update
 *           Patch
 *           Put
 *       Delete
 *           Delete
 *       Administrative
 *           Head
 *           Info
 *           Opt
 *           Trace
 *
 *   Config
 *   mapper
 *   model
 *   persistence
 *
 *   Service
 *       Create
 *       Read
 *       Update
 *       Delete
 */

@RestController
@RequestMapping( "/location/airport" )
@Tag( name = "Airports" )
@GlobalApiResponses
@GlobalApiSecurityResponses
@Log4j2
public class AirportController
{
    // Autowired via constructor
    private final AirportCreateService createService;
    private final AirportReadService   readService;
    private final AirportUpdateService updateService;
    private final AirportDeleteService deleteService;
    private final AirportDtoMapper     mapper;

    public AirportController( final AirportCreateService createService,
                              final AirportReadService readService,
                              final AirportUpdateService updateService,
                              final AirportDeleteService deleteService,
                              final AirportDtoMapper mapper )
    {
        this.createService = createService;
        this.readService   = readService;
        this.updateService = updateService;
        this.deleteService = deleteService;
        this.mapper        = mapper;
    }

    // ========== CREATE ==========
    // ===== POST =====

    // ========== READ ==========
    // ===== GET =====
    // --- List ---
    @GetMapping( "" )
    public Page<AirportDTO> restGetFindAll( final Pageable pageable )
    {
        final Page<Airport> regions = readService.findAll( pageable );

        return regions.map( mapper::domainToApi );
    }


    // --- Single entity ---
    @Operation( method = "GET",
                summary = "Find a Airport by Id",
                description = "Find a Airport by Id",
                requestBody = @RequestBody( required = false ),
                responses = { @ApiResponse( description = "Success",
                                            responseCode = "200",
                                            content = { @Content( mediaType = "application/json",
                                                                  schema = @Schema( implementation = AirportDTO.class ) ),
                                                        @Content( mediaType = "application/yaml",
                                                                  schema = @Schema( implementation = AirportDTO.class ) ),
                                                        @Content( mediaType = "application/xml",
                                                                  schema = @Schema( implementation = AirportDTO.class ) )
                                            }
                )
                },
                parameters = { @Parameter( name = "id", required = true,
                                           in = ParameterIn.PATH,
                                           description = "Primary Key" ),
                               @Parameter( name = "Bearer", required = false,
                                           schema = @Schema( implementation = String.class ),
                                           in = ParameterIn.HEADER,
                                           description = "Authentication / Authorization token" ),
                               @Parameter( name = HeaderUtility.TRACEID, required = false,
                                           schema = @Schema( implementation = String.class ),
                                           in = ParameterIn.HEADER,
                                           description = "Distributed tracing identifier" ),
                               @Parameter( name = HeaderUtility.TRACESTATE, required = false,
                                           schema = @Schema( implementation = String.class ),
                                           in = ParameterIn.HEADER,
                                           description = "Vendor specific trace identification" )
                }
    )
    @GetMapping( "/{id}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<AirportDTO> restGetFindAirportById( @PathVariable final Long id )
    {
        final Optional<Airport> optionalAirport = readService.findAirportById( id );

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
        final Optional<Airport> optionalEntity = readService.findAirportByIdent( code );

        if ( optionalEntity.isPresent() )
        {
            final AirportDTO dto = mapper.domainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }


    // ----- Count of airports -----
    // --- grouped by Continent ---
    @GetMapping( "/summary/continent/code" )
    public ResponseEntity<List<AirportCountInContinentDTO>> restGetCountAirportsInAllContinents()
    {
        final List<AirportCountInContinent> counts = readService.countAirportsByContinent();

        final List<AirportCountInContinentDTO> dto = mapper.domainToApiAirportsInContinent( counts );

        return ResponseEntity.ok( dto );
    }

    // --- grouped by Country within a specific continent ---
    // get a list of countries in the continent, with counts of airports in each country
    @GetMapping( "/summary/continent/code/{continentCode}" )
    public ResponseEntity<List<AirportCountInCountryDTO>>
    restGetCountCountryAirportsByContinent( @PathVariable final String continentCode )
    {
        final List<AirportCountInCountry> counts = readService.countCountryAirportsByContinent( continentCode );

        final List<AirportCountInCountryDTO> dto = mapper.domainToApiAirportsInCountry( counts );

        return ResponseEntity.ok( dto );
    }

    // --- Country ---

//    @GetMapping( "/summary/country/code/{countryCode}" )
//    public ResponseEntity<List<AirportCountInCountryDTO>>
//    restGetCountAirportsByCountry( @PathVariable final String countryCode )
//    {
//        final List<AirportCountInCountry> counts = service.countAirportsByCountry( countryCode );
//
//        final List<AirportCountInCountryDTO> dto = mapper.domainToApiAirportsInCountry( counts );
//
//        return ResponseEntity.ok( dto );
//    }

//    @GetMapping( "/summary/country/code/{code}" )
//    public ResponseEntity<AirportCountInCountryDTO>
//    restGetCountAirportsByCountry( @PathVariable final String code )
//    {
//        List<AirportCountInCountryDTO> foo = service.countAirportsByCountry();
    ////        final Optional<Airport> optionalEntity = service.findAirportByIdent( code );
    ////
    ////        if ( optionalEntity.isPresent() )
    ////        {
    ////            final AirportDTO dto = mapper.domainToApi( optionalEntity.get() );
    ////
    ////            return ResponseEntity.ok( dto );
    ////        }
//
//        // may include instance in header.....
//        return ResponseEntity.noContent().build();
//        // return ResponseEntity.noContent().location().build();
//    }

    // --- Region ---


//    @GetMapping( "/summary/region/code/{code}" )
//    public ResponseEntity<List<AirportCountInRegionDTO>> restGetCountAirportsByRegion( @PathVariable final String code )
//    {
//        final List<AirportCountInRegion> counts = service.countAirportsByRegion();
//
//        final List<AirportCountInRegionDTO> dto = mapper.domainToApiAirportsInRegion( counts );
//
//        return ResponseEntity.ok( dto );
//    }

    // --- by Region within a country
    //     Grouped/counted by region within a specific country
    @GetMapping( "/summary/country/code/{countryCode}" )
    public ResponseEntity<List<AirportCountInRegionDTO>>
    restGetCountAirportsByRegion( @PathVariable final String countryCode )
    {
        final List<AirportCountInRegion> counts = readService.countRegionAirportsByCountry( countryCode );

        final List<AirportCountInRegionDTO> dto = mapper.domainToApiAirportsInRegion( counts );

        return ResponseEntity.ok( dto );
    }

//    @GetMapping( "/summary/region/code/{regionCode}" )
//    public ResponseEntity<List<AirportSummaryDTO>>
//    restGetAirportsSummariesByRegion( @PathVariable final String regionCode )
//    {
//        final List<AirportSummary> counts = service.countRegionAirportsByCountry( regionCode );
//
//        final List<AirportSummaryDTO> dto = mapper.domainToApiAirportsInRegion( counts );
//
//        return ResponseEntity.ok( dto );
//    }

    // --- by specific Region  ---
    /**
     * REST method to retrieve a list of {@link AirportDTO} within the specified @see Region.
     *
     * @param regionCode the desired {@link AirportDTO#isoRegion}.
     * @return A list of {@link AirportDTO} entities found withing the desired @see Region.
     */
    @GetMapping( "/summary/region/code/{regionCode}" )
    public ResponseEntity<List<AirportDTO>> restGetAirportsByRegion( @PathVariable final String regionCode )
    {
        final List<Airport> airports = readService.findAirportsByRegion( regionCode );

        final List<AirportDTO> dto = mapper.domainToApi( airports );

        return ResponseEntity.ok( dto );
    }



    // --- Advanced Search ---
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
        final Page<Airport> result = readService.advancedQuery( iataCode,
                                                                icaoCode,
                                                                ident,
                                                                name,
                                                                paging );

        return result.map( mapper::domainToApi );
    }

    // ========== UPDATE ==========
    // ===== PATCH =====
    // ===== PUT =====

    // ========== DELETE ==========
    // ===== DELETE =====

    // ========== Administrative ==========
    // ===== HEAD =====
    // ===== INFO =====
    // ===== OPTION =====
    // ===== TRACE =====



}
