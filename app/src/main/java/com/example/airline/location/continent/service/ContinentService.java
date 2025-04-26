/* (C) 2025 */

package com.example.airline.location.continent.service;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.mapper.EntityMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.NewContinent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
public class ContinentService
{
    private final ContinentRepository repository;

    private final EntityMapper mapper;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper     maps entities to/from the domain model
     */
    public ContinentService( final ContinentRepository repository, final EntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }


//    public boolean existsById( final Integer id )
//    {
//        return repository.existsById( id );
//    }

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
    public Optional<Continent> getReferenceById( final Integer id )
    {
        final ContinentEntity continentEntity = repository.getReferenceById( id );

        return mapOptionalEntityToDomain( Optional.ofNullable( continentEntity ) );
    }


    /**
     * Find a continent by its standard two-letter code.
     *
     * @param code the 2-letter code.
     * @return optionally, the continent corresponding to the code.
     */
    public Optional<Continent> findByCode( final String code )
    {
        final Optional<ContinentEntity> continentEntity = repository.findByCode( code );

        return mapOptionalEntityToDomain( continentEntity );
    }

    // ========== Create ==========
    // TODO use a NewContinentDTO....
    public Continent create( final NewContinent entity )
    {
        if ( repository.existsByCode( entity.getCode() ) )
        {
            return null;
        }

        final var result = repository.save( mapper.domainToEntity( entity ) );

        return mapper.entityToDomain( result );
    }

    public ContinentEntity save( final ContinentEntity entity )
    {
        return repository.save( entity );
    }

    // ========== Update ==========
    public @Nullable Continent update( @NonNull final Continent entity )
    {
        if ( repository.existsById( entity.getId() ) )
        {
            save( mapper.domainToEntity( entity ) );

            return entity;
        }

        return null;
    }

    // ========== Delete ==========
    public void delete( final Continent entity )
    {
        repository.delete( mapper.domainToEntity( entity ) );
    }

    public void deleteById( final Integer continentId )
    {
        repository.deleteById( continentId );
    }


    private Optional<Continent> mapOptionalEntityToDomain( final Optional<ContinentEntity> from )
    {
        if ( from.isPresent() )
        {
            final Continent continent = mapper.entityToDomain( from.get() );

            return Optional.ofNullable( continent );
        }

        return Optional.ofNullable( null );
    }

}
