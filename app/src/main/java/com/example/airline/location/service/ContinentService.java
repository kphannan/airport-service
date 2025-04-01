/* (C)2025 */

package com.example.airline.location.service;


import java.util.Collections;
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

    // @Autowired
    private ContinentMapper     mapper;

    public ContinentService( ContinentRepository repository, ContinentMapper mapper )
    {
        this.repository = repository;
        this.mapper = mapper;
    }



    public List<Continent> findAll()
    {
        List<ContinentEntity> entities = repository.findAll();

        // TODO map from Entity to Domain model
        List<Continent> continents = Collections.emptyList();

        return continents;
    }



    public Optional<Continent> findContinentById( final Integer id )
    {
        Optional<ContinentEntity> continentEntity = repository.findById( id );

        if ( continentEntity.isPresent() )
        {
            ContinentEntity entity = continentEntity.get();
            // TODO map persistence to Domain model
            // TODO use a mapping framework
            // Continent continent = new Continent( entity.getId(), entity.getCode(), entity.getName(),
            //         entity.getWikiLink(), entity.getKeywords() );
            Continent continent = mapper.continentDTOtoDomain( entity );

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
            ContinentEntity entity = continentEntity.get();
            // TODO map persistence to Domain model
            // TODO use a mapping framework
            Continent continent = new Continent( entity.getId(), entity.getCode(), entity.getName(),
                    entity.getWikiLink(), entity.getKeywords() );

            return Optional.of( continent );
        }
        else
        {
            return Optional.ofNullable( null );
        }

    }

}
