package com.example.airline.location.continent.api;


import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.ContinentDTO;
import com.example.airline.location.continent.mapper.ContinentDtoMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.ContinentDTOTester;
import com.example.airline.location.continent.model.ContinentTester;
import com.example.airline.location.continent.service.ContinentCreateService;
import com.example.airline.location.continent.service.ContinentDeleteService;
import com.example.airline.location.continent.service.ContinentReadService;
import com.example.airline.location.continent.service.ContinentUpdateService;
import com.example.utility.HeaderUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.http.HttpHeadersAssert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Test suite for the Continent REST controller.
 */
@DisplayName( "Continent: Controller" )
class ContinentControllerTest
{
    private ContinentCreateService createService;
    private ContinentReadService   readService;
    private ContinentUpdateService updateService;
    private ContinentDeleteService deleteService;

    private ContinentController    controller;

    private ContinentDtoMapper     dtoMapper;

    private HttpHeaders            requestHeader;

    @BeforeEach
    void init()
    {
        readService   = Mockito.mock( ContinentReadService.class );
        createService = Mockito.mock( ContinentCreateService.class );
        updateService = Mockito.mock( ContinentUpdateService.class );
        deleteService = Mockito.mock( ContinentDeleteService.class );

        dtoMapper     = Mockito.spy( Mappers.getMapper( ContinentDtoMapper.class ) );

        controller = new ContinentController( readService, createService, updateService, deleteService, dtoMapper );


        requestHeader = buildDefaultHeaders();
    }

    private HttpHeaders buildDefaultHeaders()
    {
        final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
                                                            StandardCharsets.UTF_8 );
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
        headers.set( "Content-Type", desiredContentType.toString() );
        headers.set( HttpHeaders.ACCEPT_LANGUAGE, "en-US" );
        headers.set( HttpHeaders.ACCEPT_CHARSET, "utf-8" );
        headers.set( HttpHeaders.ACCEPT_ENCODING, "gzip" );
        headers.set( HeaderUtility.TRACESTATE, "testState" );
        headers.set( HeaderUtility.TRACEID, "testParent" );
        headers.set( "NoWay", "Should not exist" );

