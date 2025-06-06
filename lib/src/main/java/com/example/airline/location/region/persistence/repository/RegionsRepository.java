/* (C) 2025 */

package com.example.airline.location.region.persistence.repository;


import java.util.Optional;

import com.example.airline.location.region.persistence.model.RegionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;



/**
 * CRUD operations on the {@code Region} repository.
 */
@Repository
public interface RegionsRepository extends PagingAndSortingRepository<RegionEntity, Integer>
{
    /**
     * Find a single {@code Region} by its unique identifier.
     *
     * @param id the unique id.
     *
     * @return the {@code Region} if found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    Optional<RegionEntity> findById( Integer id );

    /**
     * Find a single {@code Region} by its ISO code.
     *
     * @param regionCode the unique id code.
     *
     * @return the {@code Region} if found.
     */
    Optional<RegionEntity> findByCode( String regionCode );

    /**
     * Find all the {@code Region}s on a {@code Continent}.
     *
     * @param continent the continent code.
     * @param paging    the {@code Page} criteria.
     *
     * @return the paged result of {@code Region}s.
     */
    Page<RegionEntity> findByContinent( String continent,
                                        Pageable paging );

    /**
     * Find all the {@code Region}s in a {@code Country}.
     *
     * @param country the ISO country code whose regions are requested.
     * @param paging  the {@code Page} criteria.
     *
     * @return the paged result of {@code Region}s.
     */
    Page<RegionEntity> findByCountry( String country,
                                      Pageable paging );

    /**
     * Retrieve all {@code Region} records by {@code Page}.
     *
     * @param paging the {@code Page} criteria.
     *
     * @return the paged result of {@code Region}s.
     */
    @Override
    @NonNull Page<RegionEntity> findAll( @NonNull Pageable paging );
}
