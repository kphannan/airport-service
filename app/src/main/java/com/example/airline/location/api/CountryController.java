/* (C)2025 */

package com.example.airline.location.api;


import java.util.Optional;

import com.example.airline.location.Country;
import com.example.airline.location.CountryDTO;
import com.example.airline.location.config.GlobalApiResponses;
import com.example.airline.location.config.GlobalApiSecurityResponses;
import com.example.airline.location.mapper.CountryMapper;
import com.example.airline.location.service.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping( "/v1/location/country" )
@Tag( name = "Countries" )
@GlobalApiResponses
@GlobalApiSecurityResponses
public class CountryController
{
    // Autowired via constructor
    private CountryService service;
    private CountryMapper     mapper;


    public CountryController( CountryService service, CountryMapper mapper  )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    @GetMapping( "" )
    public Page<CountryDTO> restGetFindAll( final Pageable pageable )
    {
        Page<Country> countries = service.findAll( pageable );

        return countries.map( entity -> mapper.countryDomainToApi( entity ) );
    }



    @GetMapping( "/{id}" )
    public ResponseEntity<CountryDTO> restGetFindCountryById( @PathVariable final Integer id )
    {
        final Optional<Country> optionalCountry = service.findCountryById( id );

        if ( optionalCountry.isPresent() )
        {
            CountryDTO dto = mapper.countryDomainToApi( optionalCountry.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping( "/code/{code}" )
    public ResponseEntity<CountryDTO> restGetFindCountryByCode( @PathVariable final String code )
    {
        Optional<Country> optionalEntity = service.findCountryByCode( code );

        if ( optionalEntity.isPresent() )
        {
            CountryDTO dto = mapper.countryDomainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }
}
