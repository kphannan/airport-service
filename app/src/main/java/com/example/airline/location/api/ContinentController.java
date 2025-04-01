
package com.example.airline.location.api;

import java.util.Collections;
import java.util.List;

import com.example.airline.location.ContinentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping( "/v1/location/continent" )
public class ContinentController
{

    @GetMapping( "" )
    public ResponseEntity<List<ContinentDTO>> restGetAll()
    {
        return ResponseEntity.ok( Collections.emptyList() );
    }

    @GetMapping( "/{id}")
    public ResponseEntity<ContinentDTO> restGetFindContinentById( @PathVariable final Integer id )
    {
        // final Optional<ContientDTO> continent = repository.findById( id );

        // if (continent.isPresent() )
        // {
        //     return ResponseEntity.ok( continent.get() );
        // }
        // else
        // {
            return ResponseEntity.notFound().build();
        // }
    }

    @GetMapping( "/code/{code}" )
    public ResponseEntity<ContinentDTO> restGetFindContinentByCode( @PathVariable final String code )
    {
        // TODO add repository ... service layer
        // TODO map from Domain Object to DTO
        // TODO return an OK response with empty body
        return ResponseEntity.notFound().build();
    }
}
