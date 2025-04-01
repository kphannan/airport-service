/* (C)2025 */

package com.example.airline.location.api;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.airline.location.Continent;
import com.example.airline.location.ContinentDTO;
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

    public ContinentController( ContinentService service )
    {
        this.service = service;
    }



    @GetMapping( "" )
    public ResponseEntity<List<ContinentDTO>> restGetFindAll()
    {
        List<Continent> continents = service.findAll();

        // TODO map domain to API model

        return ResponseEntity.ok( Collections.emptyList() );
    }



    @GetMapping( "/{id}" )
    public ResponseEntity<ContinentDTO> restGetFindContinentById( @PathVariable final Integer id )
    {
        final Optional<Continent> optionalContinent = service.findContinentById( id );

        if ( optionalContinent.isPresent() )
        {
            Continent    continent = optionalContinent.get();
            ContinentDTO dto       = new ContinentDTO( continent.getId(), continent.getCode(), continent.getName(),
                    continent.getWikiLink(), continent.getKeywords() );

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
            Continent    continent = optionalEntity.get();
            ContinentDTO dto       = new ContinentDTO( continent.getId(), continent.getCode(), continent.getName(),
                    continent.getWikiLink(), continent.getKeywords() );
            // TODO add repository ... service layer
            // TODO return an OK response with empty body
            return ResponseEntity.ok( dto );
        }

        // may include instance in header.....
        return ResponseEntity.noContent().build();
        // return ResponseEntity.noContent().location().build();
    }
}
