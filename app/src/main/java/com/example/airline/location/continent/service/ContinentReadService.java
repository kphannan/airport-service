/* (C) 2025 */

package com.example.airline.location.continent.service;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.mapper.ContinentEntityMapper;
import com.example.airline.location.continent.mapper.EntityMapHelper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.support.TransactionTemplate;


/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
public class ContinentReadService
{
    private final ContinentRepository repository;

    private final ContinentEntityMapper mapper;

//    private final TransactionTemplate transactionTemplate;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper     maps entities to/from the domain model
     */
    public ContinentReadService( final ContinentRepository repository,
                                 final ContinentEntityMapper mapper
//                                 final TransactionTemplate transactionTemplate
                               )
    {
        this.repository          = repository;
        this.mapper              = mapper;
//        this.transactionTemplate = transactionTemplate;
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
    @Transactional
    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Continent> getReferenceById( final Integer id )
    {
        final ContinentEntity continentEntity = repository.getReferenceById( id );

        // Example of using transactionTemplate to execute a read-only transaction
        // ContinentEntity continentEntity = null;
        // transactionTemplate.execute( status -> {
        //     // This is a read-only transaction, so we don't need to do anything here.
        //     continentEntity = repository.getReferenceById( id );
        // } );

        return EntityMapHelper.mapOptionalEntityToDomain( Optional.ofNullable( continentEntity ) );
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

        return EntityMapHelper.mapOptionalEntityToDomain( continentEntity );
    }

    // ========== Create ==========

    // ========== Update ==========

    // ========== Delete ==========

}
