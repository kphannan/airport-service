/* (C) 2025 */

package com.example.airline.location.api;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.example.airline.location.Continent;
import com.example.airline.location.ContinentDTO;
import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.mapper.ContinentMapper;
import com.example.airline.location.service.ContinentService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private final ContinentMapper mapper;
//    private final List<MediaType> restMediaTypes = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );


    public ContinentController( final ContinentService service, final ContinentMapper mapper )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    @Operation( method = "GET",
                summary = "Retrieve all Continents in paged form for performance",
                description = "Retrieve a paged list of Continents",

                responses = { @ApiResponse( description = "Success",
                        responseCode = "200",
                        content = { @Content( mediaType = "application/json" /*, schema = @Schema( implementation = ContinentDTO.class ) */ ),
                                    @Content( mediaType = "application/yaml" /*, schema = @Schema( implementation = ContinentDTO.class ) */ ),
                                    @Content( mediaType = "application/xml"  /*, schema = @Schema( implementation = ContinentDTO.class ) */ )
                        }
                )
                }
//                responses = {
//                    @ApiResponse( description = "Success", responseCode = "200" )
            // @ApiResponse( description = "Unauthorized", responseCode = "403" )
//    }
    )
    @GetMapping( "" )
    public ResponseEntity<List<ContinentDTO>> restGetFindAll()
    {
        final List<Continent> continents = service.findAll();

        final List<ContinentDTO> dtos = mapper.continentDomainToApi( continents );

        return ResponseEntity.ok( dtos );
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
                                            content = { @Content( mediaType = "application/json", schema = @Schema( implementation = ContinentDTO.class ) ),
                                                        @Content( mediaType = "application/yaml", schema = @Schema( implementation = ContinentDTO.class ) ),
                                                        @Content( mediaType = "application/xml", schema = @Schema( implementation = ContinentDTO.class ) )
                                            }
                              )
                },
                parameters = { @Parameter( name = "id", required = true, in = ParameterIn.PATH, description = "Primary Key" ),
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
    public ResponseEntity<ContinentDTO> restGetFindContinentById( @PathVariable final Integer id, @RequestHeader HttpHeaders requestHeader )
//    public ResponseEntity<ContinentDTO> restGetFindContinentById( @PathVariable final Integer id )
    {
        final Optional<Continent> optionalContinent = service.findContinentById( id );

        if ( optionalContinent.isPresent() )
        {
            final ContinentDTO dto = mapper.continentDomainToApi( optionalContinent.get() );

            return new ResponseEntity<ContinentDTO>( dto, HttpStatus.OK );
        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping( "/code/{code}" )
//    public ResponseEntity<ContinentDTO> restGetFindContinentByCode( @PathVariable final String code, @RequestHeader HttpHeaders requestHeader )
    public ResponseEntity<ContinentDTO> restGetFindContinentByCode( @PathVariable final String code, @RequestHeader HttpHeaders requestHeader )
    {
        final Optional<Continent> optionalEntity = service.findContinentByCode( code );

        if ( optionalEntity.isPresent() )
        {
            final ContinentDTO dto = mapper.continentDomainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }



    private boolean containsAny( Collection source, Collection target )
    {
        for ( Object t : target )
            if ( source.contains( t ) )
                return true;

        return false;
    }
}
