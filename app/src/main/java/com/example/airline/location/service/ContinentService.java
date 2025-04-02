/* (C) 2025 */

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
    private final ContinentRepository repository;

    private final ContinentMapper mapper;

    public ContinentService( final ContinentRepository repository, final ContinentMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }



    public List<Continent> findAll()
    {
        return mapper.continentEntityToDomain( repository.findAll() );
    }



    @SuppressWarnings( "PMD.ShortVariable" )
    public Optional<Continent> findContinentById( final Integer id )
    {
        final Optional<ContinentEntity> continentEntity = repository.findById( id );

        return mapOptionalEntityToDomain( continentEntity );
    }



    public Optional<Continent> findContinentByCode( final String code )
    {
        final Optional<ContinentEntity> continentEntity = repository.findByCode( code );

        return mapOptionalEntityToDomain( continentEntity );
    }



    private Optional<Continent> mapOptionalEntityToDomain( final Optional<ContinentEntity> from )
    {
        if ( from.isPresent() )
        {
            final Continent continent = mapper.continentEntityToDomain( from.get() );

            return Optional.of( continent );
        }

        return Optional.ofNullable( null );
    }

}
