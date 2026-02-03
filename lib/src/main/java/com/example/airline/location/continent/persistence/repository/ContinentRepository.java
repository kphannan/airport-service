/* (C) 2025 */

package com.example.airline.location.continent.persistence.repository;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.model.NewContinentEntity;
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
     * Determine if an element exists in the DB.
     *
     * @param continentId must not be {@literal null}.
     * @return true if the continent is found, false otherwise.
     */
    @Override
    boolean existsById( Integer continentId );

    boolean existsByCode( String continentCode );

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
    @NonNull ContinentEntity getReferenceById( @NonNull Integer id );

    /**
     * Find a single {@code Continent} by its 2-character code.
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



    // ========== Create ==========
    ContinentEntity save( NewContinentEntity entity );

    // ========== Update ==========
    // ========== Delete ==========

    /**
     * Delete a specific Continent row.
     *
     * @param entity must not be {@literal null}.
     */
    void delete( ContinentEntity entity );

    /**
     * Delete a Continent row by its primary key.
     *
     * @param continentId must not be {@literal null}.
     */
    void deleteById( Integer continentId );
}
