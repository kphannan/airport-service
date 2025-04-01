/* (C)2025 */

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
    private RegionsRepository repository;

    private RegionMapper     mapper;



    public RegionsService( RegionsRepository repository, RegionMapper mapper )
    {
        this.repository = repository;
        this.mapper = mapper;
    }



    public Page<Region> findAll( final Pageable pageable )
    {
        Page<RegionEntity> regions = repository.findAll( pageable );

        return regions.map( entity -> mapper.regionEntityToDomain(entity));
    }



    public Optional<Region> findRegionById( final Integer id )
    {
        Optional<RegionEntity> countryEntity = repository.findById( id );

        if ( countryEntity.isPresent() )
        {
            Region country = mapper.regionEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }
        else
        {
            return Optional.ofNullable( null );
        }
    }



    public Optional<Region> findRegionByCode( final String code )
    {
        Optional<RegionEntity> countryEntity = repository.findByCode( code );

        if ( countryEntity.isPresent() )
        {
            Region country = mapper.regionEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }
        else
        {
            return Optional.ofNullable( null );
        }

    }

}
