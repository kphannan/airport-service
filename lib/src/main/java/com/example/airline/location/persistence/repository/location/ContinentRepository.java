/* (C)2025 */

package com.example.airline.location.persistence.repository.location;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.persistence.model.location.ContinentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * CRUD operations on the {@code Continent} repository.
 */
@Repository
public interface ContinentRepository extends JpaRepository<ContinentEntity, Integer>
{
    /**
     * Retrieve all {@code Continent} records.
     *
     * @return the paged result of {@code Country}s.
     */
    @Override
    List<ContinentEntity> findAll();

    Optional<ContinentEntity> findById( final Integer id );

    /**
     * Find a single {@code Continent} by its id code.
     *
     * @param continentCode the 2-character continent code.
     *
     * @return the {@code Continent} if found.
     */
    Optional<ContinentEntity> findByCode( final String continentCode );

    Optional<List<ContinentEntity>> findByNameLike( final String name );

    Optional<List<ContinentEntity>> findByKeywordsLike( final String keyword );

    // /**
    // * Retrive all the countries on a given contintent.
    // *
    // * @param continent the continent code.
    // * @return the paged result of {@code Continent}s.
    // */
    // List<Continent> findByContinent( final String continent );
}
