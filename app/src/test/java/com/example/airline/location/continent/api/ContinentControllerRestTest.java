package com.example.airline.location.continent.api;


import static com.example.rest.utility.HeaderUtility.withHeaders;
import static java.util.Map.entry;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.mapper.ContinentDtoMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.NewContinent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.model.NewContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import com.example.airline.location.continent.service.ContinentCreateService;
import com.example.airline.location.continent.service.ContinentDeleteService;
import com.example.airline.location.continent.service.ContinentReadService;
import com.example.airline.location.continent.service.ContinentUpdateService;
import com.example.utility.HeaderUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;
//import org.springframework.transaction.support.TransactionTemplate;


/**
 * Tests for the REST endpoints only, no underlying Service or Repository.
 */
//@ExtendWith( SpringExtension.class )
@WebMvcTest( controllers = ContinentController.class )
@ComponentScan( basePackages = { "com.example.airline.location.continent" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Continent: API (/continent)" )
class ContinentControllerRestTest
{
    private static final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
                                                                            StandardCharsets.UTF_8 );
    @Autowired
    protected MockMvc mvc;

    @MockitoSpyBean
    private ContinentCreateService createService;
    @MockitoSpyBean
    private ContinentReadService   readService;
    @MockitoSpyBean
    private ContinentUpdateService updateService;
    @MockitoSpyBean
    private ContinentDeleteService deleteService;

//    @Autowired
//    @SuppressWarnings( "unused" )
//    private ContinentDtoMapper mapperReal;
    @MockitoSpyBean
    private ContinentDtoMapper mapper;

    @MockitoBean
    private ContinentRepository repository;

//    @Autowired
//    private TransactionTemplate transactionTemplate;

    private HttpHeaders requestHeaders;

    @BeforeEach
    void init()
    {
        requestHeaders = new HttpHeaders();
        requestHeaders.set( HeaderUtility.TRACESTATE, "testState" );
        requestHeaders.set( HeaderUtility.TRACEID, "testParent" );
        requestHeaders.set( "NoWay", "Should not exist" );
        requestHeaders.setContentType( desiredContentType );
    }


    // ========== CREATE ==========
    // ===== POST =====
    /**
     * Tests for the POST http method.
     */
    @Nested
    @DisplayName( "HTTP POST" )
    class PostMethod           // NOPMD
    {
        @Test
        @DisplayName( "Existing resource - 409: Conflict - body ???" )
        void restPost_withExisting_returnsConflict() throws Exception
        {
            // --- given
            final String jsonString =
                    """
                            {
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( post( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .content( jsonString )
                    .headers( requestHeaders );

            // -- mocks behavior
            when( repository.existsByCode( anyString() ) )
                    .thenReturn( true );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isConflict() )
//                    .andExpect( jsonPath( "$[0].id" ).value( 1 ) )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();
            // TODO verify the JSON is the created entity
            final Collection<String> headerNames   = response.getHeaderNames();

            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                    // TODO should response include problem details indicating existing entity with same ID
                       () -> verify( createService ).create( any( NewContinent.class ) ),
                       () -> verifyNoInteractions( readService ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).existsByCode( anyString() ),
                       () -> verify( repository, never() ).save( any( NewContinentEntity.class ) ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
        }


/*
        //            final HttpHeadersAssert  headersAssert = new HttpHeadersAssert( response.getHeaders() );
        final Collection<String> headerNames   = response.getHeaderNames();
        assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
            () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll()
                     );
*/


        @Test
        @DisplayName( "new resource - 200: Success - body is new entity" )
        void restPost_withNew_returnsCreated() throws Exception
        {
            // --- given
            final ContinentEntity entity = new ContinentEntity( 22, "CC", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( post( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .content( jsonString )
                    .headers( requestHeaders );

            when( repository.existsByCode( anyString() ) )
                    .thenReturn( false );
            when( repository.save( any( ContinentEntity.class ) ) )
                    .thenReturn( entity );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isCreated() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            // TODO verify the JSON is the created entity

            // It is desired to have all 'asserts' as soft asserts.
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.CREATED.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .contains( "Location" )
                               .doesNotContain( "NoWay" ),
                    // The newly created resource's address is returned
                    // - only verify the end since the host and context root may vary by environment
                    // - final number (22) is the id of the created row
                       () -> assertThat( response.getRedirectedUrl() )
                               .matches( "^.*/location/continent/22" ),
                       () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                               .anyMatch( location -> location.matches( "^.*/location/continent/22$" ) ),
                    //
                       () -> verify( createService ).create( any( NewContinent.class ) ),
                       () -> verifyNoInteractions( readService ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                    //
                       () -> verify( repository ).existsByCode( anyString() ),
                       () -> verify( repository ).save( any( ContinentEntity.class ) ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
                     );
        }



        @Disabled
        @Nested
        @DisplayName( "validations" )
        class Validation
        {

            @Disabled
            @Test
            @DisplayName( "No name or Code - 400: Bad Request - Problem details with messages" )
            void restPost_withNoRequiredParams_returnsValidationError() throws Exception
            {
                // --- given
                final String jsonString =
                        """
                        {
                           "keywords": "will report missing code, name fields"
                        }
                        """;
                final RequestBuilder request = withHeaders( post( "/location/continent" ) )
                        .characterEncoding( "UTF-8" )
                        .content( jsonString )
                        .headers( requestHeaders );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andDo( print() )
                        .andReturn();

                // --- then
                // final MockHttpServletResponse response = result.getResponse();
                final String body = result.getResponse().getContentAsString();
//                final Gson gson = new Gson();
//                final ProblemDetail details = gson.fromJson( body );
//                final ObjectMapper  mapper  = buildMapper();
//                final ProblemDetail details = mapper.readValue( body, ProblemDetail.class );
                // TODO verify the JSON is the created entity

                // It is desired to have all 'asserts' as soft asserts.
                assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus() ),
                        // TODO Use a JSON assertion instead of a plain string
//                           () -> assertThat( details.getTitle() )
//                                   .isEqualTo( "Validation failed on 'newContinentDTO'" ),
//                           () -> assertThat( details.getDetail() )
//                                   .isEqualTo( "Invalid request content." ),
                           () -> assertThatJson( body )
                                   .isObject()
                                   .containsOnly( entry( "title", "Validation failed on 'newContinentDTO'" ),
                                                  entry( "status", 400),
                                                  entry( "detail",  "Invalid request content." ),
                                                  entry( "instance", "/location/continent" ),
                                                  entry( "name", "Name is required, provided: [null]"  ),
                                                  entry( "code", "A 2-character code is required, provided: [null]"  )
                                                ),
                           () -> assertThat( result.getResponse().getHeaderNames() )
                                   .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID )
                         );
            }


        /*
        () -> assertThat( result.getResponse().getRedirectedUrl() )
                               .matches( "^.* /location/continent/123$" ),
        () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
            .anyMatch( location -> location.matches( "^.* /location/continent/123$" ) )

        final MockHttpServletResponse response = result.getResponse();
        // TODO verify the JSON is the created entity
        final Collection<String> headerNames   = response.getHeaderNames();
        //            final HttpHeadersAssert  headersAssert = new HttpHeadersAssert( response.getHeaders() );
        final Collection<String> headerNames   = response.getHeaderNames();
        assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
            () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll()
                     );
*/


            @Disabled
            @Test
            @DisplayName( "blank name and code - 400: Bad Request - problem details indicate blank values" )
            void restPost_withBlankRequiredParams_returnsValidationError() throws Exception
            {
                // --- given
                final String jsonString =
                        """
                        {
                           "code": "  ",
                           "code": "  ",
                           "name": "     "
                        }
                        """;
                final RequestBuilder request = withHeaders( post( "/location/continent" ) )
                        .characterEncoding( "UTF-8" )
                        .headers( requestHeaders )
                        .content( jsonString );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andDo( print() )
                        .andReturn();

                // --- then
                // final MockHttpServletResponse response = result.getResponse();
                // TODO verify the JSON is the created entity
                final String body = result.getResponse().getContentAsString();

                // It is desired to have all 'asserts' as soft asserts.
                assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus() ),
                           () -> assertThat( result.getResponse().getHeaderNames() )
                                   .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                        // TODO Use a JSON assertion instead of a plain string
                           () -> assertThatJson( body )
                                   .isObject()
                                   .containsOnly( entry( "title", "Validation failed on 'newContinentDTO'" ),
                                                  entry( "status", 400),
                                                  entry( "detail",  "Invalid request content." ),
                                                  entry( "instance", "/location/continent" ),
                                                  entry( "name", "Name must be between 2 and 52 characters, provided: [     ]"  ),
                                                  entry( "code", "Code must be 2 uppercase characters, provided: [  ]"  )
                                                )
                         );
            }


        /*
        () -> assertThat( result.getResponse().getRedirectedUrl() )
                               .matches( "^.* /location/continent/123$" ),
        () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
            .anyMatch( location -> location.matches( "^.* /location/continent/123$" ) )

        final MockHttpServletResponse response = result.getResponse();
        // TODO verify the JSON is the created entity
        final Collection<String> headerNames   = response.getHeaderNames();
        //            final HttpHeadersAssert  headersAssert = new HttpHeadersAssert( response.getHeaders() );
        final Collection<String> headerNames   = response.getHeaderNames();
        assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
            () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll()
                     );
*/



            @Test
            @DisplayName( "invalid Wiki URI - 400: Bad Request - problem detail shows malformed URI" )
            void restPost_withInvalidWikiLink_returnsValidationError() throws Exception
            {
                // --- given
                final String jsonString =
                        """
                        {
                           "id": 1,
                           "code": "NA",
                           "name": "A valid length name",
                           "wikiLink": "https://wikipedia.com/bad url/not encoded",
                           "keywords": "will report missing code, name fields"
                        }
                        """;
                final RequestBuilder request = withHeaders( put( "/location/continent" ) )
                        .characterEncoding( "UTF-8" )
                        .headers( requestHeaders )
                        .content( jsonString );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andDo( print() )
                        .andReturn();

                // --- then
                // final MockHttpServletResponse response = result.getResponse();
                // TODO verify the JSON is the created entity

                // It is desired to have all 'asserts' as soft asserts.
                assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(),
                                               result.getResponse().getStatus() ),
                           () -> assertThat( result.getResponse().getHeaderNames() )
                                   .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                           () -> assertThat( result.getResponse().getContentAsString() )
                                   .contains( "Cannot deserialize value of type `java.net.URI` from String" )
                         );

            }

        /*
        () -> assertThat( result.getResponse().getRedirectedUrl() )
                               .matches( "^.* /location/continent/123$" ),
        () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
            .anyMatch( location -> location.matches( "^.* /location/continent/123$" ) )

        final MockHttpServletResponse response = result.getResponse();
        // TODO verify the JSON is the created entity
        final Collection<String> headerNames   = response.getHeaderNames();
        //            final HttpHeadersAssert  headersAssert = new HttpHeadersAssert( response.getHeaders() );
        final Collection<String> headerNames   = response.getHeaderNames();
        assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
            () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll()
                     );
*/

        }
    }


    // ========== READ ==========
    // ===== GET =====
    /**
     * Tests for the GET http method.
     */
    @Nested
    @DisplayName( "HTTP GET" )
    class GetMethod           // NOPMD
    {
        @Test
        @DisplayName( "with valid ID - 200: Success - entity in response body" )
        void restGetById_withId_returnsItem() throws Exception
        {
            final RequestBuilder request = withHeaders( get( "/location/continent/{id}", 1 ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders );

            final ContinentEntity continentEntity = new ContinentEntity( 1, "NA", "North", null, null );
            final Continent      continent        = new Continent( 1, "NA", "North", null, null );

            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.of( continentEntity ) );
            when( readService.findById( anyInt() ) )
                    .thenReturn( Optional.of( continent ) );


            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isOk() )
                    .andExpect( content().encoding( "UTF-8" ) )
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$.id" ).value( 1 ) )
                    .andExpect( jsonPath( "$.code" ).value( "NA" ) )
                    .andExpect( jsonPath( "$.name" ).value( "North" ) )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = response.getHeaderNames();
            // final String body = response.getContentAsString();
            // TODO need to assert the resulting JSON....
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
//                       () -> assertThat( response.getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findById( anyInt() ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
                     );
        }


        @Test
        @DisplayName( "with invalid id - 204: No Content - Empty body" )
        void restGetById_withWrongId_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/{id}", 1 ) )
                    .headers( requestHeaders );

            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.empty() );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID )
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findById( anyInt() ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
        }


        @Test
        @DisplayName( "with non-numeric ID - 400: Bad Request - body with Problem Details" )
        void restGetById_withTextId_returnsBadRequestWithProblemDetail() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isBadRequest() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final String body = response.getContentAsString();
            // TODO convert body to ProblemDetail....  use ObjectMapper.....

            // --- then
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            // TODO examine the ProblemDetail
            assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> assertFalse( body.isBlank() ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService, never() ).findById( anyInt() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository, never()  ).findById( anyInt() ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
        }


        @Test
        @DisplayName( "with code - 200: Success - body with entity" )
        void restGetByCode_withCode_returnsItem() throws Exception
        {
            // --- given
            final ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null );
            final RequestBuilder  request         = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( eq( "ZZ" ) ) )
                    .thenReturn( Optional.of( continentEntity ) );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$.id" ).value( 1 ) )
                    .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
                    .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                    .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                    .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            // TODO need to assert the resulting JSON....

            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findByCode( anyString() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findByCode( anyString() ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
                     );
        }

        @Test
        @DisplayName( "with invalid code - 204: No Content - body is empty" )
        void restGetByCode_withInvalidCode_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isNoContent() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();
//            final String body = response.getContentAsString();

            // --- then
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( body ).isNullOrEmpty(),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findByCode( anyString() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findByCode( anyString() ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
        }

        @Test
        @DisplayName( "with no code - 404: Not Found - body is problem detail" )
        void restGetByCode_withNoCode_returnsNotFoundWithProblemDetail() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isNotFound() )
                    .andReturn();


            // --- then
            final MockHttpServletResponse response = result.getResponse();
            final String body = response.getContentAsString();
            // TODO examine the ProblemDetail
            // TODO convert body to ProblemDetail....  use ObjectMapper.....
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NOT_FOUND.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> assertThat( body ).isNotBlank(),
                    //
                       () -> verifyNoInteractions( createService ),
                       () -> verifyNoInteractions( readService ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService )
                    // TODO mapper check
                     );
        }


        @Test
        @DisplayName( "get all - 200: Success - body is list of entities" )
        void restGetAll_returnsSuccess() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent" ) )
                    .headers( requestHeaders );

            // ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
            final List<ContinentEntity> resultList =
                    List.of(
                            new ContinentEntity( 1, "XX", "::XNAMEX::", null, null ),
                            new ContinentEntity( 2, "YY", "::YNAMEY::", null, null ),
                            new ContinentEntity( 3, "ZZ", "::ZNAMEZ::", null, null )
                           );

            when( repository.findAll() )
                    .thenReturn( resultList );

            // --- when
            final ResultActions actions = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$[0].id" ).value( 1 ) );
            // .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
            // .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
            // .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
            // .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
            final MvcResult result = actions.andReturn();

            // final String jsonString =
            //         """
            //                 [
            //                     {
            //                        "id": 1,
            //                        "code: "XX",
            //                        "name": "::XNAMEX::"
            //                     },
            //                     {
            //                        "id": 2,
            //                        "code: "YY",
            //                        "name": "::YNAMEY::"
            //                     },
            //                     {
            //                        "id": 3,
            //                        "code: "ZZ",
            //                        "name": "::ZNAMEZ::"
            //                     }
            //                 ]
            //                 """;

            // --- then
            // TODO need to assert the resulting JSON....
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findAll(),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findAll()
                     );
        }
    }

    // ========== UPDATE ==========
    // ===== PATCH =====
    /**
     * Tests for the PATCH http method.
     */
    @Nested
    @DisplayName( "HTTP PATCH" )
    class PatchMethod           // NOPMD
    {
        //        @Disabled       // TODO research JsonPatch more.
        @Test
        void patchContinent_withKeywords_returnsOk() throws Exception
        {
            // --- given
//            String x = "[{\"op\":\"move\",\"from\":\"/a\",\"path\":\"/b/2\"}]";
            final String json = "[{\"op\": \"replace\", \"path\": \'/keywords\", \"value\": \"Key 7, Key 8\"}]";

            final ContinentEntity continentEntity = new ContinentEntity( 1, "NA", "North", null, null );

            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.of( continentEntity ) );

            final MediaType patchContentType = new MediaType( "application",
                                                              "json-patch+json",
                                                              StandardCharsets.UTF_8 );

            requestHeaders.setContentType( patchContentType );

            final RequestBuilder request = withHeaders( patch( "/location/continent/{id}", 1 ) )
                    .characterEncoding( "UTF-8" )
//                    .contentType( "application/json-patch+json" )
                    .headers( requestHeaders )
                    .content( json );

            // --- when

            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
//                    .andExpect( status().isOk() )
                    .andExpect( status().isNotImplemented() )
                    .andExpect( content().encoding( "UTF-8" ) )
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$.id" ).value( 1 ) )
                    .andExpect( jsonPath( "$.code" ).value( "NA" ) )
                    .andExpect( jsonPath( "$.name" ).value( "North" ) )
//                    .andExpect( jsonPath( "$.keywords" ).value( "North" ) )
                    .andReturn();

            // --- then
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NOT_IMPLEMENTED.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID )
                       () -> verify( readService ).findById(  anyInt() ) ,
                       () -> verify( repository ).findById( anyInt() ),
//                       () -> verify( repository ).save( any( ContinentEntity.class ) ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
                     );

