package com.example.airline.location.continent.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.airline.location.continent.api.ContinentController;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.NewContinent;
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
@DisplayName( "Continent: Service - Create" )
class ContinentCreateServiceTest
{
    @MockitoBean
    private ContinentRepository repository;

    @Autowired
    private ContinentCreateService service;


    @Test
    @DisplayName( "Create Continent" )
    void createContinent_NotExisting_isCreated()
    {
        // -- given
        final ContinentEntity    continentEntity = new ContinentEntity( 1, "NA", "North", null, null );

        when( repository.existsByCode( anyString() ) )
                .thenReturn( false );
        when( repository.save( any( ContinentEntity.class ) ) )
                .thenReturn( continentEntity );

        // -- when
        final NewContinent       newContinent = new NewContinent( "NA", "North", null, null );
        final Continent continent  = service.create( newContinent );

        // -- then
        assertAll( () -> assertEquals( "North", continent.getName() ),
                   () -> assertEquals( "NA", continent.getCode() ),
                   () -> assertNull( continent.getWikiLink() ),
                   () -> assertNull( continent.getKeywords() ),
                   () -> verify( repository ).existsByCode( anyString() ),
                   () -> verify( repository ).save( any( ContinentEntity.class ) )
                 );
    }

    @Test
    @DisplayName( "Don't Create Continent" )
    void createContinent_Existing_notCreated()
    {
        // -- given
        when( repository.existsByCode( anyString() ) )
                .thenReturn( true );

        //  -- when
        final NewContinent       newContinent = new NewContinent( "NA", "North", null, null );
        final Continent continent  = service.create( newContinent );

        // -- then
        assertAll( () -> assertNull( continent ),
                   () -> verify( repository ).existsByCode( anyString() ),
                   () -> verify( repository, never() ).save( any( ContinentEntity.class ) )
                 );
    }


}
