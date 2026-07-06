/* (C) 2025 */

package com.example.airline.location.continent.mapper;


import java.util.List;

import com.example.airline.location.continent.ContinentDTO;
import com.example.airline.location.continent.NewContinentDTO;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.NewContinent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * MapStruct configuration for Continent, ContinentEntity, ContinentDTO.
 */
@Mapper( componentModel = "spring" )
public interface ContinentDtoMapper
{
    ContinentDtoMapper INSTANCE = Mappers.getMapper( ContinentDtoMapper.class );

    // ------------------------
    // ----- Domain / API -----
    // --- Domain --> API ---
    // --- Instance

    /**
     * Map a domain instance to an API instance.
     */
    ContinentDTO domainToApi( Continent continent );

    // --- Collection

    /**
     * Map a list of domain instances to a list of API instances.
     */
    List<ContinentDTO> domainToApi( List<Continent> continents );

    // --- API --> Domain ---
    // --- Instance

    /**
     * REST API representation of a Continent.
     *
     * @param dto the API layer representation of a Continent.
     * @return the service layer representation of the Continent.
     */
    Continent apiToDomain( ContinentDTO dto );

    /**
     * Map a REST API dto to its internal representation.
     *
     * @param dto the API's DTO representation of a NewContient.
     * @return the internal service layer representation of the new continent.
     */
    NewContinent apiToDomain( NewContinentDTO dto );
    // --- Collection
}
