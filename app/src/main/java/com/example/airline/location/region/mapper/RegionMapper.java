/* (C) 2025 */

package com.example.airline.location.region.mapper;


import java.util.List;

import com.example.airline.location.region.model.Region;
import com.example.airline.location.RegionDTO;
import com.example.airline.location.persistence.model.location.RegionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



/**
 * MapStruct configuration for Region, RegionEntity, RegionDTO.
 */
@Mapper( componentModel = "spring" )
public interface RegionMapper
{
//    RegionMapper INSTANCE = Mappers.getMapper( RegionMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Persistence --> Domain ---
    // --- Instance
    /** Map a single db entity instance to a domain instance. */
    Region entityToDomain( RegionEntity dto );

    // --- Collection
    /** Map a list of domain instances to a list of db entity instances. */
    List<Region> entityToDomain( List<RegionEntity> dtos );

    // --- Domain --> Persistence ---
    // --- Instance
    // --- Collection

    // ------------------------
    // ----- Domain / API -----
    // --- Domain --> API ---
    // --- Instance
    /** Map a domain instance to an API instance. */
    RegionDTO domainToApi( Region region );

    // --- Collection
    /** Map a list of domain instances to a list of API instances. */
    List<RegionDTO> domainToApi( List<Region> regions );

//    <T,S> T entityToDomain( S s );

    // --- API --> Domain ---
    // --- Instance
    // --- Collection
}
