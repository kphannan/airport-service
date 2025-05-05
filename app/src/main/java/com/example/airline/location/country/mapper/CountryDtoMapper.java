/* (C) 2025 */

package com.example.airline.location.country.mapper;


import java.util.List;

import com.example.airline.location.country.CountryDTO;
import com.example.airline.location.country.model.Country;
import org.mapstruct.Mapper;


/**
 * MapStruct configuration for Country, CountryDTO.
 * <p>
 * Mapstruct will generate the methods.
 */
@Mapper( componentModel = "spring" )
public interface CountryDtoMapper
{
    // ------------------------
    // ----- Domain / API -----
    // --- Domain --> API ---
    // --- Instance

    /**
     * Map a domain instance to an API instance.
     */
    CountryDTO domainToApi( Country country );

    // --- Collection

    /**
     * Map a list of domain instances to a list of API instances.
     */
    List<CountryDTO> domainToApi( List<Country> countrys );

    // --- API --> Domain ---
    // --- Instance
    // --- Collection
}
