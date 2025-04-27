/* (C) 2025 */

package com.example.airline.location.region.mapper;


import java.util.List;

import com.example.airline.location.RegionDTO;
import com.example.airline.location.region.model.Region;
import org.mapstruct.Mapper;


/**
 * MapStruct configuration for Region, RegionDTO.
 */
@Mapper( componentModel = "spring" )
public interface RegionDtoMapper
{
    // ------------------------
    // ----- Domain / API -----
    // --- Domain --> API ---
    // --- Instance
    /** Map a domain instance to an API instance. */
    RegionDTO domainToApi( Region region );

    // --- Collection
    /** Map a list of domain instances to a list of API instances. */
    List<RegionDTO> domainToApi( List<Region> regions );

    // --- API --> Domain ---
    // --- Instance
    // --- Collection
}
