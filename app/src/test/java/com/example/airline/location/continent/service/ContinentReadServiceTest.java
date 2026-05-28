package com.example.airline.location.continent.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.airline.location.continent.api.ContinentController;
import com.example.airline.location.continent.mapper.ContinentEntityMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@WebMvcTest( controllers = ContinentController.class )
@ComponentScan( basePackages = { "com.example.airline.location.continent" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Continent: Service - Read" )
class ContinentReadServiceTest
{
    @MockitoBean
    private ContinentRepository repository;

    @MockitoSpyBean
    private ContinentEntityMapper mapper;

    @Autowired
    private ContinentReadService service;


    @Test
    @DisplayName( "find all" )
    void read_findAll()
    {
        // Continent continent = new Continent( 1, "code", "name", null, null );

        // --- given
        when( repository.existsById( anyInt() ) ).thenReturn( true );
        when( repository.existsByCode( anyString() ) ).thenReturn( true );

        // --- when
        service.findAll();

        // --- then
        assertAll( () -> verify( repository, atMost( 1 ) ).existsById( anyInt() ),
                   () -> verify( repository, atMost( 1 ) ).existsByCode( anyString() ),
                   () -> verify( repository ).findAll()
        );
    }


    @Test
    @DisplayName( "findById")
    void read_findById()
    {
        // given
        ContinentEntity entity = new ContinentEntity( 1, "code", "name", null, "Key string" );
        when( repository.findById( anyInt() ) ).thenReturn( Optional.of( entity ) );

        // --- when
        Optional<Continent> continent = service.findById( Integer.valueOf( 123 ) );

        // --- then
        // assertThat( Optional.ofNullable( continent.get() ) ).isPresent().contains(  );
        assertAll( () -> assertThat( continent.get().name() ).contains( "name" ),
                   () -> assertThat( continent.get().code() ).contains( "code" ),
                   () -> assertThat( continent.get().wikiLink() ).isNull(),
                   () -> assertThat( continent.get().keywords() ).contains( "Key string" ),
                   () -> verify( repository, atMost( 1 ) ).findById( anyInt() )
                 );
    }

    @Test
    @DisplayName( "findByCode")
    void read_findByCode()
    {
        // given
        ContinentEntity entity = new ContinentEntity( 1, "code", "name", null, "Key string" );
        when( repository.findByCode( anyString() ) ).thenReturn( Optional.of( entity ) );

        // --- when
        Optional<Continent> continent = service.findByCode( "abc" );

        // --- then
        // assertThat( Optional.ofNullable( continent.get() ) ).isPresent().contains(  );
        assertAll( () -> assertThat( continent.get().name() ).contains( "name" ),
                   () -> assertThat( continent.get().code() ).contains( "code" ),
                   () -> assertThat( continent.get().wikiLink() ).isNull(),
                   () -> assertThat( continent.get().keywords() ).contains( "Key string" ),
                   () -> verify( repository, atMost( 1 ) ).findByCode( anyString() )
                   // () -> verify( mapper ).entityToDomain( any( ContinentEntity.class ) )
                 );
    }


}
