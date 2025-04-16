/* (C) 2025 */

package com.example.airline.location.country.service;


import java.util.Optional;

import com.example.airline.location.country.model.Country;
import com.example.airline.location.country.mapper.CountryMapper;
import com.example.airline.location.persistence.model.location.CountryEntity;
import com.example.airline.location.country.persistence.repository.CountryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



/**
 * Spring Service (business logic) supporting the Country domain object.
 */
@Service
public class CountryService
{
    private final CountryRepository repository;

    private final CountryMapper mapper;

    /**
     * Create a CountryService supported by autowire.
     *
     * @param repository jpa repository of Countries
     * @param mapper maps entities to/from the domain model
     */
    public CountryService( final CountryRepository repository, final CountryMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    /**
     * Retrieve a paged list of Countries.
     *
     * @param pageable the page control structure.
     * @return a page of Countries.
     */
    public Page<Country> findAll( final Pageable pageable )
    {
        final Page<CountryEntity> countries = repository.findAll( pageable );

        return countries.map( mapper::entityToDomain );
    }



    /**
     * Find a country by its id.
     *
     * @param id the id of the desired country.
     * @return the optional country if it was found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Country> findCountryById( final Integer id )
    {
        final Optional<CountryEntity> countryEntity = repository.findById( id );

        if ( countryEntity.isPresent() )
        {
            final Country country = mapper.entityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }



    /**
     * Find a country by its standard letter code.
     *
     * @param code the letter code.
     * @return optionally, the country corresponding to the code.
     */
    public Optional<Country> findCountryByCode( final String code )
    {
        final Optional<CountryEntity> countryEntity = repository.findByCode( code );

        if ( countryEntity.isPresent() )
        {
            final Country country = mapper.entityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }

}
