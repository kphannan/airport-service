/* (C) 2025 */

package com.example.airline.location.service;


import java.util.Optional;

import com.example.airline.location.Region;
import com.example.airline.location.mapper.RegionMapper;
import com.example.airline.location.persistence.model.location.RegionEntity;
import com.example.airline.location.persistence.repository.location.RegionsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Region domain object.
 */
@Service
public class RegionsService
{
    private final RegionsRepository repository;

    private final RegionMapper mapper;

    /**
     * Create a RegionService supported by autowire.
     *
     * @param repository jpa repository of Regions
     * @param mapper maps entities to/from the domain model
     */
    public RegionsService( final RegionsRepository repository, final RegionMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    /**
     * Retrieve a paged list of Regions.
     *
     * @param pageable the page control structure.
     * @return a page of Regions.
     */
    public Page<Region> findAll( final Pageable pageable )
    {
        final Page<RegionEntity> regions = repository.findAll( pageable );

        return regions.map( mapper::entityToDomain );
    }



    /**
     * Find a region by its id.
     *
     * @param id the id of the desired region.
     * @return the optional region if it was found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Region> findRegionById( final Integer id )
    {
        final Optional<RegionEntity> countryEntity = repository.findById( id );

        if ( countryEntity.isPresent() )
        {
            final Region country = mapper.entityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }



    /**
     * Find a region by its standard letter code.
     *
     * @param code the letter code.
     * @return optionally, the reqion corresponding to the code.
     */
    public Optional<Region> findRegionByCode( final String code )
    {
        final Optional<RegionEntity> countryEntity = repository.findByCode( code );

        if ( countryEntity.isPresent() )
        {
            final Region country = mapper.entityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }

}
