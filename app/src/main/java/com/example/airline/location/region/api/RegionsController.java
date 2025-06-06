/* (C) 2025 */

package com.example.airline.location.region.api;


import java.util.Optional;

import com.example.airline.location.region.mapper.RegionDtoMapper;
import com.example.airline.location.region.model.Region;
import com.example.airline.location.RegionDTO;
import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.region.service.RegionsService;
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
import org.springframework.web.bind.annotation.RestController;



/**
 * API controller for managing regions.
 * <p>
 * This controller provides endpoints to retrieve region information.
 * </p>
 * <p>
 * The API supports pagination and returns data in JSON, YAML, and XML formats.
 * </p>
 */
@RestController
@RequestMapping( "/location/region" )
@GlobalApiResponses
@GlobalApiSecurityResponses
@Tag( name = "Regions" )
public class RegionsController
{
    // Autowired via constructor
    private final RegionsService  service;
    private final RegionDtoMapper mapper;

    /**
     * Constructor for the RegionsController.
     *
     * @param service The service to use for region operations.
     * @param mapper  The mapper to convert between domain and API objects.
     */
    public RegionsController( final RegionsService service, final RegionDtoMapper mapper )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    /**
     * Find all Regions.
     *
     * @param pageable The pagination information.
     * @return A page of RegionDTO objects.
     */
    @GetMapping( "" )
    public Page<RegionDTO> restGetFindAll( final Pageable pageable )
    {
        final Page<Region> regions = service.findAll( pageable );

        return regions.map( mapper::domainToApi );
    }



    /**
     * Find a Region by Id.
     *
     * @param id The primary key of the region.
     * @return A ResponseEntity containing the RegionDTO object if found, or no content if not found.
     */
    @Operation( method = "GET",
            summary = "Find a Continent by Id",
            description = "Find a Continent by Id",
            requestBody = @RequestBody( required = false ),
            responses = { @ApiResponse( description = "Success",
                    responseCode = "200",
                    content = { @Content( mediaType = "application/json", schema = @Schema( implementation = RegionDTO.class ) ),
                                @Content( mediaType = "application/yaml", schema = @Schema( implementation = RegionDTO.class ) ),
                                @Content( mediaType = "application/xml", schema = @Schema( implementation = RegionDTO.class ) )
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
    public ResponseEntity<RegionDTO> restGetFindRegionById( @PathVariable final Integer id )
    {
        final Optional<Region> optionalRegions = service.findRegionById( id );

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
    @GetMapping( "/code/{code}" )
    public ResponseEntity<RegionDTO> restGetFindRegionByCode( @PathVariable final String code )
    {
        final Optional<Region> optionalEntity = service.findRegionByCode( code );

        if ( optionalEntity.isPresent() )
        {
            final RegionDTO dto = mapper.domainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }
}
