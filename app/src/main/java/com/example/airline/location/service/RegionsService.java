/* (C) 2025 */

package com.example.airline.location.service;


import java.util.Optional;

import com.example.airline.location.Region;
import com.example.airline.location.mapper.RegionMapper;
import com.example.airline.location.persistence.model.location.RegionEntity;
import com.example.airline.location.persistence.repository.location.RegionsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;



@Component
// @RefreshScope
public class RegionsService
{
    private final RegionsRepository repository;

    private final RegionMapper mapper;

    public RegionsService( final RegionsRepository repository, final RegionMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    public Page<Region> findAll( final Pageable pageable )
    {
        final Page<RegionEntity> regions = repository.findAll( pageable );

        return regions.map( mapper::regionEntityToDomain );
    }



    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Region> findRegionById( final Integer id )
    {
        final Optional<RegionEntity> countryEntity = repository.findById( id );

        if ( countryEntity.isPresent() )
        {
            final Region country = mapper.regionEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }



    public Optional<Region> findRegionByCode( final String code )
    {
        final Optional<RegionEntity> countryEntity = repository.findByCode( code );

        if ( countryEntity.isPresent() )
        {
            final Region country = mapper.regionEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }

}
