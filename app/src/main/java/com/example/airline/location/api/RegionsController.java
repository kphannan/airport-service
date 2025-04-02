/* (C) 2025 */

package com.example.airline.location.api;


import java.util.Optional;

import com.example.airline.location.Region;
import com.example.airline.location.RegionDTO;
import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.mapper.RegionMapper;
import com.example.airline.location.service.RegionsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping( "/v1/location/region" )
@GlobalApiResponses
@GlobalApiSecurityResponses
@Tag( name = "Regions" )
public class RegionsController
{
    // Autowired via constructor
    private final RegionsService service;
    private final RegionMapper   mapper;

    public RegionsController( final RegionsService service, final RegionMapper mapper )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    @GetMapping( "" )
    public Page<RegionDTO> restGetFindAll( final Pageable pageable )
    {
        final Page<Region> regions = service.findAll( pageable );

        return regions.map( entity -> mapper.regionDomainToApi( entity ) );
    }



    @GetMapping( "/{id}" )
    @SuppressWarnings( "PMD.ShortVariable" )
    public ResponseEntity<RegionDTO> restGetFindRegionById( @PathVariable final Integer id )
    {
        final Optional<Region> optionalRegions = service.findRegionById( id );

        if ( optionalRegions.isPresent() )
        {
            final RegionDTO dto = mapper.regionDomainToApi( optionalRegions.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping( "/code/{code}" )
    public ResponseEntity<RegionDTO> restGetFindRegionByCode( @PathVariable final String code )
    {
        final Optional<Region> optionalEntity = service.findRegionByCode( code );

        if ( optionalEntity.isPresent() )
        {
            final RegionDTO dto = mapper.regionDomainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }
}
