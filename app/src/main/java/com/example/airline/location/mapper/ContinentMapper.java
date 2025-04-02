/* (C) 2025 */

package com.example.airline.location.mapper;


import java.util.List;

import com.example.airline.location.Continent;
import com.example.airline.location.ContinentDTO;
import com.example.airline.location.persistence.model.location.ContinentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



@Mapper( componentModel = "spring" )
public interface ContinentMapper
{
    ContinentMapper INSTANCE = Mappers.getMapper( ContinentMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Instance
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    Continent continentEntityToDomain( ContinentEntity entity );

    // --- Collection
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    List<Continent> continentEntityToDomain( List<ContinentEntity> entities );

    // ------------------------
    // ----- Domain / API -----
    // Domain --> API
    ContinentDTO continentDomainToApi( Continent continent );

    // API --> Domain
    // --- n/a
    // --- Collection
    // Domain --> API
    List<ContinentDTO> continentDomainToApi( List<Continent> continents );

    // API --> Domain
}
