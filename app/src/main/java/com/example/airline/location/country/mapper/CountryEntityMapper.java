/* (C) 2025 */

package com.example.airline.location.country.mapper;


import java.util.List;

import com.example.airline.location.CountryDTO;
import com.example.airline.location.country.model.Country;
import com.example.airline.location.country.persistence.model.CountryEntity;
import org.mapstruct.Mapper;


/**
 * MapStruct configuration for Country, CountryEntity.
 * <p>
 * Mapstruct will generate the methods.
 */
@Mapper( componentModel = "spring" )
public interface CountryEntityMapper
{
//    CountryMapper INSTANCE = Mappers.getMapper( CountryMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Persistence --> Domain ---
    // --- Instance

    /**
     * Map a single db entity instance to a domain instance.
     */
    Country entityToDomain( CountryEntity entity );

    // --- Collection

    /**
     * Map a list of domain instances to a list of db entity instances.
     */
    List<Country> entityToDomain( List<CountryEntity> entities );

    // --- Domain --> Persistence ---
    // --- Instance
    // --- Collection

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
