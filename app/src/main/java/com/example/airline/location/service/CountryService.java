/* (C)2025 */

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
    private CountryRepository repository;

    private CountryMapper     mapper;



    public CountryService( CountryRepository repository, CountryMapper mapper )
    {
        this.repository = repository;
        this.mapper = mapper;
    }



    // TODO add support for paging.
    // public List<Country> findAll()
    // {
    //     return mapper.countryEntityToDomain( repository.findAll() );
    // }

    public Page<Country> findAll( final Pageable pageable )
    {
        Page<CountryEntity> countries = repository.findAll( pageable );

        return countries.map( entity -> mapper.countryEntityToDomain(entity));
    }


    public Optional<Country> findCountryById( final Integer id )
    {
        Optional<CountryEntity> countryEntity = repository.findById( id );

        if ( countryEntity.isPresent() )
        {
            Country country = mapper.countryEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }
        else
        {
            return Optional.ofNullable( null );
        }
    }



    public Optional<Country> findCountryByCode( final String code )
    {
        Optional<CountryEntity> countryEntity = repository.findByCode( code );

        if ( countryEntity.isPresent() )
        {
            Country country = mapper.countryEntityToDomain( countryEntity.get() );

            return Optional.of( country );
        }
        else
        {
            return Optional.ofNullable( null );
        }

    }

}
