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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/*
 *   API (Controller) /location/airport
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
 *
 *
 * /location/airport
 *     GET      - /location/airport                                            (restGetFindAll)                                 paged list of all airports
 *     GET      - /location/airport/{id}                                       (restGetFindAirportById)                         single airport by ID
 *     GET      - /location/airport/code/{code}                                (restGetFindAirportByCode)                       single airport by ICAO code
 *     GET      - /location/airport/summary/continent/code                     (restGetCountAirportsInAllContinents)            count of airports on continent
 *     GET      - /location/airport/summary/continent/code/{continentCode}     (restGetCountCountryAirportsByContinent)         count of airports by country
 *  *  GET      - /location/airport/summary/country/code/{countryCode}         (restGetCountAirportsByCountry)
 *  -  GET      - /location/airport/summary/country/code/{regionCode}          (restGetCountAirportsByRegion)
 *  +  GET      - /location/airport/summary/region/code/{regionCode}           (restGetCountAirportsByRegion)
 *  -  GET      - /location/airport/summary/region/code/{regionCode}           (restGetAirportsByRegion)                        List of airports in a Region
 *  +  GET      - /location/airport/region/code/{regionCode}                   (restGetAirportsByRegion)                        List of airports in a Region
 *  +  GET      - /location/airport/country/code/{countryCode}                 (restGetAirportsByCountry)                        List of airports in a Region
 *     GET      - /location/airport/search                                     (advancedQuery)                                  General query

 *
 * * /location/airport                                            - GET       paged list of all airports
 * /location/airport/{id}                                       - GET       single airport
 * /location/airport/code/{code}                                - GET       single airport
 * /location/airport/summary/continent/code                     - GET       count of airports by continent
 * /location/airport/summary/continent/code/{continentCode}     - GET       count of airports by country
 * /location/airport/summary/country/code/{countryCode}
 * /location/airport/summary/country/code/{countryCode}
 * /location/airport/summary/country/code/{regionCode}
 * /location/airport/summary/region/code/{regionCode}           - GET
 * /location/airport/search                                     - GET
 *
 */

