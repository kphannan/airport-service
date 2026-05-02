/* (C) 2025 */

package com.example.airline.location.region.service;


import java.util.Optional;

import com.example.airline.location.region.mapper.RegionEntityMapper;
import com.example.airline.location.region.model.Region;
import com.example.airline.location.region.persistence.model.RegionEntity;
import com.example.airline.location.region.persistence.repository.RegionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Region domain object.
 */
@Service
public class RegionService
{
    private final RegionRepository repository;

    private final RegionEntityMapper mapper;



    /*
     *   API (Controller)
     *       Create
     *           Post
     *       Read
     *           Get
     *       Update
     *           Patch
     *           Put
     *       Delete
     *           Delete
     *       Administrative
     *           Head
     *           Info
     *           Opt
     *           Trace
     *
     *   Config
     *   mapper
     *   model
     *   persistence
     *
     *   Service
     *       Create
     *       Read
     *       Update
     *       Delete
     */


    /**
     * Create a RegionService supported by autowire.
     *
     * @param repository jpa repository of Regions
     * @param mapper maps entities to/from the domain model
     */
    public RegionService( final RegionRepository repository, final RegionEntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }


    // ========== CREATE ==========
    // ===== POST =====

    // ========== READ ==========
    // ===== GET =====
    // --- Single ---
    /**
     * Find a region by its id.
     *
     * @param id the id of the desired region.
     * @return the optional region if it was found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Region> findRegionById( final Integer id )
    {
        return mapOptionalEntityToDomain( repository.findById( id ) );
    }



    /**
     * Find a region by its standard letter code.
     *
     * @param code the letter code.
     * @return optionally, the reqion corresponding to the code.
     */
    public Optional<Region> findRegionByCode( final String code )
    {
        return mapOptionalEntityToDomain( repository.findByCode( code ) );
    }

    // --- Multiple ---
    /**
     * Retrieve a paged list of Regions.
     *
     * @param pageable the page control structure.
     * @return a page of Regions.
     */
    public Page<Region> findAll( final Pageable pageable )
    {
        final Page<RegionEntity> pageItems = repository.findAll( pageable );

        return pageItems.map( mapper::entityToDomain );
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













    private Optional<Region> mapOptionalEntityToDomain( final Optional<RegionEntity> from )
    {
        if ( from.isPresent() )
        {
            final Region item = mapper.entityToDomain( from.get() );

            return Optional.of( item );
        }

        return Optional.empty();
    }

    /*
    private <T,S> Optional<T> mapOptional( final Optional<S> from )
    {
        if ( from.isPresent() )
        {
            final T item = mapper.entityToDomain( from.get() );

            return Optional.of( item );
        }

        return Optional.ofNullable( null );
    }
    */

}
