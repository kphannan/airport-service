/* (C) 2025 */

package com.example.airline.location.continent.api;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.continent.ContinentDTO;
import com.example.airline.location.continent.NewContinentDTO;
import com.example.airline.location.continent.mapper.ContinentDtoMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.service.ContinentCreateService;
import com.example.airline.location.continent.service.ContinentDeleteService;
import com.example.airline.location.continent.service.ContinentReadService;
import com.example.airline.location.continent.service.ContinentUpdateService;
import com.example.utility.HeaderUtility;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// TODO Trace header.... https://github.com/w3c/trace-context/blob/main/spec/20-http_request_header_format.md
//      https://w3c.github.io/trace-context/

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


/**
 * REST controller with CRUD operations for Continent entities.
 */
@RestController
@RequestMapping( "/location/continent" )
@Tag( name = "Continents" )
@GlobalApiResponses
@GlobalApiSecurityResponses
@Log4j2
public class ContinentController
{
    // Autowired via constructor
    private final ContinentReadService   readService;
    private final ContinentCreateService createService;
    private final ContinentUpdateService updateService;
    private final ContinentDeleteService deleteService;
    private final ContinentDtoMapper     mapper;

    //    private final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
    //                                                                StandardCharsets.UTF_8 );


    /**
     * Constructor for the ContinentController.
     *
     * @param service The service to use for continent operations.
     * @param mapper  The mapper to convert between domain and API objects.
     */
    public ContinentController( final ContinentReadService service,
                                final ContinentCreateService createService,
                                final ContinentUpdateService updateService,
                                final ContinentDeleteService deleteService,
                                final ContinentDtoMapper mapper )
    {
        this.readService   = service;
        this.createService = createService;
        this.updateService = updateService;
        this.deleteService = deleteService;
        this.mapper        = mapper;
    }


    // ========== CREATE ==========
    // ===== POST =====

