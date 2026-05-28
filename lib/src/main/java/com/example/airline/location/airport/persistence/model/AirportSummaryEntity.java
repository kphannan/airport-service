package com.example.airline.location.airport.persistence.model;



/*
facility.type
facility.ident
facility.continent
        .continentName
facility.isoCountry
        .countryName
facility.isoRegion
        .regionName
facility.municipality
facility.name
facility.scheduledService
*/


import com.example.airline.location.LocationCodePatterns;
import com.example.utility.IgnoreGeneratedCoverage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * Commonly used subset of the Airport attributes.
 *
 * @param ident            unique identifier of an airport.  This is not the persistence id.
 * @param name             common name of the airport.
 * @param continentCode    de facto continent code where the airport exists.
 * @param continentName    common name of the continent.
 * @param countryCode      ISO 3166-1:alpha2 code for the country where the airport exists.
 * @param countryName      name of the country where the airport exists.
 * @param regionCode       ISO 3166 code of the high-level administrative subdivision of the country where the airport
 *                         is primarily located.
 * @param regionName       geopolitical division within the country where the airport exists.
 * @param municipality     The primary municipality that the airport serves. {@see AirportEntity#municipality}
 * @param type             The type of the airport. Allowed values are "closed_airport", "heliport", "large_airport",
 *                         "medium_airport", "seaplane_base", and "small_airport".
 * @param scheduledService {@code yes} if the airport has regularly scheduled flights, {@code no} if there is no
 *                         scheduled service.
 */
// TODO add validations @Pattern, @Size, @NonBlank
@IgnoreGeneratedCoverage
public record AirportSummaryEntity(
    @NotBlank
    // @Size( max = 7 )
    @Pattern( regexp = "^(([0-9]{1,2}[A-Z]{1,2}[0-9]?)|([A-Z]{3,4}[0-9]?)|([A-Z]{1,2}-([0-9]{2,5}|[A-Z]{3,4}))|([A-Z]{1,2}[0-9]{1,2}[A-Z]{1,2}?[0-9]?)|([A-Z]{1,2}[0-9]{1,2})|([A-Z]{1,2}-(([0-9]{1,2}[A-Z]{1,2}[0-9]?)|([A-Z]{2,3}[0-9]{1,2}))))$",
              message = "Airport ident must a unique 4 to 7 character code following a specific pattern" )
    String ident,

    @NotBlank
    @Size( max = 90 )
    String name,

    @NotBlank
    @Pattern( regexp = LocationCodePatterns.CONTINENT_CODE,
              message = "Continent code must be 2 uppercase characters" )
    String continentCode,

    @NotBlank
    @Size( max = 52 )
    String continentName,

    @NotBlank( message = "An ISO 3166-1:alpha2 country code is required" )
    @Pattern( regexp = LocationCodePatterns.CONTINENT_CODE, message = "Country code must be a valid ISO 3166-1:alpha2" )
    String countryCode,

    @NotBlank
    @Size( max = 52 )
    String countryName,

    @NotBlank( message = "A unique region code is required" )
    @Pattern( regexp = "[A-Z]{2}-[A-Z\\-]{1,4}",
              message = "Region code must be a valid ISO 3166-1:alpha2 followed by '-' and a local code" )
    String regionCode,

    @NotBlank
    @Size( max = 80 )
    String regionName,

    @NotBlank
    @Size( max = 128 )
    String municipality,

    @NotBlank
    @Size( max = 14 )
    String type,                // TODO should become and enum

    @NotBlank
    //@Size( min = 2, max = 3 )
    @Pattern( regexp = "(no)|(yes)|(NO)|(YES)",
              message = "Choices are 'yes' or 'no'" )
    String scheduledService     // TODO boolean
)
{}

