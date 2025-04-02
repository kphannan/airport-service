/* (C) 2025 */

package com.example.airline.location.mapper;


import java.util.List;

import com.example.airline.location.Country;
import com.example.airline.location.CountryDTO;
import com.example.airline.location.persistence.model.location.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



@Mapper( componentModel = "spring" )
public interface CountryMapper
{
    CountryMapper INSTANCE = Mappers.getMapper( CountryMapper.class );

    // --------------------------------
    // ----- Domain / Persistence -----
    // --- Instance
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    Country countryEntityToDomain( CountryEntity dto );

    // --- Collection
    // Domain --> Persistence
    // --- n/a
    // Persistence --> Domain
    List<Country> countryEntityToDomain( List<CountryEntity> dtos );

    // ------------------------
    // ----- Domain / API -----
    // Domain --> API
    CountryDTO countryDomainToApi( Country country );

    // API --> Domain
    // --- n/a
    // --- Collection
    // Domain --> API
    List<CountryDTO> countryDomainToApi( List<Country> countrys );

    // API --> Domain
}
