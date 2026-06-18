/* (C) 2025 */

package com.example.airline.location.continent.service;


import com.example.airline.location.continent.mapper.ContinentEntityMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.NewContinent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
@Log4j2
public class ContinentCreateService
{
    private final ContinentRepository repository;

    private final ContinentEntityMapper mapper;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper     maps entities to/from the domain model
     */
    // @PitExclude
    // @DoNotMutate
    public ContinentCreateService( final ContinentRepository repository, final ContinentEntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }




    // ========== Create ==========

    /**
     * Create a new Continent instance in the persistent store.
     *
     * @param newContinent a NewContinent request object.
     * @return A new Continent object or null if the entity already exists.
     */
    public Continent create( final NewContinent newContinent )
    {
        if ( repository.existsByCode( newContinent.code() ) )
        {
            // TODO should result in ProblemDetail returned to API caller
            return null;
        }

        final ContinentEntity result = repository.save( mapper.domainToEntity( newContinent ) );

        return mapper.entityToDomain( result );
    }

}
