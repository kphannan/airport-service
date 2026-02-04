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
    public boolean delete( final Continent entity )
    {
        boolean existing = repository.existsById( entity.getId() ) || repository.existsByCode( entity.getCode() );
//        if ( repository.existsById( entity.getId() ) || repository.existsByCode( entity.getCode() ) )
//        {
            repository.delete( mapper.domainToEntity( entity ) );
//        }

        // TODO return not found (404)
        return existing;
    }

    public boolean deleteById( final Integer continentId )
    {
        boolean existing = repository.existsById( continentId );

        repository.deleteById( continentId );

        return existing;
    }

}
