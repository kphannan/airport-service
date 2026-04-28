package com.example.airline.location.continent.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.airline.location.continent.api.ContinentController;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest( controllers = ContinentController.class )
@ComponentScan( basePackages = { "com.example.airline.location.continent" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Continent: Service - Delete" )
class ContinentDeleteServiceTest
{
    @MockitoBean
    private ContinentRepository repository;

    @Autowired
    private ContinentDeleteService service;


//    @Test
//    void constructor()
//    {
//        new ContinentDeleteService( null, null );
//    }

    @Test
    @DisplayName( "by ID" )
    void withID_invokesDelete()
    {
        Continent continent = new Continent( 1, "code", "name", null, null );

        // --- given
        when( repository.existsById( anyInt() ) ).thenReturn( true );
        when( repository.existsByCode( anyString() ) ).thenReturn( true );

        // --- when
        final boolean isExisting = service.delete( continent );

        // --- then
        assertAll( () -> assertThat( isExisting ).isTrue(),
                   () -> verify( repository, atMost( 1 ) ).existsById( anyInt() ),
                   () -> verify( repository, atMost( 1 ) ).existsByCode( anyString() ),
                   () -> verify( repository ).delete( any( ContinentEntity.class ) )
                 );
    }


    @Test
    @DisplayName( "by Code" )
    void withoutID_invokesDelete()
    {
        Continent continent = new Continent( 2, "code", "name", null, null );

        // --- given
        when( repository.existsById( anyInt() ) ).thenReturn( false );
        when( repository.existsByCode( anyString() ) ).thenReturn( true );

        // --- when
        final boolean isExisting = service.delete( continent );

        // --- then
        assertAll( () -> assertThat( isExisting ).isTrue(),
                   () -> verify( repository, atMost( 1 ) ).existsById( anyInt() ),
                   () -> verify( repository, atMost( 1 ) ).existsByCode( anyString() ),
                   () -> verify( repository ).delete( any( ContinentEntity.class ) )
                 );
    }


    @Test
    @DisplayName( "neither id nor code" )
    void withoutCodeAndId_doesNotInvokeDelete()
    {
        Continent continent = new Continent( 3, "code", "name", null, null );

        // --- given
        when( repository.existsById( anyInt() ) ).thenReturn( false );
        when( repository.existsByCode( anyString() ) ).thenReturn( false );

        // --- when
        final boolean isExisting = service.delete( continent );

        // --- then
        assertAll( () -> assertThat( isExisting ).isFalse(),
                   () -> verify( repository, atMost( 1 ) ).existsById( anyInt() ),
                   () -> verify( repository, atMost( 1 ) ).existsByCode( anyString() ),
                   () -> verify( repository, times( 1 ) ).delete( any( ContinentEntity.class ) )
                 );
    }
}
