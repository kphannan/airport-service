/* (C) 2025 */

package com.example.airline.location.region.mapper;


import java.util.List;

import com.example.airline.location.region.model.Region;
import com.example.airline.location.region.persistence.model.RegionEntity;
import org.mapstruct.Mapper;


/**
 * MapStruct configuration for Region, RegionEntity.
 */
@Mapper( componentModel = "spring" )
public interface RegionEntityMapper
{
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
}
