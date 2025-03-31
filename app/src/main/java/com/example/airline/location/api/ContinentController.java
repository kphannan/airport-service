
package com.example.airline.location.api;

import java.util.List;
import java.util.Collections;

import com.example.airline.location.ContinentDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping( "/location/continent" )
public class ContinentController
{

    @GetMapping( "" )
    public List<ContinentDTO> restGetAll()
    {
        return Collections.emptyList();
    }
}
