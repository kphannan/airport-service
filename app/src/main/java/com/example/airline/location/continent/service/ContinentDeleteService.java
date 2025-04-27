/* (C) 2025 */

package com.example.airline.location.continent.service;


import com.example.airline.location.continent.mapper.ContinentEntityMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
public class ContinentDeleteService
{
    private final ContinentRepository repository;

    private final ContinentEntityMapper mapper;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper     maps entities to/from the domain model
     */
    public ContinentDeleteService( final ContinentRepository repository, final ContinentEntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    // ========== Create ==========

    // ========== Update ==========

    // ========== Delete ==========
    public void delete( final Continent entity )
    {
        repository.delete( mapper.domainToEntity( entity ) );
    }

    public void deleteById( final Integer continentId )
    {
        repository.deleteById( continentId );
    }

}
