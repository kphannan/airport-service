/* (C) 2025 */

package com.example.airline.location.continent.api;


import java.net.URI;
import java.nio.charset.StandardCharsets;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
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

    private final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
                                                                StandardCharsets.UTF_8 );


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


    // ===================
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
                responses = { @ApiResponse( description = "All Continents found and returned in an array",
                                            responseCode = "200",
                                            content = {
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
                parameters = {
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
        final List<Continent> continents = readService.findAll();

        final List<ContinentDTO> dtos = mapper.domainToApi( continents );

        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .body( dtos );
    }


    /**
     * Find a Continent by Id.
     *
     * @param continentId    The primary key of the Continent to find.
     * @param requestHeaders Request headers with authentication and tracing information.
     * @return the ContinentDTO object if found, or a 204 No Content response if not found.
     */
    @Operation( method = "GET",
                summary = "Find a Continent by Id",
                description = "Find a Continent by Id",
                requestBody = @RequestBody( required = false ),
                responses = { @ApiResponse( description = "Continent found and returned",
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
        restGetFindContinentById( @Valid @PathVariable( name = "continentId" ) final Integer continentId,
                                  @RequestHeader final HttpHeaders requestHeaders )
    {
        final Optional<Continent> optionalContinent = readService.getReferenceById( continentId );

        if ( optionalContinent.isPresent() )
        {
            final ContinentDTO dto = mapper.domainToApi( optionalContinent.get() );

//            final ResponseEntity<ContinentDTO> response = new ResponseEntity<>( dto,
//                                                                                HeaderUtility.copyNeededHeaders( requestHeaders ),
////                                                                          responseHeaders( requestHeaders, desiredContentType ),
//                                                                          HttpStatus.OK );
////            return response;
//            ResponseEntity.BodyBuilder bb = ResponseEntity.status( HttpStatusCode.valueOf( 200 ) );
//            bb.contentType( requestHeader.getContentType() );
//            return bb.body( dto );
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




    // ===================
    // ===== POST =====
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
    public ResponseEntity<ContinentDTO>
        restPostAddContinent( @Valid @org.springframework.web.bind.annotation.RequestBody final NewContinentDTO newContinentDTO,
                              @RequestHeader final HttpHeaders requestHeaders )
    {
        final Continent continent = createService.create( mapper.apiToDomain( newContinentDTO ) );
        if ( null != continent )
        {
            // Build the resource id (path) of the newly created item
            final URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path( "/{continentId}" )
                    .buildAndExpand( continent.getId() )
                    .toUri();
            final ResponseEntity<ContinentDTO> response = ResponseEntity
                    .created( location )
                    .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                    .body( mapper.domainToApi( continent ) );

            log.error( response.toString() );

            return response;
        }

        // The item is already in the DB.  If the client intention is to update,
        // then a PUT should have been used.
        return ResponseEntity
                .status( HttpStatus.CONFLICT )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                //.body( "Continent does not exist" )
                .build();
    }



    // ===================
    // ===== PUT =====
    @Operation( method = "PUT",
                summary = "Update a Continent",
                description = "Update a Continent only if it exists",
                requestBody = @RequestBody( required = true,
                                            content = { @Content( mediaType = "application/json",
                                                                  schema = @Schema( implementation = ContinentDTO.class ) )
                                            }
                ),
                responses = { @ApiResponse( description = "Continent updated and returned",
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

    // ===================
    // ===== DELETE =====
    @Operation( method = "DELETE",
                summary = "Delete a Continent by id",
                description = "Delete a Continent regardless if it exists or not.",
                responses = { @ApiResponse( description = "Continent deleted",
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

    // ===================
    // ===== PATCH =====

    /**
     * Handle HTTP Method PATCH for /location/continent/{continentId}.
     *
     * @param continent A modified continent instance.
     * @param requestHeaders Request's HttpHeaders
     * @return
     */
    @Operation( method = "PATCH",
                summary = "Update a Continent",
                description = "Update a Continent only if it exists.  All non-null attributes are updated.",
                requestBody =
                    @RequestBody( required = true,
                                  content = { @Content( mediaType = "application/json",
                                                        schema = @Schema( implementation = ContinentDTO.class ) )
                                  }
                ),
                responses = {
                    @ApiResponse(
                        description = "Continent updated and returned",
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
    @PatchMapping( "" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<ContinentDTO> restPatchContinentById( @Valid @RequestBody final ContinentDTO continent,
                                                                @RequestHeader final HttpHeaders requestHeaders )
    {
        // TODO implement PATCH
        return ResponseEntity
                .noContent()
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    // ===================
    // ===== Options =====

    /**
     * Handle HTTP Method OPTIONS for /location/continent.
     *
     * @param requestHeaders Request's HttpHeaders
     * @return
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

        final HttpHeaders responseHeaders = optionsHeaders();
        responseHeaders.set( HeaderUtility.TRACEID, requestHeaders.getFirst(  HeaderUtility.TRACEID ) );
        responseHeaders.set( HeaderUtility.TRACESTATE, requestHeaders.getFirst(  HeaderUtility.TRACESTATE ) );
        return ResponseEntity
                .noContent()
                .headers( responseHeaders )
                .build();
    }


    // TODO build the header as a static the first time is is requested....
    private static HttpHeaders optionsHeaders()
    {
        final HttpHeaders headers = new HttpHeaders();

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
        final List<MediaType> mediaType = List.of( MediaType.APPLICATION_JSON,
                                             MediaType.APPLICATION_YAML,
                                             MediaType.APPLICATION_XML );
        final String allowsString    =
                allows
                        .stream()
                        .map( HttpMethod::name )
                        .collect( Collectors.joining( "," ) );
        final String mediaTypeString =
                mediaType
                        .stream()
                        .map( MediaType::toString )
                        .collect( Collectors.joining( "," ) );

        headers.add( HttpHeaders.ALLOW, allowsString );
        headers.add( HttpHeaders.ACCEPT, mediaTypeString );

        return headers;
    }

    // ===================
    // ===== HEAD =====

    /**
     * Handle HTTP Method HEAD for /location/continent.
     *
     * @param requestHeaders Request's HttpHeaders
     * @return
     */
    @RequestMapping( value = "", method = RequestMethod.HEAD )
    public ResponseEntity<Void> restHeadContinent( @Valid @RequestHeader final HttpHeaders requestHeaders )
    {
        // This effectively needs to do the same as GET, but with an empty response body.
        // Headers are set for Content-Type and Content length, and the same status code.
        // "detail": "Request method 'DELETE' is not supported; Supported methods: HEAD, TRACE, POST, GET, OPTIONS",

        // HttpHeaders headers = new HttpHeaders();
        // headers.add( HttpHeaders.CONTENT_TYPE, requestHeaders.getAccept().toString() );
        // ResponseEntity<ContinentDTO> rr = restGetFindContinentById( continentId, requestHeaders );

        return ResponseEntity
                .noContent()
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    /**
     * Handle HTTP Method HEAD for /location/continent/{continentId}.
     *
     * @param continentId A continent ID.
     * @param requestHeaders Request's HttpHeaders
     * @return
     */
    @RequestMapping( value = "/{continentId}", method = RequestMethod.HEAD )
    public ResponseEntity<Void>
        restHeadContinent_withID( @Valid @PathVariable( name = "continentId" ) final Integer continentId,
                                  @RequestHeader final HttpHeaders requestHeaders )
    {
        // This effectively needs to do the same as GET, but with an empty response body.
        // Headers are set for Content-Type and Content length, and the same status code.
        // "detail": "Request method 'DELETE' is not supported; Supported methods: HEAD, TRACE, POST, GET, OPTIONS",

        // HttpHeaders headers = new HttpHeaders();
        // headers.add( HttpHeaders.CONTENT_TYPE, requestHeaders.getAccept().toString() );
        final ResponseEntity<ContinentDTO> rr = restGetFindContinentById( continentId, requestHeaders );

        return new ResponseEntity<>( rr.getHeaders(),
                                     //  HttpStatusCode.valueOf( 200 )
                                     rr.getStatusCode().equals( HttpStatusCode.valueOf( 200 ) )
                                        ? HttpStatus.NO_CONTENT : rr.getStatusCode() );
    }

    // ===== INFO =====
    // @RequestMapping( value = "", method = RequestMethod.INFO )
    // public ResponseEntity<ContinentDTO> restInfoContinent( @RequestHeader HttpHeaders requestHeader )
    // {
    //     return ResponseEntity.noContent().build();
    // }

    // ===================
    // ===== TRACE =====

    /**
     * Handle HTTP Method TRACE for /location/continent.
     *
     * @param requestHeaders Request's HttpHeaders
     * @return
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
        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .build();
    }

    /**
     * Handle HTTP Method TRACE for /location/continent/{continentId}.
     *
     * @param continentId A continent ID.
     * @param requestHeaders Request's HttpHeaders
     * @return
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
                .path( "/{continentId}" )
                .buildAndExpand( continentId )
                .toUri();
        return ResponseEntity
                .status( HttpStatus.OK )
                .headers( HeaderUtility.copyNeededHeaders( requestHeaders ) )
                .location( location )
                .build();
    }

}