    /**
     * Create a new Continent entity in a persistent store.
     *
     * @param newContinentDTO the intended Continent entity.
     * @param requestHeaders HTTP headers including trace identifiers.
     * @return the new persistent entity with a persistent store key.
     */
    @Operation( method = "POST",
                summary = "Add a Continent",
                description = "Add a new Continent only if it does not already exist",
                requestBody = @RequestBody( required = true,
                                            content = { @Content( mediaType = "application/json",
                                                                  schema = @Schema( implementation = NewContinentDTO.class ) )
                                            }
                ),
                responses = {
                    @ApiResponse( description = "Continent created and returned",
                                  responseCode = "201",
                                  content = {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                        )
                },
                parameters = {
                    @Parameter( name = "Bearer",
                                required = true,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                }
    )
    @PostMapping( "" )
    // public ResponseEntity<ContinentDTO>
    public ResponseEntity<?>
        restPostAddContinent( @Valid @org.springframework.web.bind.annotation.RequestBody final NewContinentDTO newContinentDTO,
                              @RequestHeader final HttpHeaders requestHeaders )
    {
        final Continent continent = createService.create( mapper.apiToDomain( newContinentDTO ) );
        if ( null != continent )
        {
            // Build the resource id (path) of the newly created item
            final URI newResourceLocation = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path( "/{continentId}" )
                    .buildAndExpand( continent.id() )
                    .toUri();

            return ResponseEntity
                    .created( newResourceLocation )
                    .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                    .body( mapper.domainToApi( continent ) );
        }

        // The item is already in the DB.  If the client intention is to update,
        // then a PUT should have been used.
        final ProblemDetail problemDetail = ProblemDetail.forStatus( HttpStatus.CONFLICT );
        return ResponseEntity
                   // .status( HttpStatus.CONFLICT )
                .status( problemDetail.getStatus() )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                // TODO Include Problem Details as response body
                .body( problemDetail );
                // .build();
    }


    // ========== READ ==========
    // ===== GET =====
    /**
     * Find all Continents.
     *
     * @param requestHeaders Request headers with authentication and tracing information.
     * @return a paged list of ContinentDTO objects.
     */
    @Operation( method = "GET",
                summary = "Retrieve a list of all Continents",
                description = "Find all Continents and return them in an array",
                responses =
                {
                    @ApiResponse( description = "All Continents found and returned in an array",
                                  responseCode = "200",
                                  content =
                                  {
                                      @Content( mediaType = "application/json"
                                      /*, schema = @Schema( implementation = ContinentDTO.class ) */ ),
                                      @Content( mediaType = "application/yaml"
                                      /*, schema = @Schema( implementation = ContinentDTO.class ) */ ),
                                      @Content( mediaType = "application/xml"
                                      /*, schema = @Schema( implementation = ContinentDTO.class ) */ )
                                  }
                    ),
                    @ApiResponse( description = "Unauthorized", responseCode = "403" )
                },
                parameters =
                {
                    @Parameter( name = HeaderUtility.TRACEID, required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE, required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                },
                security = {}
    )
    @GetMapping( "" )
    public ResponseEntity<List<ContinentDTO>> restGetFindAll( @Valid @RequestHeader final HttpHeaders requestHeaders )
    {
        /* TODO
        [    Test worker] m.m.a.RequestResponseBodyMethodProcessor :
        Writing [Page 1 of 1 containing com.example.airline.location.country.CountryDTO instances]
        [    Test worker] ration$PageModule$WarningLoggingModifier :
        Serializing PageImpl instances as-is is not supported, meaning that there is
        no guarantee about the stability of the resulting JSON structure!
        For a stable JSON structure, please use Spring Data's PagedModel
        (globally via @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO))
        or Spring HATEOAS and Spring Data's PagedResourcesAssembler as documented in
        https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.pageables.
        */
        final List<Continent> continents = readService.findAll();

        final List<ContinentDTO> dtos = mapper.domainToApi( continents );

        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .body( dtos );
    }


    /**
     * Find a Continent by ID.
     *
     * @param continentId    The primary key of the Continent to find.
     * @param requestHeaders Request headers with authentication and tracing information.
     * @return the ContinentDTO object if found, or a 204 No Content response if not found.
     */
    @Operation( method = "GET",
                summary = "Find a Continent by Id",
                description = "Find a Continent by Id",
                requestBody = @RequestBody( required = false ),
                responses = {
                    @ApiResponse( description = "Continent found and returned",
                                  responseCode = "200",
                                  content = {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                    )
                },
                parameters = {
                    @Parameter( name = "continentId", required = true, in = ParameterIn.PATH, description = "Primary Key" ),
                    @Parameter( name = HeaderUtility.TRACEID, required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE, required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                },
                security = {}
    )
    @GetMapping( "/{continentId}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO>
        restFindContinentById( @Valid @PathVariable( name = "continentId" ) final Integer continentId,
                               @RequestHeader final HttpHeaders requestHeaders )
    {
        final Optional<Continent> optionalContinent = readService.findById( continentId );

        if ( optionalContinent.isPresent() )
        {
            final ContinentDTO dto = mapper.domainToApi( optionalContinent.get() );

            // TODO handle Accept:application/json or Accept:application/XML
            // TODO  Last-Modified
            return ResponseEntity
                    .status( HttpStatus.OK )
                    .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                    .body( dto );
        }

        return ResponseEntity
                .noContent()
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    /**
     * Find a Continent by code.
     *
     * @param code           The 2-character code of the Continent to find.
     * @param requestHeaders Request headers with authentication and tracing information.
     * @return the ContinentDTO object if found, or a 204 No Content response if not found.
     */
    @Operation( method = "GET",
                summary = "Find a Continent by its abbreviation",
                description = "Find a Continent by its 2 letter code",
                responses =
                {
                    @ApiResponse( description = "Continent found and returned",
                                  responseCode = "200",
                                  content =
                                  {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                    )
                },
                parameters = {
                    @Parameter( name = "code",
                                required = true,
                                in = ParameterIn.PATH,
                                description = "2 character code" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                },
                security = {}
    )
    @GetMapping( "/code/{code}" )
    public ResponseEntity<ContinentDTO>
        restGetFindContinentByCode( @Valid @PathVariable final String code,
                                    @RequestHeader final HttpHeaders requestHeaders )
    {
        final Optional<Continent> optionalEntity = readService.findByCode( code );

        if ( optionalEntity.isPresent() )
        {
            final ContinentDTO dto = mapper.domainToApi( optionalEntity.get() );

            return ResponseEntity
                    .status( HttpStatus.OK )
                    .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                    .body( dto );
        }

        // may include instance in header.....
        return ResponseEntity
                .noContent()
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    // ========== UPDATE ==========
    // ===== PATCH =====
    /**
     * Handle HTTP Method PATCH for /location/continent/{continentId}.
     *
     * @param continentId key of the entity to update.
     * @param patch A modified continent instance.
     * @param requestHeaders Request's HttpHeaders
     * @return the updated Conntinent
     */
    @Operation( method = "PATCH",
                summary = "Update a Continent",
                description = "Update a Continent only if it exists.  All non-null attributes are updated.",
                requestBody =
                @RequestBody( required = true,
                              content = { @Content( mediaType = "application/json-patch+json",
                                                    schema = @Schema( implementation = ContinentDTO.class ) )
                              }
                ),
                responses =
                {
                    @ApiResponse( description = "Continent updated and returned",
                                  responseCode = "200",
                                  content =
                                  {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                    )
                },
                parameters =
                {
                    @Parameter( name = "Bearer",
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                }
    )
    @PatchMapping( path = "/{continentId}", consumes = "application/json-patch+json" )
    public ResponseEntity<ContinentDTO> restPatchContinentById( @Valid @PathVariable( name = "continentId" )
                                                                    final Integer continentId,
                                                                @RequestBody final String patch,
                                                                // @RequestBody final JsonPatch patch,
                                                                @RequestHeader final HttpHeaders requestHeaders )
    {
        final Optional<Continent> original = readService.findById( continentId );
        final Optional<Continent> updated = applyPatchToContinent( original, null );
        final ContinentDTO dto = mapper.domainToApi( updated.get() );

        // TODO implement PATCH
        // return ResponseEntity
        //         .status( HttpStatus.OK )
        //         .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
        //         .body( dto ); // .... use mapper to convert to DTO.
        //         .build();

        return ResponseEntity
                .status( HttpStatus.NOT_IMPLEMENTED )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .body( dto );
    }

    private Optional<Continent> applyPatchToContinent( final Optional<Continent> original, final JsonPatch jsonPatch )
    {
        final Optional<Continent> updated = original;
        if ( original.isPresent() )
        {
            // TODO setup ObjectMapper
            // ObjectMapper objectMapper;
            // jsonPatch.apply( objectMapper.convertValue( original.get(), JsonNode.class ) );
        }

        return updated;
    }

    // ===== PUT =====

    /**
     * Update an existing Continent entity.
     *
     * @param continentDTO the modified Continent entity.
     * @param requestHeaders request headers including traceID.
     * @return OK when updated, along with the updated entitty;
     *         CONFLICT when the underlying persistent object has been modified after
     *         it had been fetched.
     */
    @Operation( method = "PUT",
                summary = "Update a Continent",
                description = "Update a Continent only if it exists",
                requestBody = @RequestBody( required = true,
                                            content = { @Content( mediaType = "application/json",
                                                                  schema = @Schema( implementation = ContinentDTO.class ) )
                                            }
                ),
                responses =
                {
                    @ApiResponse( description = "Continent updated and returned",
                                  responseCode = "200",
                                  content = {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                    )
                },
                parameters = {
                    @Parameter( name = "Bearer",
                                required = true,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                }
    )
    @PutMapping( "" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO>
        restPutContinentById( @Valid @org.springframework.web.bind.annotation.RequestBody final ContinentDTO continentDTO,
                              @RequestHeader final HttpHeaders requestHeaders )
    {
        final Continent continent = updateService.update( mapper.apiToDomain( continentDTO ) );
        if ( null != continent )
        {
            // TODO handle Accept:application/json or Accept:application/XML
            return ResponseEntity
                    .status( HttpStatus.OK )
                    .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                    .body( mapper.domainToApi( continent ) );
        }

        // The item is not in the DB.  If the client intention is to insert,
        // then a POST should have been used.
        return ResponseEntity
                .status( HttpStatus.CONFLICT )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    // ========== DELETE ==========
    // ===== DELETE =====

    /**
     * Delete a Continent entity using its primary key.
     *
     * @param continentId unique identifier of the Continent entity.
     * @param requestHeaders HTTP headers, primarily for call tracing
     * @return NO_CONTENT when entity is deleted, NOT_FOUND when the entity has already been deleted.
     */
    @Operation( method = "DELETE",
                summary = "Delete a Continent by id",
                description = "Delete a Continent regardless if it exists or not.",
                responses =
                {
                    @ApiResponse( description = "Continent deleted",
                                  responseCode = "204",
                                  content = {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                    )
                },
                parameters = {
                    @Parameter( name = "continentId", required = true, in = ParameterIn.PATH, description = "Unique ID" ),
                    @Parameter( name = "Bearer",
                                required = true,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                }
    )
    @DeleteMapping( "/{continentId}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO>
        restDeleteContinentById( @Valid @PathVariable( name = "continentId" ) final Integer continentId,
                                 @RequestHeader final HttpHeaders requestHeaders )
    {
        // Delete is idempotent and will return NO_CONTENT regardless if
        // the item was deleted, or if it didn't exist.
        //  deleteService.deleteById( continentId );
        // return ResponseEntity.status( HttpStatus.GONE ).build();

        return ResponseEntity
                .status( deleteService.deleteById( continentId )
                         ? HttpStatus.NO_CONTENT
                         : HttpStatus.NOT_FOUND )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    /**
     * Deletes a Continent entity if it exists in persistent store.
     *
     * @param continentDTO the continent entity to delete
     * @param requestHeaders headers of the request, mostly for tracing.
     * @return ResponseEntity with no body.  Status NO_CONTENT indicates the entity
     *         was deleted; NOT_FOUND indicates the entity did not exist.
     */
    @DeleteMapping( "" )
    @SuppressWarnings( "PMD.ShortVariable" )
    @Operation( method = "DELETE",
                summary = "Delete a Continent",
                description = "Delete a Continent regardless if it exists or not.",
                requestBody = @RequestBody( required = true,
                                            content = { @Content( mediaType = "application/json",
                                                                  schema = @Schema( implementation = ContinentDTO.class ) )
                                            }
                ),
                responses = {
                    @ApiResponse( description = "Continent has been deleted",
                                  responseCode = "204",
                                  content = {
                                      @Content( mediaType = "application/json",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/yaml",
                                                schema = @Schema( implementation = ContinentDTO.class ) ),
                                      @Content( mediaType = "application/xml",
                                                schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                    )
                },
                parameters = {
                    @Parameter( name = "Bearer",
                                required = true,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                }
    )
    public ResponseEntity<ContinentDTO>
        restDelete( @Valid @org.springframework.web.bind.annotation.RequestBody final ContinentDTO continentDTO,
                    @RequestHeader final HttpHeaders requestHeaders )
    {
        // Delete is idempotent and will return NO_CONTENT regardless if
        // the item was deleted, or if it didn't exist.
        return ResponseEntity
                .status( deleteService.delete( mapper.apiToDomain( continentDTO ) )
                         ? HttpStatus.NO_CONTENT
                         : HttpStatus.NOT_FOUND )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    // ========== Administrative ==========
    // ===== HEAD =====
    /**
     * Handle HTTP Method HEAD for /location/continent.
     *
     * @param requestHeaders Request's HttpHeaders
     * @return {@see org.springframework.http.ResponseEntity} without a body.
     */
    @RequestMapping( value = "", method = RequestMethod.HEAD )
    public ResponseEntity<Void> restHeadContinent( @Valid @RequestHeader final HttpHeaders requestHeaders )
    {
        // This effectively needs to do the same as GET, but with an empty response body.
        // Headers are set for Content-Type and Content length, and the same status code.
        // "detail": "Request method 'DELETE' is not supported; Supported methods: HEAD, TRACE, POST, GET, OPTIONS",
        final ResponseEntity<List<ContinentDTO>> result = restGetFindAll( requestHeaders );

        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( result.getHeaders() )
                .build();
    }

    /**
     * Handle HTTP Method HEAD for /location/continent/{continentId}.
     *
     * @param continentId A continent ID.
     * @param requestHeaders Request's HttpHeaders
     * @return {@see org.springframework.http.ResponseEntity} without a body.
     */
    @RequestMapping( value = "/{continentId}", method = RequestMethod.HEAD )
    public ResponseEntity<Void>
        restHeadContinent_withID( @Valid @PathVariable( name = "continentId" ) final Integer continentId,
                                  @RequestHeader final HttpHeaders requestHeaders )
    {
        // This effectively needs to do the same as GET, but with an empty response body.
        // Headers are set for Content-Type and Content length, and the same status code.
        // "detail": "Request method 'DELETE' is not supported; Supported methods: HEAD, TRACE, POST, GET, OPTIONS",

        final ResponseEntity<ContinentDTO> result = restFindContinentById( continentId, requestHeaders );

        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( result.getHeaders() )
                .build();
    }

    // ===== INFO =====
    // @RequestMapping( value = "", method = RequestMethod.INFO )
    // public ResponseEntity<ContinentDTO> restInfoContinent( @RequestHeader HttpHeaders requestHeader )
    // {
    //     return ResponseEntity.noContent().build();
    // }

    // ===== OPTION =====
    /**
     * Handle HTTP Method OPTIONS for /location/continent.
     *
     * @param requestHeaders Request's HttpHeaders
     * @return {@see org.springframework.http.ResponseEntity} without a body.
     */
    @RequestMapping( value = "", method = RequestMethod.OPTIONS )
    public ResponseEntity<Void> restOptionsContinent( @Valid @RequestHeader final HttpHeaders requestHeaders )
    {
        // "detail": "Request method 'DELETE' is not supported; Supported methods: HEAD, TRACE, POST, GET, OPTIONS",
        // - [x] Allow: GET, HEAD, OPTIONS, POST, PUT, TRACE
        // - [x] Accept: application/json, application/yaml, application/xml
        // - [x] Content-Type: application/json, application/yaml, application/xml
        // - [ ] Access-Control-Allow-Origin: * or https://somedomain....
        // - [ ] Access-Control-Allow-Headers: Content-Type, Authorization

        final HttpHeaders responseHeaders = optionsHeaders( requestHeaders );
        return ResponseEntity
                .noContent()
                .headers( responseHeaders )
                .build();
    }

    // TODO build the header as a static the first time is is requested....
    private static HttpHeaders optionsHeaders( final HttpHeaders httpHeaders )
    {
        final HttpHeaders headers = null == httpHeaders
                                    ? new HttpHeaders()
                                    : HeaderUtility.copyNeededHeaders( httpHeaders );

        final List<HttpMethod> allows = List.of(
                HttpMethod.DELETE,
                HttpMethod.GET,
                HttpMethod.HEAD,
                HttpMethod.OPTIONS,
                HttpMethod.PATCH,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.TRACE
                                               );
        final String allowsString    =
                allows
                        .stream()
                        .map( HttpMethod::name )
                        .collect( Collectors.joining( "," ) );
        headers.add( HttpHeaders.ALLOW, allowsString );

        final List<MediaType> mediaType = List.of( MediaType.APPLICATION_JSON,
                                                   MediaType.APPLICATION_YAML,
                                                   MediaType.APPLICATION_XML );
        final String mediaTypeString =
                mediaType
                        .stream()
                        .map( MediaType::toString )
                        .collect( Collectors.joining( "," ) );
        headers.add( HttpHeaders.ACCEPT, mediaTypeString );

        return headers;
    }

    // ===== TRACE =====
    /**
     * Handle HTTP Method TRACE for /location/continent.
     *
     * @param requestHeaders Request's HttpHeaders
     * @return {@see org.springframework.http.ResponseEntity} without a body.
     */
    @Operation( method = "TRACE",
                summary = "TRACE Continent",
                description = "Continent API TRACE.",
                responses = {
                    @ApiResponse(
                            description = "TRACE......",
                            responseCode = "200",
                            content =
                            {
                                @Content( mediaType = "application/json",
                                          schema = @Schema( implementation = ContinentDTO.class ) ),
                                @Content( mediaType = "application/yaml",
                                          schema = @Schema( implementation = ContinentDTO.class ) ),
                                @Content( mediaType = "application/xml",
                                          schema = @Schema( implementation = ContinentDTO.class ) )
                            }
                    )
                },
                parameters = {
                    @Parameter( name = "Bearer",
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                }
    )
    @RequestMapping( value = "", method = RequestMethod.TRACE )
    public ResponseEntity<Void> restTraceContinent( @Valid @RequestHeader final HttpHeaders requestHeaders )
    {
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .location( location )
                .build();
    }

    /**
     * Handle HTTP Method TRACE for /location/continent/{continentId}.
     *
     * @param continentId A continent ID.
     * @param requestHeaders Request's HttpHeaders
     * @return {@see org.springframework.http.ResponseEntity} without a body.
     */
    @Operation( method = "TRACE",
                summary = "TRACE Continent",
                description = "Continent API TRACE.",
                responses = {
                    @ApiResponse(
                            description = "TRACE......",
                            responseCode = "200",
                            content =
                            {
                                @Content( mediaType = "application/json",
                                          schema = @Schema( implementation = ContinentDTO.class ) ),
                                @Content( mediaType = "application/yaml",
                                          schema = @Schema( implementation = ContinentDTO.class ) ),
                                @Content( mediaType = "application/xml",
                                          schema = @Schema( implementation = ContinentDTO.class ) )
                            }
                    )
                },
                parameters = {
                    @Parameter( name = "Bearer",
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" ),
                    @Parameter( name = HeaderUtility.TRACEID,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Distributed tracing identifier" ),
                    @Parameter( name = HeaderUtility.TRACESTATE,
                                required = false,
                                schema = @Schema( implementation = String.class ),
                                in = ParameterIn.HEADER,
                                description = "Vendor specific trace identification" )
                }
    )
    @RequestMapping( value = "/{continentId}", method = RequestMethod.TRACE )
    public ResponseEntity<Void> restTraceContinent( @Valid @PathVariable( name = "continentId" ) final Integer continentId,
                                                    @RequestHeader final HttpHeaders requestHeaders )
    {
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .location( location )
                .build();
    }

}
