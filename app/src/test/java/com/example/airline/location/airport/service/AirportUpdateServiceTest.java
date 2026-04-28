package com.example.airline.location.airport.service;


import com.example.airline.location.airport.api.AirportController;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class AirportUpdateServiceTest
{
    @MockitoBean
    private AirportRepository repository;



    @Autowired
    private AirportUpdateService service;


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
