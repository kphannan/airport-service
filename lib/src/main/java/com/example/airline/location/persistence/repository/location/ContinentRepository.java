/* (C) 2025 */

package com.example.airline.location.persistence.repository.location;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.persistence.model.location.ContinentEntity;
import org.jspecify.annotations.NonNull;
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

    /**
     * Find the Continent entry given its primary key.
     *
     * @param id the continent primary key.
     *
     * @return the DB entry for the target continent.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    @Override
    @NonNull Optional<ContinentEntity> findById( @NonNull Integer id );

    /**
     * Find a single {@code Continent} by its 2 character code.
     *
     * @param continentCode the 2-character continent code.
     *
     * @return the {@code Continent} if found.
     */
    Optional<ContinentEntity> findByCode( String continentCode );

    /**
     * Find a single {@code Continent} whose name contains the specified text.
     *
     * @param name the text to find in the continent name.
     *
     * @return the {@code Continent} if found.
     */
    Optional<List<ContinentEntity>> findByNameLike( String name );

    /**
     * Find a single {@code Continent} by ancillary keywords.
     *
     * @param keyword criteria for keyword match.
     *
     * @return the {@code Continent} if found.
     */
    Optional<List<ContinentEntity>> findByKeywordsLike( String keyword );

    // /**
    // * Retrive all the countries on a given contintent.
    // *
    // * @param continent the continent code.
    // * @return the paged result of {@code Continent}s.
    // */
    // List<Continent> findByContinent( final String continent );
}
