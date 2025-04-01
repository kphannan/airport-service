/* (C)2025 */

package com.example.airline.location.api;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.Continent;
import com.example.airline.location.ContinentDTO;
import com.example.airline.location.mapper.ContinentMapper;
import com.example.airline.location.service.ContinentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping( "/v1/location/continent" )
public class ContinentController
{
    // Autowired via constructor
    private ContinentService service;
    private ContinentMapper     mapper;


    public ContinentController( ContinentService service, ContinentMapper mapper  )
    {
        this.service = service;
        this.mapper  = mapper;
    }



    @GetMapping( "" )
    public ResponseEntity<List<ContinentDTO>> restGetFindAll()
    {
        List<Continent> continents = service.findAll();

        List<ContinentDTO> dtos = mapper.continentDomainToApi( continents );

        return ResponseEntity.ok( dtos );
    }



    @GetMapping( "/{id}" )
    public ResponseEntity<ContinentDTO> restGetFindContinentById( @PathVariable final Integer id )
    {
        final Optional<Continent> optionalContinent = service.findContinentById( id );

        if ( optionalContinent.isPresent() )
        {
            ContinentDTO dto = mapper.continentDomainToApi( optionalContinent.get() );

            return ResponseEntity.ok( dto );
        }

        return ResponseEntity.noContent().build();
    }



    @GetMapping( "/code/{code}" )
    public ResponseEntity<ContinentDTO> restGetFindContinentByCode( @PathVariable final String code )
    {
        Optional<Continent> optionalEntity = service.findContinentByCode( code );

        if ( optionalEntity.isPresent() )
        {
            ContinentDTO dto = mapper.continentDomainToApi( optionalEntity.get() );

            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }
}
