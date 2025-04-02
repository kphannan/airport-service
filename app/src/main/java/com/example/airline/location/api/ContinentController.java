/* (C)2025 */

package com.example.airline.location.api;


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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping( "/v1/location/continent" )
@Tag( name = "Continents" )
@GlobalApiResponses
@GlobalApiSecurityResponses
public class ContinentController
{
    // Autowired via constructor
    private ContinentService service;
    private ContinentMapper     mapper;


    public ContinentController( ContinentService service, ContinentMapper mapper  )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    @Operation( method = "GET",
                summary = "Retrieve all Continents in paged form for performance",
                description = "Retrive a paged list of Continents",
                responses = {
                    @ApiResponse( description = "Success", responseCode = "200" )
                    // @ApiResponse( description = "Unauthorized", responseCode = "403" )
                }
    )
    @GetMapping( "" )
    public ResponseEntity<List<ContinentDTO>> restGetFindAll()
    {
        List<Continent> continents = service.findAll();

        List<ContinentDTO> dtos = mapper.continentDomainToApi( continents );

        return ResponseEntity.ok( dtos );
    }



    @Operation( method = "GET",
                summary = "Find a Continent by Id",
                description = "Find a Continent by Id",
                responses = {
                    @ApiResponse( description = "Success",
                                  responseCode = "200",
                                  content = { @Content( mediaType = "application/json",
                                                        schema = @Schema( implementation = ContinentDTO.class )
                                                      )
                                            }
                                  )
                },
                parameters = {
                    @Parameter( name = "id", required = true, in = ParameterIn.PATH ),
                    @Parameter( name        = "Bearer",
                                required    = false,
                                schema      = @Schema( implementation = String.class ),
                                in          = ParameterIn.HEADER,
                                description = "Authentication / Authorization token" )
                }
    )
    @GetMapping( "/{id}" )
    public ResponseEntity<ContinentDTO> restGetFindContinentById( @PathVariable final Integer id )
    {
        final Optional<Continent> optionalContinent = service.findContinentById( id );

        if ( optionalContinent.isPresent() )
        {
            ContinentDTO dto = mapper.continentDomainToApi( optionalContinent.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping( "/code/{code}" )
    public ResponseEntity<ContinentDTO> restGetFindContinentByCode( @PathVariable final String code )
    {
        Optional<Continent> optionalEntity = service.findContinentByCode( code );

        if ( optionalEntity.isPresent() )
        {
            ContinentDTO dto = mapper.continentDomainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }
}
