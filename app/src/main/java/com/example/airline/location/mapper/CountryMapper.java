/* (C) 2025 */

package com.example.airline.location.mapper;


import java.util.List;

import com.example.airline.location.Country;
import com.example.airline.location.CountryDTO;
import com.example.airline.location.persistence.model.location.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



/**
 * MapStruct configuration for Country, CountryEntity, CountryDTO.
 *
 * Mapstruct will generate the methods.
 */
@Mapper( componentModel = "spring" )
public interface CountryMapper
{
    CountryMapper INSTANCE = Mappers.getMapper( CountryMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Persistence --> Domain ---
    // --- Instance
    /** Map a single db entity instance to a domain instance. */
    Country countryEntityToDomain( CountryEntity entity );

    // --- Collection
    /** Map a list of domain instances to a list of db entity instances. */
    List<Country> countryEntityToDomain( List<CountryEntity> entities );

    // --- Domain --> Persistence ---
    // --- Instance
    // --- Collection

    // ------------------------
    // ----- Domain / API -----
    // --- Domain --> API ---
    // --- Instance
    /** Map a domain instance to an API instance. */
    CountryDTO countryDomainToApi( Country country );

    // --- Collection
    /** Map a list of domain instances to a list of API instances. */
    List<CountryDTO> countryDomainToApi( List<Country> countrys );

    // --- API --> Domain ---
    // --- Instance
    // --- Collection
}
