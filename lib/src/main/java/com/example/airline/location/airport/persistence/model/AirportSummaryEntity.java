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


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// TODO add validations @Pattern, @Size, @NonBlank
public record AirportSummaryEntity(
    @NotBlank
    @Size( max = 7 )
    String ident,
    @NotBlank
    @Size( max = 90 )
    String name,

    @NotBlank
    @Pattern( regexp = "[A-Z]{2}", message = "Continent code must be 2 uppercase characters" )
    String continentCode,
    @NotBlank
    @Size( max = 52 )
    String continentName,


    @NotBlank( message = "An ISO 3166:1-alpha2 country code is required" )
    @Pattern( regexp = "[A-Z]{2}", message = "Country code must a valid ISO 3166:1-alpha2" )
    String countryCode,

    @NotBlank
    @Size( max = 52 )
    String countryName,

    @NotBlank( message = "A unique region code is required" )
    @Pattern( regexp = "[A-Z]{2}-[A-Z\\-]{1,4}",
              message = "Region code must a valid ISO 3166:1-alpha2 followed by '-' and a local code" )
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