//            final Optional<Continent> original = readService.findById( 7 );
//
//            final Optional<Continent> updated = applyPatchEntity( json, original );


        }
/*
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID )
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll(),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
*/

    }

    // ===== PUT =====
    /**
     * Tests for the PUT http method.
     */
    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod           // NOPMD
    {
        @Test
        @DisplayName( "entity does not exist - 409: Conflict - can't create a new instance" )
        void restPut_withNewEntity_returnsConflict() throws Exception
        {
            // --- given
            final ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( put( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( false );
            when( repository.getReferenceById( anyInt() ) )
                    .thenReturn( entity );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> verify( updateService ).update( any( Continent.class ) ),
                       () -> verify( repository ).existsById( anyInt() ),
                       () -> verify( repository, never() ).save( any( ContinentEntity.class) ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
        }

        @Test
        @DisplayName( "existing entity - 200: Success  - update/return an existing instance" )
        void restPut_withExistingEntity_returnsOK() throws Exception
        {
            // --- given
            final ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( put( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( true );
            when( repository.save( any( ContinentEntity.class ) ) )
                    .thenReturn( entity );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( content().contentTypeCompatibleWith( "application/json" ) )
                    .andExpect( jsonPath( "$.id" ).value( 22 ) )
                    .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
                    .andExpect( jsonPath( "$.name" ).value( "::ZNAMEZ::" ) )
                    .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                    .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
//                       () -> verify( repository )
//                               .existsById( eq( 77 ) ),
                       () -> verify( updateService ).update( any( Continent.class ) ),
                       () -> verify( repository ).existsById( anyInt() ),
                       () -> verify( repository ).save( any( ContinentEntity.class) ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
//                       () -> verify( repository, times( 1 ) )
//                               .save( any( ContinentEntity.class ) )
                     );
        }
    }


    // ========== DELETE ==========
    // ===== DELETE =====
    /**
     * Tests for the DELETE http method.
     */
    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod           // NOPMD
    {
        @Test
        @DisplayName( "by ID - 204: No Content - empty body" )
        void restDeleteById_withId_returnsGone() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) )
                    .headers( requestHeaders );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( true );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( deleteService ).deleteById( anyInt() ),
                       () -> verify( repository ).deleteById( anyInt() )
                     );
        }

        @Test
        @DisplayName( "by invalid ID - 404: Not Found - empty body" )
        void restDeleteByUnknownId_withId_returnsNotFound() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) )
                    .headers( requestHeaders );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( false );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( repository ).deleteById( anyInt() )
                     );
        }

        //        @Test
