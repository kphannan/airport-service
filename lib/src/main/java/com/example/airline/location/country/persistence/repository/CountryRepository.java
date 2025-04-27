/* (C) 2025 */

package com.example.airline.location.country.persistence.repository;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.country.persistence.model.CountryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * CRUD operations on the {@code Country} repository.
 */
@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Integer>
{
    /**
     * Retrieve all {@code Country} records by {@code Page}.
     *
     * @param paging the {@code Page} criteria.
     *
     * @return the paged result of {@code Country}s.
     */
    @Override
    Page<CountryEntity> findAll( Pageable paging );

    /**
     * Find a single {@code Country} by its id code.
     *
     * @param countryCode the ISO country code.
     *
     * @return the {@code Country} if found.
     */
    Optional<CountryEntity> findByCode( String countryCode );

    /**
     * Retrive all the countries on a given contintent.
     *
     * @param continent the continent code.
     *
     * @return the paged result of {@code Country}s.
     */
    List<CountryEntity> findByContinent( String continent );
}