/**
 * REST microservice for Airport instances.
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


    /**
     * Construct a new REST Controller.
     *
     * @param createService Create service (Crud)
     * @param readService   Read service (cRud)
     * @param updateService Update service (crUd)
     * @param deleteService Delete service (cruD)
     * @param mapper        API DTO to domain layer mapper.
     */
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

    /**
     * Get a paged subset of airports.
     *
     * @param pageable       the subset {@link org.springframework.data.domain.Pageable}
     * @param requestHeaders HttpHeaders primarily for call tracing, optional.
     *
     * @return the desired page of Airports.
     */
    // TODO add OpenAPI spec
    @GetMapping( "" )
    public Page<AirportDTO>
        restGetFindAll( final Pageable pageable,
                        @RequestHeader final HttpHeaders requestHeaders )
    {
        final Page<Airport> regions = readService.findAll( pageable );

        return regions.map( mapper::domainToApi );
    }


    // --- Single entity ---

    /**
     * Find an airport by its ID in the persistent store.
     *
     * @param id             the unique persistence id of the airport (ICAO or IATA)
     * @param requestHeaders HttpHeaders primarily for call tracing, optional.
     *
     * @return the airport.
     */
    @Operation( method = "GET",
                summary = "Find a Airport by Id",
                description = "Find a Airport by Id",
                requestBody = @RequestBody( required = false ),
                responses = {
                    @ApiResponse( description = "Success",
                                  responseCode = "200",
                                  content = {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = AirportDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = AirportDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = AirportDTO.class ) )
                                  }
                    )
                },
                parameters = {
                    @Parameter( name = "id", required = true,
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
    public ResponseEntity<AirportDTO>
        restGetFindAirportById( @PathVariable final Long id,
                                @RequestHeader final HttpHeaders requestHeaders )
    {
        final Optional<Airport> optionalAirport = readService.findAirportById( id );

        if ( optionalAirport.isPresent() )
        {
            final AirportDTO dto = mapper.domainToApi( optionalAirport.get() );

            return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .body( dto );
        }

        return ResponseEntity.noContent().build();
    }


    /**
     * Find an airport by its unique code (ICAO / IATA).
     *
     * @param code the airport code (ICAO or IATA)
     * @return the airport.
     */
    // TODO add OpenAPI spec
    @GetMapping( "/code/{code}" )
    public ResponseEntity<AirportDTO>
        restGetFindAirportByCode( @PathVariable final String code,
                                  @RequestHeader final HttpHeaders requestHeaders )
    {
        final Optional<Airport> optionalEntity = readService.findAirportByIdent( code );

        if ( optionalEntity.isPresent() )
        {
            final AirportDTO dto = mapper.domainToApi( optionalEntity.get() );

            return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .body( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }


    // ----- Count of airports -----
    // --- grouped by Continent ---

    /**
     * Get a list of continents and the count of airports in that continent.
     *
     * @return List of all continents and the number of airports in the continent.
     */
    // TODO add OpenAPI spec
    @GetMapping( "/summary/continent/code" )
    public ResponseEntity<List<AirportCountInContinentDTO>>
        restGetCountAirportsInAllContinents( @RequestHeader final HttpHeaders requestHeaders )
    {
        final List<AirportCountInContinent> counts = readService.countAirportsByContinent();

        final List<AirportCountInContinentDTO> dto = mapper.domainToApiAirportsInContinent( counts );

        return ResponseEntity
            .status( HttpStatus.OK )
            .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
            .body( dto );
    }

    // --- grouped by Country within a specific continent ---

    /**
     * get a list of countries in the continent, with counts of airports in each country.
     *
     * @param continentCode  the 2 character continent code.
     * @param requestHeaders HttpHeaders primarily for call tracing, optional.
     *
     * @return List of countries including the number of airports in that country.
     */
    // TODO add OpenAPI spec
    @GetMapping( "/summary/continent/code/{continentCode}" )
    public ResponseEntity<List<AirportCountInCountryDTO>>
        restGetCountCountryAirportsByContinent( @PathVariable final String continentCode,
                                                @RequestHeader final HttpHeaders requestHeaders )
    {
        final List<AirportCountInCountry> counts = readService.countCountryAirportsByContinent( continentCode );

        final List<AirportCountInCountryDTO> dto = mapper.domainToApiAirportsInCountry( counts );

        return ResponseEntity
            .status( HttpStatus.OK )
            .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
            .body( dto );
    }

    // --- Country ---

    /**
     * Get the count of all airports in a specific country.
     *
     * @param countryCode    the desired country code, required.
     * @param requestHeaders HttpHeaders primarily for call tracing, optional.
     *
     * @return collection of Country names and the number of airports within that country.
     */
    @GetMapping( "/summary/country/code/{countryCode}" )
    public ResponseEntity<List<AirportCountInCountryDTO>>
        restGetCountAirportsByCountry( @PathVariable final String countryCode,
                                       @RequestHeader final HttpHeaders requestHeaders )
    {
        final List<AirportCountInCountry> counts = readService.countAirportsByCountry( countryCode );

        final List<AirportCountInCountryDTO> dto = mapper.domainToApiAirportsInCountry( counts );

        return ResponseEntity
            .status( HttpStatus.OK )
            .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
            .body( dto );
    }

    // @GetMapping( "/summary/country/code/{code}" )
    // public ResponseEntity<AirportCountInCountryDTO>
    // restGetCountAirportsByCountry( @PathVariable final String code )
    // {
    //    List<AirportCountInCountryDTO> foo = service.countAirportsByCountry();
    // //        final Optional<Airport> optionalEntity = service.findAirportByIdent( code );
    // //
    // //        if ( optionalEntity.isPresent() )
    // //        {
    // //            final AirportDTO dto = mapper.domainToApi( optionalEntity.get() );
    // //
    // //            return ResponseEntity.ok( dto );
    // //        }
    //
    //    // may include instance in header.....
    //    return ResponseEntity.noContent().build();
    //    // return ResponseEntity.noContent().location().build();
    // }

    // --- Region ---


    // @GetMapping( "/summary/region/code/{code}" )
    // public ResponseEntity<List<AirportCountInRegionDTO>> restGetCountAirportsByRegion( @PathVariable final String code )
    // {
    //    final List<AirportCountInRegion> counts = service.countAirportsByRegion();
    //
    //    final List<AirportCountInRegionDTO> dto = mapper.domainToApiAirportsInRegion( counts );
    //
    //    return ResponseEntity.ok( dto );
    // }

    // --- by Region within a country
    //     Grouped/counted by region within a specific country

    /**
     * Find number of airports in each region within a country.
     *
     * @param regionCode unique ISO 3166 code of the country.
     *
     * @return list of regions and the number of airports in that region.
     */
    // TODO add OpenAPI spec
    @GetMapping( "/summary/country/code/{regionCode}" )
    public ResponseEntity<List<AirportCountInRegionDTO>>
        restGetCountAirportsByRegion( @PathVariable final String regionCode,
                                      @RequestHeader final HttpHeaders requestHeaders )
    {
        final List<AirportCountInRegion> counts = readService.countRegionAirportsByCountry( regionCode );

        final List<AirportCountInRegionDTO> dto = mapper.domainToApiAirportsInRegion( counts );

        return ResponseEntity
            .status( HttpStatus.OK )
            .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
            .body( dto );
    }

    // @GetMapping( "/summary/region/code/{regionCode}" )
    // public ResponseEntity<List<AirportSummaryDTO>>
    // restGetAirportsSummariesByRegion( @PathVariable final String regionCode )
    // {
    //    final List<AirportSummary> counts = service.countRegionAirportsByCountry( regionCode );
    //
    //    final List<AirportSummaryDTO> dto = mapper.domainToApiAirportsInRegion( counts );
    //
    //    return ResponseEntity.ok( dto );
    // }

    // --- by specific Region  ---

    /**
     * REST method to retrieve a list of {@link AirportDTO} within the specified @see Region.
     *
     * @param regionCode the desired {@link AirportDTO#isoRegion}.
     *
     * @return A list of {@link AirportDTO} entities found withing the desired @see Region.
     */
    // TODO add OpenAPI spec
    @GetMapping( "/summary/region/code/{regionCode}" )
    public ResponseEntity<List<AirportDTO>>
        restGetAirportsByRegion( @PathVariable final String regionCode,
                                 @RequestHeader final HttpHeaders requestHeaders )
    {
        final List<Airport> airports = readService.findAirportsByRegion( regionCode );

        return ResponseEntity
            .status( HttpStatus.OK )
            .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
            .body( mapper.domainToApi( airports ) );
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
     * @return the target page with {@code Airport} records if any match the criteria.
     */
    // TODO add OpenAPI spec
    @GetMapping( path = "/search" )
    public Page<AirportDTO>
        advancedQuery( @RequestParam( name = "iataCode", required = false ) final String iataCode,
                       @RequestParam( name = "icaoCode", required = false ) final String icaoCode,
                       @RequestParam( name = "ident",    required = false ) final String ident,
                       @RequestParam( name = "name",     required = false ) final String name,
                       final Pageable paging,
                       @RequestHeader final HttpHeaders requestHeaders )
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
