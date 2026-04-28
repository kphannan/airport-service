package com.example.airline.location.airport.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import com.example.airline.location.airport.api.AirportController;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInCountryEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInRegionEntity;
import com.example.airline.location.airport.persistence.model.AirportSummaryEntity;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

//@ExtendWith( SpringExtension.class )
//@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@WebMvcTest( controllers = AirportController.class )
@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Airport: Service" )
class AirportDeleteServiceTest
{
    @MockitoBean
    private AirportRepository repository;



    @Autowired
    private AirportDeleteService service;


    // ========== Create ==========
    @Nested
    @DisplayName( "Create" )
    class Create           // NOPMD
    {
    }


    // ========== Read ==========
    @Nested
    @DisplayName( "Read" )
    class Read           // NOPMD
    {
    }


    // ========== Update ==========
    @Nested
    @DisplayName( "Update" )
    class Update           // NOPMD
    {
    }


    // ========== Delete ==========
    @Nested
    @DisplayName( "Delete" )
    class Delete           // NOPMD
    {
    }

}
