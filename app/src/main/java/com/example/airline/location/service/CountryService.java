/* (C) 2025 */

package com.example.airline.location.service;


import java.util.Optional;

import com.example.airline.location.Country;
import com.example.airline.location.mapper.CountryMapper;
import com.example.airline.location.persistence.model.location.CountryEntity;
import com.example.airline.location.persistence.repository.location.CountryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;



@Component
// @RefreshScope
public class CountryService
{
    private final CountryRepository repository;

    private final CountryMapper mapper;

    public CountryService( final CountryRepository repository, final CountryMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    public Page<Country> findAll( final Pageable pageable )
    {
        final Page<CountryEntity> countries = repository.findAll( pageable );

        return countries.map( mapper::countryEntityToDomain );
    }



    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Country> findCountryById( final Integer id )
    {
        final Optional<CountryEntity> countryEntity = repository.findById( id );

        if ( countryEntity.isPresent() )
        {
            final Country country = mapper.countryEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }



    public Optional<Country> findCountryByCode( final String code )
    {
        final Optional<CountryEntity> countryEntity = repository.findByCode( code );

        if ( countryEntity.isPresent() )
        {
            final Country country = mapper.countryEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }

        return Optional.ofNullable( null );
    }

}
