package com.example.airline.location.continent.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.airline.location.continent.api.ContinentController;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.ContinentTester;
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
@DisplayName( "Continent: Service - Update" )
class ContinentUpdateServiceTest
{
    @MockitoBean
    private ContinentRepository repository;

    @Autowired
    private ContinentUpdateService service;



    @Test
    @DisplayName( "update existing" )
    void update_existing_returnChanged()
    {
        // --- given
        final Continent       continent       = new Continent( 1, "code", "xnew name", null, "xUpdated" );
        final ContinentEntity continentEntity = new ContinentEntity( 1, "code", "new name", null, "Updated" );

        when( repository.existsById( anyInt() ) ).thenReturn( true );
        when( repository.save( any( ContinentEntity.class ) ) )
                .thenReturn( continentEntity );

        // --- when
        final Continent updated = service.update( continent );

        // --- then
        ContinentTester continentTester = ContinentTester.of( updated );
        assertAll( () -> assertThat( continentTester )
                             .hasName( "new name")
                             .hasCode( "code" )
                             .blankWikiLink()
                             .hasKeywords( "Updated" ),
                   () -> verify( repository, atMost( 1 ) ).existsById( anyInt() ),
                   () -> verify( repository, never() ).existsByCode( anyString() ),
                   () -> verify( repository ).save( any( ContinentEntity.class ) )
        );
    }

    @Test
    @DisplayName( "does not exist" )
    void update_notExisting_returnNull()
    {
        // --- given
        final Continent       continent       = new Continent( 1, "code", "name", null, null );

        when( repository.existsById( anyInt() ) ).thenReturn( false );
        when( repository.existsByCode( anyString() ) ).thenReturn( false );

        // --- when
        final Continent updated = service.update( continent );

        // --- then
        assertAll( () -> assertThat( updated ).isNull(),
                   () -> verify( repository, atMost( 1 ) ).existsById( anyInt() ),
                   () -> verify( repository, atMost( 1 ) ).existsByCode( anyString() ),
                   () -> verify( repository, never() ).save( any( ContinentEntity.class ) )
        );
    }

}
