/* (C) 2025 */

package com.example.airline.location.continent.api;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.ContinentDTO;
import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.continent.mapper.ContinentMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.service.ContinentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// TODO Trace header.... https://github.com/w3c/trace-context/blob/main/spec/20-http_request_header_format.md
//      https://w3c.github.io/trace-context/


@RestController
@RequestMapping( "/location/continent" )
@Tag( name = "Continents" )
@GlobalApiResponses
@GlobalApiSecurityResponses
public class ContinentController
{
    // Autowired via constructor
    private final ContinentService service;
    private final ContinentMapper  mapper;
//    private final List<MediaType> restMediaTypes = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );


    /**
     * Constructor for the ContinentController.
     *
     * @param service The service to use for continent operations.
     * @param mapper  The mapper to convert between domain and API objects.
     */
    public ContinentController( final ContinentService service, final ContinentMapper mapper )
    {
        this.service = service;
        this.mapper  = mapper;
    }


    /**
     * Find all Continents.
     *
     * @param requestHeader Request headers with authentication and tracing information.
     * @return a paged list of ContinentDTO objects.
     */
    @Operation( method = "GET",
                summary = "Retrieve all Continents in paged form for performance",
                description = "Retrieve a paged list of Continents",

                responses = { @ApiResponse( description = "Success",
                                            responseCode = "200",
                                            content = { @Content( mediaType = "application/json"
                                                    /*, schema = @Schema( implementation = ContinentDTO.class ) */ ),
                                                        @Content( mediaType = "application/yaml"
                                                                /*, schema = @Schema( implementation = ContinentDTO.class ) */ ),
                                                        @Content( mediaType = "application/xml"
                                                                /*, schema = @Schema( implementation = ContinentDTO.class ) */ )
                                            }
                )
                }
//                responses = {
//                    @ApiResponse( description = "Success", responseCode = "200" )
                // @ApiResponse( description = "Unauthorized", responseCode = "403" )
//    }
                )
    @GetMapping( "" )
    public ResponseEntity<List<ContinentDTO>> restGetFindAll( @RequestHeader final HttpHeaders requestHeader )
    {
        final List<Continent> continents = service.findAll();

        final List<ContinentDTO> dtos = mapper.domainToApi( continents );

        return ResponseEntity.ok( dtos );
    }