//        @DisplayName( "by ID - 204: No Content - empty body" )
//        void restDeleteById_withId_returnsGone() throws Exception
//        {
//            // --- given
//            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) );
//
//            // --- when
//            final MvcResult result = mvc
//                    .perform( request )
//                    .andDo( print() )
//                    .andReturn();
//
//            // --- then
//            // final MockHttpServletResponse response = result.getResponse();
//
//            assertAll( () -> assertEquals( HttpStatus.GONE.value(), result.getResponse().getStatus() ),
//                       () -> verify( repository ).deleteById( anyInt() )
//                     );
//        }

        @Test
        @DisplayName( "by entity - 204: No Content - empty body" )
        void restDelete_withEntity_returnsNoContent() throws Exception
        {
            // --- given
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( delete( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( true );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( deleteService ).delete( any( Continent.class ) ),
                       () -> verify( repository ).delete( any( ContinentEntity.class ) )
                     );
        }

        @Test
        @DisplayName( "by unknown entity - 404: Not Found - empty body" )
        void restDelete_withUnknownEntity_returnsNotFound() throws Exception
        {
            // --- given
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( delete( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );
            when( repository.existsById( anyInt() ) )
                    .thenReturn( false );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( repository ).delete( any( ContinentEntity.class ) )
                     );
        }
    }

    // ========== Administrative ==========
    // ===== HEAD =====
    /**
     * Tests for the HEAD http method.
     */
    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod           // NOPMD
    {
        @Test
        @DisplayName( "no parameters - 200: empty body" )
        void restHead_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( head( "/location/continent" ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll(),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );


        }

        @Test
        @DisplayName( "with ID - 200: No body - has headers" )
        void restHeadWithId_returnsHeaders() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( head( "/location/continent/{continentId}", 123 ) )
                    .headers( requestHeaders );

            final ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.of( entity ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> assertTrue( response.getContentAsString().isEmpty() ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verify( repository ).findById(  anyInt() )
//                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );

        }


        @Test
        @DisplayName( "with ID - 204: No Content - has headers" )
        void restHeadWithBadId_returnsHeaders() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( head( "/location/continent/{continentId}", 123 ) )
                    .headers( requestHeaders );

            when( readService.findById( anyInt() ) )
                    .thenReturn( Optional.empty() );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> assertTrue( response.getContentAsString().isEmpty() ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verify( repository ).findById(  anyInt() )
//                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
        }



//        final ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
//        final String jsonString =
//                """
//                        {
//                           "id": 77,
//                           "code": "CC",
//                           "name": "foo"
//                        }
//                        """;
//        final RequestBuilder request = withHeaders( put( "/location/continent" ) )
//                .characterEncoding( "UTF-8" )
//                .content( jsonString );
//
//        when( repository.existsById( anyInt() ) )
//            .thenReturn( false );
//        when( repository.getReferenceById( anyInt() ) )
//            .thenReturn( entity );
//
//




    }

    // ===== INFO =====
    /**
     * Tests for the INFO http method.
     */
    @Nested
    @DisplayName( "HTTP INFO" )
    class InfoMethod           // NOPMD
    {
        // @Test
        // void restTrace_returnsNoContent() throws Exception
        // {
        //     // --- given
        //     final RequestBuilder request = withHeaders( MockMvcRequestBuilders.request( HttpMethod.INFO,
        //                                                                                 "/location/continent" ) );
        //
        //     // --- when
        //     final MvcResult result = mvc
        //             .perform( request )
        //             .andDo( print() )
        //             .andReturn();
        //
        //     // --- then
        //     final MockHttpServletResponse response = result.getResponse();
        //
        //     assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() )
        //              );
        // }
    }

    // ===== OPTION =====
    /**
     * Tests for the OPTIONS http method.
     */
    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod           // NOPMD
    {
        @Test
        @DisplayName( "no args - 204: No Content - empty body" )
        void restOptions_returnsHeaders() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( options( "/location/continent" ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), response.getStatus() ),
                       () -> assertThat( response.getHeaders( HttpHeaders.ACCEPT ) )
                               .contains( "application/json,application/yaml,application/xml" ),
                       () -> assertThat( response.getHeader( HttpHeaders.ALLOW ) )
                               .contains( "DELETE" )
                               .contains( "GET" )
                               .contains( "HEAD" )
                               .contains( "OPTIONS" )
                               .contains( "PATCH" )
                               .contains( "POST" )
                               .contains( "PUT" )
                               .contains( "TRACE" ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
//                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" )
                     );
        }
    }

    // ===== TRACE =====
    /**
     * Tests for the TRACE http method.
     */
    @Nested
    @DisplayName( "HTTP TRACE" )
    class TraceMethod           // NOPMD
    {
        @Test
        @DisplayName( "no args - 200: OK - empty body" )
        void restTrace_returnsOk() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( MockMvcRequestBuilders.request( HttpMethod.TRACE,
                                                                                        "/location/continent" ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .contains( "Location" )
                               .doesNotContain( "NoWay" ),
                       () -> assertThat( response.getRedirectedUrl() )
                               .matches( "^.*/location/continent$" ),
                       () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                               .anyMatch( location -> location.matches( "^.*/location/continent$" ) )
                     );


        }


        @Test
        @DisplayName( "no args - 200: OK - empty body" )
        void restTraceWithId_returnsOk() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( MockMvcRequestBuilders.request( HttpMethod.TRACE,
                                                                                        "/location/continent/{continentId}",
                                                                                        123
                                                                                      ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
//            var response = result.getResponse();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" )
                               .contains( HttpHeaders.LOCATION ),
                       () -> assertThat( result.getResponse().getRedirectedUrl() )
                               .matches( "^.*/location/continent/123$" ),
                       () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                               .anyMatch( location -> location.matches( "^.*/location/continent/123$" ) )
//                               .contains( "/location/continent/123")
//                               .matches( ".*/location/continent/123$" )
//                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Location" ) )

//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID )
                     );
/*
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
//                       () -> assertThat( result.getResponse().getHeaderNames() )
//                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID )
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll(),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
                     );
*/





        }

    }


//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }






    private static ObjectMapper buildMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
//        mapper.deserializationConfig().
//                mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        return mapper;
    }

}