        return new HttpHeaders( headers );
    }


    // ========== Create ==========
    // ===== Post (Create) =====
    @Nested
    @DisplayName( "Create" )
    class Post           // NOPMD
    {
//        @Test
//        @DisplayName( "Continent" )
//        void methodPost_Continent_isCreated()
//        {
//            final ContinentController controller = new ContinentController( readService, createService, updateService, deleteService, dtoMapper );
//
//            final Continent continent = new Continent( 1, "NA", "North", null, null );
//            final NewContinentDTO dto = new NewContinentDTO( "NA", "North", null, null );
//
//            when( readService.findByCode( anyString() ) )
//                    .thenReturn( Optional.of( continent ) );
//            when( createService.create( any( NewContinent.class ) ) )
//                    .thenReturn( continent );
//
//            ResponseEntity<ContinentDTO> response = controller.restPostAddContinent( dto, requestHeader );
//            final HttpHeaders headers = response.getHeaders();
//
//            assertAll( () -> assertEquals( "application/json;charset=UTF-8", headers.getFirst( "Content-Type" )  ),
//                       () -> verify( createService ).create(  any( NewContinent.class ) ) ,
//                       () -> verify( readService ).findByCode( anyString() ),
//                       () -> verifyNoInteractions( updateService ),
//                       () -> verifyNoInteractions( deleteService )
//                     );
//        }
    }   // end of Post class group


    // ========== Read ==========
    // ===== Get (Read) =====
    @Nested
    @DisplayName( "Find" )
    class Get           // NOPMD
    {
        @Test
        @DisplayName( "Continent By Id" )
        void methodGet_byId_returnsInstance()
        {
            final ContinentController controller = new ContinentController( readService,
                                                                            createService,
                                                                            updateService,
                                                                            deleteService,
                                                                            dtoMapper );

            final Continent continent = new Continent( 1,
                                                       "NA", "North",
                                                       null, null );

            when( readService.findById( anyInt() ) )
                    .thenReturn( Optional.of( continent ) );

            final ResponseEntity<ContinentDTO> response = controller.restFindContinentById( 100, requestHeader );
            final HttpHeaders headers = response.getHeaders();

            assertAll( () -> assertThat( ContinentDTOTester.of( response.getBody() ) )
                                 .hasName( "North")
                                 .hasCode( "NA" )
                                 .blankWikiLink()
                                 .blankKeywords(),
                       () -> assertEquals( "application/json;charset=UTF-8", headers.getFirst( "Content-Type" )  ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService, times( 1 ) )
                               .findById(  anyInt() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService )
            );
        }

    }   // end of Get class group


    // ========== Update ==========
    // ===== Put (Update) =====
    @Nested
    @DisplayName( "Update" )
    class Put           // NOPMD
    {
    }   // end of Put class group

    // ===== Patch (Update) =====
    @Nested
    @DisplayName( "Update (patch)" )
    class Patch           // NOPMD
    {
    }   // end of Put class group


    // ========== Delete ==========
    // ===== Delete (Delete) =====
    @Nested
    @DisplayName( "Delete" )
    class Delete           // NOPMD
    {
        @Test
        @DisplayName( "Continent By Id" )
        void methodDelete_existingById_returnsNoContent()
        {
            final ContinentController controller = new ContinentController( readService,
                                                                            createService,
                                                                            updateService,
                                                                            deleteService,
                                                                            dtoMapper );

            when( deleteService.deleteById( anyInt() ) )
                    .thenReturn( true );

            final ResponseEntity<ContinentDTO> response = controller.restDeleteContinentById( 100, requestHeader );
            final HttpHeadersAssert headersAssert = new HttpHeadersAssert( response.getHeaders() );

            assertAll( () -> assertThat( response.getStatusCode() )
                                 .isEqualTo( HttpStatus.NO_CONTENT ),
                       // Response
                       () -> headersAssert
                                 // .doesNoHaveSe
                                 // .hasHeaderSatisfying( HttpHeaders.CONTENT_TYPE,
                                 //                       values ->
                                 //                           // assertThat( values )
                                 //                           //     .satisfiesAnyOf( v -> assertThat( v, matches( "application/json " ) ) )
                                 //                       {
                                 //                            assertThat( values )
                                 //                                .matches( "application" );
                                 //
                                 //                                // .anyMatch( "foo", "bar" );
                                 //                       }
                                 //                     )
                                 .hasValue( HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8" ),
                       () -> assertThat( response.getBody() )
                                 .isNull(),
                       // Service layer
                       () -> verifyNoInteractions( createService ),
                       () -> verifyNoInteractions( readService ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verify( deleteService ).deleteById( anyInt() )
            );
        }
    }   // end of Delete class group


    // ===== Head =====
    @Nested
    @DisplayName( "HEAD" )
    class Head           // NOPMD
    {
    }   // end of Put class group

    // ===== Info =====
    @Nested
    @DisplayName( "INFO" )
    class Info           // NOPMD
    {
    }   // end of Put class group

    // ===== Options =====
    @Nested
    @DisplayName( "OPTIONS" )
    class Options           // NOPMD
    {
        @Test
        @DisplayName( "with header" )
        void methodOptions_headersOnly_returnsOptions()
        {
            // --- given

            // --- when
            final ResponseEntity<Void> response = controller.restOptionsContinent( requestHeader );


            // --- then
            final List<String>      expectedMethods = Arrays.asList( "DELETE",
                                                                     "GET",
                                                                     "HEAD",
                                                                     "OPTIONS",
                                                                     "PATCH",  "POST", "PUT",
                                                                     "TRACE"  );
            final HttpHeaders headers         = response.getHeaders();

            final HttpHeadersAssert headersAssert = new HttpHeadersAssert( headers );
            assertAll( () -> headersAssert
                               .containsHeader( HttpHeaders.ACCEPT )
                               .containsHeader( HttpHeaders.ALLOW )
                               .containsHeader( HttpHeaders.CONTENT_TYPE )
                               // .containsHeader( "TRACEPARENT" )
                               // .containsHeader( "TRACESTATE" )
                               .doesNotContainHeader( "NoWay" )
                               .hasValue( "TRACEPARENT", "testParent" )
                               .hasValue( "TRACESTATE", "testState" )
                               // .hasHeaderSatisfying( HttpHeaders.ALLOW, contains( expectedMethods ) )
                               // .hasExactlyValuesInAnyOrder( HttpHeaders.ALLOW, expectedMethods )
                               .hasValue( HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8" ),
                       () -> assertThat( headers.get( HttpHeaders.ALLOW ) )
                               .anySatisfy( element ->
                                   {
                                       for ( final String expected : expectedMethods )
                                       {
                                           assertThat( element ).contains( expected );
                                       }
                                   }
                               ),
                       () -> assertThat( headers.get( HttpHeaders.ACCEPT ) )
                               .contains( "application/json,application/yaml,application/xml" ),
                       () -> verifyNoInteractions( createService ),
                       () -> verifyNoInteractions( readService ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService )
            );
        }


        @Test
        @DisplayName( "without header" )
        void methodOptions_withoutHeaders_returnsOptions()
        {
            // --- given

            // --- when
            final ResponseEntity<Void> response = controller.restOptionsContinent( null );


            // --- then
            final List<String> expectedMethods = Arrays.asList( "DELETE", "GET",
                                                                "HEAD", "OPTIONS",
                                                                "PATCH", "POST", "PUT",
                                                                "TRACE" );
            final HttpHeaders headers         = response.getHeaders();

            final HttpHeadersAssert headersAssert = new HttpHeadersAssert( headers );
            assertAll( () -> headersAssert
                               .containsHeader( HttpHeaders.ACCEPT )
                               .containsHeader( HttpHeaders.ALLOW )
                               .doesNotContainHeader( "NoWay" ),
                       () -> assertThat( headers.get( HttpHeaders.ALLOW ) )
                               .anySatisfy( element ->
                                            {
                                                for ( final String expected : expectedMethods )
                                                {
                                                    assertThat( element ).contains( expected );
                                                }
                                            }
                               ),
                       () -> assertThat( headers.get( HttpHeaders.ACCEPT ) )
                               .contains( "application/json,application/yaml,application/xml" )
            );
        }

    }   // end of Put class group

    // ===== Trace =====
    @Nested
    @DisplayName( "TRACE" )
    class Trace           // NOPMD
    {
    }   // end of Put class group

}