    /**
     * Find a Continent by Id.
     *
     * @param id            The primary key of the Continent to find.
     * @param requestHeader Request headers with authentication and tracing information.
     * @return the ContinentDTO object if found, or a 204 No Content response if not found.
     */
    @Operation( method = "GET",
                summary = "Find a Continent by Id",
                description = "Find a Continent by Id",
                requestBody = @RequestBody( required = false
                                            // content = { @Content( mediaType = "application/json",
                                            //                       schema = @Schema( implementation = ContinentDTO.class ) )
                                            //           }
                ),
                responses = { @ApiResponse( description = "Success",
                                            responseCode = "200",
                                            content = { @Content( mediaType = "application/json", schema = @Schema(
                                                    implementation = ContinentDTO.class ) ),
                                                        @Content( mediaType = "application/yaml", schema = @Schema(
                                                                implementation = ContinentDTO.class ) ),
                                                        @Content( mediaType = "application/xml", schema = @Schema(
                                                                implementation = ContinentDTO.class ) )
                                            }
                )
                },
                parameters = {
                    @Parameter( name = "id", required = true, in = ParameterIn.PATH, description = "Primary Key" ),
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
    public ResponseEntity<ContinentDTO> restGetFindContinentById( @PathVariable final Integer id,
                                                                  @RequestHeader HttpHeaders requestHeader )
    {
        final Optional<Continent> optionalContinent = service.getReferenceById( id );

        if ( optionalContinent.isPresent() )
        {
            final ContinentDTO dto = mapper.domainToApi( optionalContinent.get() );

            return ResponseEntity.ok( dto );
//            return new ResponseEntity<>( dto, HttpStatus.OK );
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Find a Continent by code.
     *
     * @param code          The 2-character code of the Continent to find.
     * @param requestHeader Request headers with authentication and tracing information.
     * @return the ContinentDTO object if found, or a 204 No Content response if not found.
     */
    @GetMapping( "/code/{code}" )
    public ResponseEntity<ContinentDTO> restGetFindContinentByCode( @PathVariable final String code,
                                                                    @RequestHeader final HttpHeaders requestHeader )
    {
        final Optional<Continent> optionalEntity = service.findByCode( code );

        if ( optionalEntity.isPresent() )
        {
            final ContinentDTO dto = mapper.domainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }




    // ===== POST =====
    @PostMapping( "" )
    public ResponseEntity<ContinentDTO> restPostAddContinent( @RequestHeader HttpHeaders requestHeader )
    {
//        final Optional<Continent> optionalContinent = service.findById( id );
//
//        if ( optionalContinent.isPresent() )
//        {
//            final ContinentDTO dto = mapper.domainToApi( optionalContinent.get() );
//
//            return new ResponseEntity<ContinentDTO>( dto, HttpStatus.OK );
//        }

        return ResponseEntity.noContent().build();
    }



    // ===== PUT =====
//    @PutMapping( "/{id}" )
    @PutMapping( "" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO> restPutContinentById( //@PathVariable( "id" ) final Integer id,
                                                              @org.springframework.web.bind.annotation.RequestBody ContinentDTO continentDTO,
                                                              @RequestHeader HttpHeaders requestHeader )
    {
        Continent continent = service.update( mapper.apiToDomain( continentDTO ) );
        if ( null != continent )
        {
            return ResponseEntity.ok( mapper.domainToApi( continent ) );
        }

        // The item is not in the DB.  If client intention is to insert
        // then a POST should have been used.
        return ResponseEntity
                .status( HttpStatus.CONFLICT )
//                .body( "Continent does not exist" );
                .build();
    }

    // ===== DELETE =====
    @DeleteMapping( "/{id}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO> restDeleteContinentById( @PathVariable final Integer id,
                                                                 @RequestHeader HttpHeaders requestHeader )
    {
        // Delete is idempotent and will return NO_CONTENT regardless if
        // the item was deleted, or if it didn't exist.
        service.deleteById( id );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping( "" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO> restDelete( @org.springframework.web.bind.annotation.RequestBody ContinentDTO continentDTO,
                                                    @RequestHeader HttpHeaders requestHeader )
    {
        // Delete is idempotent and will return NO_CONTENT regardless if
        // the item was deleted, or if it didn't exist.
        service.deleteById( continentDTO.getId() );

        return ResponseEntity.noContent().build();
    }

    // ===== PATCH =====
    @PatchMapping( "/{id}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO> restPatchContinentById( @PathVariable final Integer id,
                                                                     @RequestHeader HttpHeaders requestHeader )
    {
        return ResponseEntity.noContent().build();
    }

    // ===== Options =====
    @RequestMapping( value = "", method = RequestMethod.OPTIONS )
    public ResponseEntity<Void> restOptionsContinent( @RequestHeader HttpHeaders requestHeader )
    {
        // "detail": "Request method 'DELETE' is not supported; Supported methods: HEAD, TRACE, POST, GET, OPTIONS",

        return ResponseEntity.noContent().build();
    }

    // ===== HEAD =====
    @RequestMapping( value = "", method = RequestMethod.HEAD )
    public ResponseEntity<Void> restHeadContinent( @RequestHeader HttpHeaders requestHeader )
    {
        // "detail": "Request method 'DELETE' is not supported; Supported methods: HEAD, TRACE, POST, GET, OPTIONS",
        return ResponseEntity.noContent().build();
    }

    // ===== INFO =====
//    @RequestMapping( value = "", method = RequestMethod.INFO )
//    public ResponseEntity<ContinentDTO> restInfoContinent( @RequestHeader HttpHeaders requestHeader )
//    {
//        return ResponseEntity.noContent().build();
//    }

    // ===== TRACE =====
    @RequestMapping( value = "", method = RequestMethod.TRACE )
    public ResponseEntity<Void> restTraceContinent( @RequestHeader HttpHeaders requestHeader )
    {
        return ResponseEntity.noContent().build();
    }

}
