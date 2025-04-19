/* (C) 2025 */

package com.example.airline.location.continent.mapper;


import java.util.List;

import com.example.airline.location.ContinentDTO;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.persistence.model.location.ContinentEntity;
import org.mapstruct.Mapper;


/**
 * MapStruct configuration for Continent, ContinentEntity, ContinentDTO.
 */
@Mapper( componentModel = "spring" )
public interface ContinentMapper
{
    //ContinentMapper INSTANCE = Mappers.getMapper( ContinentMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Persistence --> Domain ---
    // --- Instance
    /** Map a single db entity instance to a domain instance. */
    Continent entityToDomain( ContinentEntity entity );

    // --- Collection
    /** Map a list of domain instances to a list of db entity instances. */
    List<Continent> entityToDomain( List<ContinentEntity> entities );

    // --- Domain --> Persistence ---
    // --- Instance
    ContinentEntity domainToEntity( Continent domain );
    // --- Collection

    // ------------------------
    // ----- Domain / API -----
    // --- Domain --> API ---
    // --- Instance
    /** Map a domain instance to an API instance. */
    ContinentDTO domainToApi( Continent continent );

    // --- Collection
    /** Map a list of domain instances to a list of API instances. */
    List<ContinentDTO> domainToApi( List<Continent> continents );

    // --- API --> Domain ---
    // --- Instance
    Continent apiToDomain( ContinentDTO dto );
    // --- Collection
}
