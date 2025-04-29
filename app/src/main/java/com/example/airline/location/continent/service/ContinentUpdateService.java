/* (C) 2025 */

package com.example.airline.location.continent.service;


import com.example.airline.location.continent.mapper.ContinentEntityMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
public class ContinentUpdateService
{
    private final ContinentRepository repository;

    private final ContinentEntityMapper mapper;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper     maps entities to/from the domain model
     */
    public ContinentUpdateService( final ContinentRepository repository, final ContinentEntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }

    // ========== Create ==========


    // ========== Update ==========
    public @Nullable Continent update( @NonNull final Continent continent )
    {
        if ( repository.existsById( continent.getId() ) )
        {
            return mapper.entityToDomain( repository.save( mapper.domainToEntity( continent ) ) );
        }

        return null;
    }

    // ========== Delete ==========

}
