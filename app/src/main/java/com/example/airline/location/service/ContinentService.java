/* (C) 2025 */

package com.example.airline.location.service;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.Continent;
import com.example.airline.location.mapper.ContinentMapper;
import com.example.airline.location.persistence.model.location.ContinentEntity;
import com.example.airline.location.persistence.repository.location.ContinentRepository;
import org.springframework.stereotype.Service;



/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
public class ContinentService
{
    private final ContinentRepository repository;

    private final ContinentMapper mapper;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper maps entities to/from the domain model
     */
    public ContinentService( final ContinentRepository repository, final ContinentMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    /**
     * Retrieve a list of Continents.
     *
     * @return a list of Continents.
     */
    public List<Continent> findAll()
    {
        return mapper.entityToDomain( repository.findAll() );
    }



    /**
     * Find a continent by its id.
     *
     * @param id the id of the desired continent.
     * @return the optional continent if it was found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Continent> findContinentById( final Integer id )
    {
        final Optional<ContinentEntity> continentEntity = repository.findById( id );

        return mapOptionalEntityToDomain( continentEntity );
    }



    /**
     * Find a continent by its standard two-letter code.
     *
     * @param code the 2-letter code.
     * @return optionally, the continent corresponding to the code.
     */
    public Optional<Continent> findContinentByCode( final String code )
    {
        final Optional<ContinentEntity> continentEntity = repository.findByCode( code );

        return mapOptionalEntityToDomain( continentEntity );
    }



    private Optional<Continent> mapOptionalEntityToDomain( final Optional<ContinentEntity> from )
    {
        if ( from.isPresent() )
        {
            // TODO refactor mapper method to just 'entityToDomain(...)'
            final Continent continent = mapper.entityToDomain( from.get() );

            return Optional.of( continent );
        }

        return Optional.ofNullable( null );
    }

}
