/* (C) 2025 */

package com.example.airline.location.country.api;


import java.util.Optional;

import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.country.CountryDTO;
import com.example.airline.location.country.mapper.CountryDtoMapper;
import com.example.airline.location.country.model.Country;
import com.example.airline.location.country.service.CountryService;
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
 * API controller for managing countries.
 *
 * This controller provides endpoints to retrieve country information.
 *
 * The API supports pagination and returns data in JSON, YAML, and XML formats.
 */
@RestController
@RequestMapping( "/location/country" )
@Tag( name = "Countries" )
@GlobalApiResponses
@GlobalApiSecurityResponses
public class CountryController
{
    // Autowired via constructor
    private final CountryService   service;
    private final CountryDtoMapper mapper;

    /**
     * Constructor for the CountryController.
     *
     * @param service The service to use for country operations.
     * @param mapper  The mapper to convert between domain and API objects.
     */
    public CountryController( final CountryService service, final CountryDtoMapper mapper )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    /**
     * Find all Countries.
     *
     * @param pageable The pagination information.
     * @return A Page of CountryDTO objects.
     */
    @GetMapping( "" )
    public Page<CountryDTO> restGetFindAll( final Pageable pageable )
    {
        final Page<Country> countries = service.findAll( pageable );

        return countries.map( mapper::domainToApi );
    }



    /**
     * Find a Country by Id.
     *
     * @param id The primary key of the country to find.
     * @return A ResponseEntity containing the found CountryDTO or no content if not found.
     */
    @Operation( method = "GET",
            summary = "Find a Continent by Id",
            description = "Find a Continent by Id",
            requestBody = @RequestBody( required = false ),
            responses = { @ApiResponse( description = "Success",
                    responseCode = "200",
                    content = { @Content( mediaType = "application/json",
                                          schema = @Schema( implementation = CountryDTO.class ) ),
                                @Content( mediaType = "application/yaml",
                                          schema = @Schema( implementation = CountryDTO.class ) ),
                                @Content( mediaType = "application/xml",
                                          schema = @Schema( implementation = CountryDTO.class ) )
                    }
            )
            },
            parameters = { @Parameter( name = "id",
                                       required = true,
                                       in = ParameterIn.PATH,
                                       description = "Primary Key" ),
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
    public ResponseEntity<CountryDTO> restGetFindCountryById( @PathVariable final Integer id )
    {
        final Optional<Country> optionalCountry = service.findCountryById( id );

        if ( optionalCountry.isPresent() )
        {
            final CountryDTO dto = mapper.domainToApi( optionalCountry.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    /**
     * Find a Country by code.
     *
     * @param code The 2-character alpha code of the country to find.
     * @return A ResponseEntity containing the found CountryDTO or no content if not found.
     */
    @GetMapping( "/code/{code}" )
    public ResponseEntity<CountryDTO> restGetFindCountryByCode( @PathVariable final String code )
    {
        final Optional<Country> optionalEntity = service.findCountryByCode( code );

        if ( optionalEntity.isPresent() )
        {
            final CountryDTO dto = mapper.domainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }
}
