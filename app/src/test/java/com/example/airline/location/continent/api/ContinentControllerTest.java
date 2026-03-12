package com.example.airline.location.continent.api;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.example.airline.location.continent.ContinentDTO;
import com.example.airline.location.continent.mapper.ContinentDtoMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.service.ContinentCreateService;
import com.example.airline.location.continent.service.ContinentDeleteService;
import com.example.airline.location.continent.service.ContinentReadService;
import com.example.airline.location.continent.service.ContinentUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@DisplayName( "Continent Controller" )
class ContinentControllerTest
{
    private ContinentCreateService createService;
    private ContinentReadService   readService;
    private ContinentUpdateService updateService;
    private ContinentDeleteService deleteService;

    private ContinentDtoMapper     dtoMapper;

    private HttpHeaders            requestHeader;

    @BeforeEach
    void setUp()
    {
        readService   = Mockito.mock( ContinentReadService.class );
        createService = Mockito.mock( ContinentCreateService.class );
        updateService = Mockito.mock( ContinentUpdateService.class );
        deleteService = Mockito.mock( ContinentDeleteService.class );

        dtoMapper     = Mappers.getMapper( ContinentDtoMapper.class );


        final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
                                                            StandardCharsets.UTF_8 );
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
        headers.set( "Content-Type", desiredContentType.toString() );
        headers.set( HttpHeaders.ACCEPT_LANGUAGE, "en-US" );
        headers.set( HttpHeaders.ACCEPT_CHARSET, "utf-8" );
        headers.set( HttpHeaders.ACCEPT_ENCODING, "gzip" );
        requestHeader = new HttpHeaders( headers );
    }

    @Nested
    @DisplayName( "get/fetch" )
    class Get           // NOPMD
    {
        @Test
        @DisplayName( "Continent By Id" )
        void methodGet_ContinentById()
        {
            final ContinentController controller = new ContinentController( readService, createService, updateService, deleteService, dtoMapper );

            final Continent continent = new Continent( 1, "NA", "North", null, null );

            when( readService.getReferenceById( anyInt() ) )
                    .thenReturn( Optional.of( continent ) );

            ResponseEntity<ContinentDTO> response = controller.restGetFindContinentById( 100, requestHeader );
            final HttpHeaders headers = response.getHeaders();

            assertAll( () -> assertNotNull( response.getBody() ),
                       () -> assertEquals( "application/json;charset=UTF-8", headers.getFirst( "Content-Type" )  )
                     );
        }

    }   // end of Get class group


    @Nested
    @DisplayName( "Post methods" )
    class Post           // NOPMD
    {
    }   // end of Post class group

    @Nested
    @DisplayName( "Put methods" )
    class Put           // NOPMD
    {
    }   // end of Put class group

    @Nested
    @DisplayName( "Delete methods" )
    class Delete           // NOPMD
    {
    }   // end of Delete class group

}
