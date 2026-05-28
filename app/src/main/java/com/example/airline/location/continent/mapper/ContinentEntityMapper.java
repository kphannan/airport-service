/* (C) 2025 */

package com.example.airline.location.continent.mapper;


import java.util.List;

import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.NewContinent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.model.NewContinentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/**
 * MapStruct configuration for Continent, ContinentEntity, ContinentDTO.
 */
@Mapper( componentModel = "spring" )
public interface ContinentEntityMapper
{
    ContinentEntityMapper INSTANCE = Mappers.getMapper( ContinentEntityMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Persistence --> Domain ---
    // --- Instance

    /**
     * Map a single db entity instance to a domain instance.
     *
     * @param entity persistence entity.
     * @return the domain representation of a continent.
     */
    Continent entityToDomain( ContinentEntity entity );

    // --- Collection

    /**
     * Map a list of domain instances to a list of db entity instances.
     *
     * @param entities collection of Continent persistence entities.
     * @return collection of domain layer Continent instances.
     */
    List<Continent> entityToDomain( List<ContinentEntity> entities );

    // --- Domain --> Persistence ---
    // --- Instance

    /**
     * Convert a service layer representation into a persistent layer representation of a Continent.
     *
     * @param domain Service layer representation of a Continent.
     * @return the persistent layer representation of a Continent.
     */
    ContinentEntity domainToEntity( Continent domain );

    /**
     * Map a NewContient service layer representation to a persistent store representation.
     *
     * @param domain service layer representation of a NewContinent request.
     * @return a persistent layer representation of the NewContinent object.
     */
    @Mapping( target = "id", ignore = true )
    NewContinentEntity domainToEntity( NewContinent domain );
    // --- Collection

}
