/* (C) 2025 */

package com.example.airline.location.mapper;


import java.util.List;

import com.example.airline.location.Region;
import com.example.airline.location.RegionDTO;
import com.example.airline.location.persistence.model.location.RegionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



@Mapper( componentModel = "spring" )
public interface RegionMapper
{
    RegionMapper INSTANCE = Mappers.getMapper( RegionMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Instance
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    Region regionEntityToDomain( RegionEntity dto );

    // --- Collection
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    List<Region> regionEntityToDomain( List<RegionEntity> dtos );

    // ------------------------
    // ----- Domain / API -----
    // Domain --> API
    RegionDTO regionDomainToApi( Region region );

    // API --> Domain
    // --- n/a
    // --- Collection
    // Domain --> API
    List<RegionDTO> regionDomainToApi( List<Region> regions );

    // API --> Domain
}
