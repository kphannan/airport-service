/* (C)2025 */

package com.example.airline.location.persistence.repository.location;


import java.util.Optional;

import com.example.airline.location.persistence.model.location.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;



/**
 * CRUD operations on the {@code Region} repository.
 */
@Repository
public interface RegionsRepository extends PagingAndSortingRepository<Region, Integer>
{
    /**
     * Find a single {@code Region} by its unique identifier.
     *
     * @param id the unique id.
     * 
     * @return the {@code Region} if found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    Optional<Region> findById( final Integer id );

    /**
     * Find a single {@code Region} by its ISO code.
     *
     * @param regionCode the unique id code.
     * 
     * @return the {@code Region} if found.
     */
    Optional<Region> findByCode( final String regionCode );

    /**
     * Find all the {@code Region}s on a {@code Continent}.
     *
     * @param continent the continent code.
     * @param paging    the {@code Page} criteria.
     * 
     * @return the paged result of {@code Region}s.
     */
    Page<Region> findByContinent( final String continent,
                                  Pageable paging );

    /**
     * Find all the {@code Region}s in a {@code Country}.
     *
     * @param country the ISO country code whose regions are requested.
     * @param paging  the {@code Page} criteria.
     * 
     * @return the paged result of {@code Region}s.
     */
    Page<Region> findByCountry( final String country,
                                Pageable paging );

    /**
     * Retrieve all {@code Region} records by {@code Page}.
     *
     * @param paging the {@code Page} criteria.
     * 
     * @return the paged result of {@code Region}s.
     */
    @Override
    Page<Region> findAll( final Pageable paging );
}
