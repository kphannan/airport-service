/* (C) 2025 */

package com.example.airline.location.region.service;


import com.example.airline.location.region.mapper.RegionEntityMapper;
import com.example.airline.location.region.model.Region;
import com.example.airline.location.region.persistence.repository.RegionRepository;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the ReGion domain object.
 */
@Service
public class RegionReadService
{
    private final RegionRepository repository;

    private final RegionEntityMapper mapper;

    /**
     * Create a RegionReadService supported by autowire.
     *
     * @param repository jpa repository of Region instances
     * @param mapper     maps entities to/from the domain model
     */
    public RegionReadService( final RegionRepository repository, final RegionEntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    // ========== Create ==========

    // ========== Read ==========

    // ========== Update ==========

    // ========== Delete ==========


}
