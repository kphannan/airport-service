package com.example.airline.location.airport.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.example.airline.location.airport.api.AirportController;
import com.example.airline.location.airport.mapper.AirportDtoMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.continent.model.Continent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;




@WebMvcTest( controllers = AirportController.class )
@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Airport: Service - Delete" )
class AirportDeleteServiceTest
{
    @MockitoBean
    private AirportRepository repository;

    @Autowired
    private AirportDeleteService service;

    @MockitoSpyBean
    private AirportDtoMapper dtoMapper;

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

        @Test
        @DisplayName( "by ID" )
        void withID_invokesDelete()
        {
            Continent continent = new Continent( 1, "code", "name", null, null );

            // --- given
            when( repository.existsById( anyLong() ) ).thenReturn( true );
            when( repository.existsByCode( anyString() ) ).thenReturn( true );

            // --- when
//            final boolean isExisting = service.delete( 1L );
            final boolean isExisting = service.deleteById( 1L );

            // --- then
            assertAll( () -> assertThat( isExisting ).isTrue(),
                       () -> verify( repository, atMost( 1 ) ).existsById( anyLong() ),
                       () -> verify( repository, atMost( 1 ) ).existsByCode( anyString() ),
                       () -> verify( repository ).deleteById( anyLong() )
                     );
        }

        @Test
        @DisplayName( "without valid ID" )
        void withoutCodeAndId_doesNotInvokeDelete()
        {
            // --- given
            when( repository.existsById( anyLong() ) ).thenReturn( false );

            // --- when
            final boolean isExisting = service.deleteById( anyLong() );

            // --- then
            assertAll( () -> assertThat( isExisting )
                               .isFalse(),
                       () -> verify( repository, atMost( 1 ) )
                               .existsById( anyLong() ),
                       () -> verify( repository, never() )
                               .deleteById( anyLong() ),
                       () -> verify( repository, never() )
                               .delete( any( AirportEntity.class ) )
                     );
        }

        @Test
        @DisplayName( "entity without valid ID" )
        void delete_entityWithId_invokesDelete()
        {
            final Airport airport = new Airport( 2L, "ident", "type", "::YYNAME::", BigDecimal.valueOf( 12.34 ), BigDecimal.valueOf( 56.78 ), 50, "continent", "country", "region", "municiipality", "NO", "foo", "icao", "iata", "local", null, null, null );

            // --- given
            when( repository.existsById( anyLong() ) ).thenReturn( true );

            // --- when
            final boolean isExisting = service.delete( airport );

            // --- then
            assertAll( () -> assertThat( isExisting )
                               .isTrue(),
                       () -> verify( repository, atMost( 1 ) )
                               .existsById( anyLong() ),
                       () -> verify( repository, times( 1 ) )
                               .delete( any( AirportEntity.class ) ),
                       () -> verify( repository, never() )
                               .deleteById( anyLong() )
                     );
        }

        @Test
        @DisplayName( "entity without valid ID" )
        void delete_entityWithoutId_doesNotInvokeDelete()
        {
            final Airport airport = new Airport( 2L, "ident", "type", "::YYNAME::", BigDecimal.valueOf( 12.34 ), BigDecimal.valueOf( 56.78 ), 50, "continent", "country", "region", "municiipality", "NO", "foo", "icao", "iata", "local", null, null, null );

            // --- given
            when( repository.existsById( anyLong() ) ).thenReturn( false );

            // --- when
            final boolean isExisting = service.delete( airport );

            // --- then
            assertAll( () -> assertThat( isExisting )
                               .isFalse(),
                       () -> verify( repository, atMost( 1 ) )
                               .existsById( anyLong() ),
                       () -> verify( repository, never() )
                               .delete( any( AirportEntity.class ) ),
                       () -> verify( repository, never() )
                               .deleteById( anyLong() )
                     );
        }

    }

}
