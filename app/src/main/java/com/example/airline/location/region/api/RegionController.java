/* (C) 2025 */

package com.example.airline.location.region.api;

import java.util.Optional;

import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.region.RegionDTO;
import com.example.airline.location.region.mapper.RegionDtoMapper;
import com.example.airline.location.region.model.Region;
import com.example.airline.location.region.service.RegionCreateService;
import com.example.airline.location.region.service.RegionDeleteService;
import com.example.airline.location.region.service.RegionReadService;
import com.example.airline.location.region.service.RegionUpdateService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * API controller for managing regions.
 *
 * <p>This controller provides endpoints to retrieve region information.
 *
 * <p>The API supports pagination and returns data in JSON, YAML, and XML formats.
 *
 */
@RestController
@RequestMapping( "/location/region" )
@GlobalApiResponses
@GlobalApiSecurityResponses
@Tag( name = "Regions" )
public class RegionController
{
    // Autowired via constructor
    private final RegionCreateService createService;
    private final RegionReadService   readService;
    private final RegionUpdateService updateService;
    private final RegionDeleteService deleteService;
    private final RegionDtoMapper     mapper;


    /**
     *
     * @param createService service to create instances
     * @param readService service to read instances
     * @param updateService service to update instances
     * @param deleteService service to delete instances
     * @param mapper The mapper to convert between domain and API objects.
     */
    public RegionController( final RegionCreateService createService,
                             final RegionReadService   readService,
                             final RegionUpdateService updateService,
                             final RegionDeleteService deleteService,
                             final RegionDtoMapper mapper )
    {
        this.createService = createService;
        this.readService   = readService;
        this.updateService = updateService;
        this.deleteService = deleteService;
        this.mapper  = mapper;
    }

    // ========== CREATE ==========
    // ===== POST =====

    // ========== READ ==========
    // ===== GET =====
    // --- Single ---
    /**
     * Find a Region by ID.
     *
     * @param id The primary key of the region.
     * @return A ResponseEntity containing the RegionDTO object if found, or no content if not found.
     */
    @Operation( method = "GET",
                summary = "Find a Region by Id",
                description = "Find a Region by Id",
                requestBody = @RequestBody( required = false ),
                responses = { @ApiResponse( description = "Success",
                                            responseCode = "200",
                                            content = {
                                                    @Content( mediaType = "application/json",
                                                              schema = @Schema( implementation = RegionDTO.class ) ),
                                                    @Content( mediaType = "application/yaml",
                                                              schema = @Schema( implementation = RegionDTO.class ) ),
                                                    @Content( mediaType = "application/xml",
                                                              schema = @Schema( implementation = RegionDTO.class ) )
                                            }
                )
                },
                parameters = {
                        @Parameter( name = "id",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    description = "Primary Key" ),
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
    @GetMapping( "/{id}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<RegionDTO> restGetFindRegionById( @PathVariable final Integer id )
    {
        final Optional<Region> optionalRegions = readService.findRegionById( id );

        if ( optionalRegions.isPresent() )
        {
            final RegionDTO dto = mapper.domainToApi( optionalRegions.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    /**
     * Find a Region by code.
     *
     * @param code The code of the region.
     * @return A ResponseEntity containing the RegionDTO object if found, or no content if not found.
     */
    @Operation( method = "GET",
                summary = "Find a Region by its abbreviation",
                description = "Find a Region by its 3-7 letter code",
                responses = {
                        @ApiResponse( description = "Region found and returned",
                                      responseCode = "200",
                                      content = {
                                              @Content( mediaType = "application/json",
                                                        schema = @Schema( implementation = RegionDTO.class ) ),
                                              @Content( mediaType = "application/yaml",
                                                        schema = @Schema( implementation = RegionDTO.class ) ),
                                              @Content( mediaType = "application/xml",
                                                        schema = @Schema( implementation = RegionDTO.class ) )
                                      }
                        )
                },
                parameters = {
                        @Parameter( name = "code",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    description = "3-7 character code" ),
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
    public ResponseEntity<RegionDTO> restGetFindRegionByCode( @Valid @PathVariable final String code,
                                                              @RequestHeader final HttpHeaders requestHeader )
    {
        final Optional<Region> optionalEntity = readService.findRegionByCode( code );

        if ( optionalEntity.isPresent() )
        {
            final RegionDTO dto = mapper.domainToApi( optionalEntity.get() );

            final ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status( HttpStatusCode.valueOf( 200 ) );

            bodyBuilder.contentType( requestHeader.getContentType() );
            bodyBuilder.contentLength( dto.toString().length() );

            return bodyBuilder.body( dto );
        }

        // may include instance in header.....
        return ResponseEntity
                .noContent()
                .location( requestHeader.getLocation() )
                .build();
    }

    // --- Multiple ---
    /**
     * Find all Regions.
     *
     * @param pageable The pagination information.
     * @return A page of RegionDTO objects.
     */
    @GetMapping( "" )
    public Page<RegionDTO> restGetFindAll( final Pageable pageable )
    {
        final Page<Region> regions = readService.findAll( pageable );

        // var zzz = regions.map( mapper::domainToApi );

//        ResponseEntity.BodyBuilder bb = ResponseEntity.status( HttpStatusCode.valueOf( 200 ) );
//        bb.contentLength( zzz.getSize() );
//        bb.contentType(  )
//        return bb.body( zzz );
        return regions.map( mapper::domainToApi );
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
