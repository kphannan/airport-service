/* (C)2025 */

package com.example.airline.location.service;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.Continent;
import com.example.airline.location.mapper.ContinentMapper;
import com.example.airline.location.persistence.model.location.ContinentEntity;
import com.example.airline.location.persistence.repository.location.ContinentRepository;
import org.springframework.stereotype.Component;



@Component
// @RefreshScope
public class ContinentService
{
    private ContinentRepository repository;

    private ContinentMapper     mapper;



    public ContinentService( ContinentRepository repository, ContinentMapper mapper )
    {
        this.repository = repository;
        this.mapper = mapper;
    }



    public List<Continent> findAll()
    {
        return mapper.continentDTOtoDomain( repository.findAll() );
    }



    public Optional<Continent> findContinentById( final Integer id )
    {
        Optional<ContinentEntity> continentEntity = repository.findById( id );

        if ( continentEntity.isPresent() )
        {
            Continent continent = mapper.continentDTOtoDomain( continentEntity.get() );

            return Optional.of( continent );
        }
        else
        {
            return Optional.ofNullable( null );
        }
    }



    public Optional<Continent> findContinentByCode( final String code )
    {
        Optional<ContinentEntity> continentEntity = repository.findByCode( code );

        if ( continentEntity.isPresent() )
        {
            Continent continent = mapper.continentDTOtoDomain( continentEntity.get() );

            return Optional.of( continent );
        }
        else
        {
            return Optional.ofNullable( null );
        }

    }

}
