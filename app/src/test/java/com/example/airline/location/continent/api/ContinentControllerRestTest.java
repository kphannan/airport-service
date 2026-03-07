package com.example.airline.location.continent.api;


import static com.example.rest.utility.HeaderUtility.withHeaders;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.mapper.ContinentDtoMapper;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import com.example.airline.location.continent.service.ContinentReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.support.TransactionTemplate;


/**
 * Tests for the REST endpoints only, no underlying Service or Repository.
 */
//@ExtendWith( SpringExtension.class )
@WebMvcTest( controllers = ContinentController.class )
@ComponentScan( basePackages = { "com.example.airline.location.continent" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "REST Controller - /continent" )
class ContinentControllerRestTest
{
    @Autowired
    protected MockMvc mvc;

    @Autowired
    @SuppressWarnings( "unused" )
    private ContinentReadService service;

    @Autowired
    @SuppressWarnings( "unused" )
    private ContinentDtoMapper mapper;

    @MockitoBean
    private ContinentRepository repository;

//    @Autowired
//    private TransactionTemplate transactionTemplate;
    private HttpHeaders requestHeaders;

    @BeforeEach
    void setup()
    {
        requestHeaders = new HttpHeaders();
        requestHeaders.set( "TRACESTATE", "testState");
        requestHeaders.set( "TRACEPARENT", "testParent");
    }

    /**
     * Tests for the GET http method.
     */
    @Nested
    @DisplayName( "HTTP GET" )
    class GetMethod
    {
        @Test
        @DisplayName( "with valid ID - 200: Success - entity in response body" )
        void restGetById_withId_returnsItem() throws Exception
        {
            final ContinentEntity continentEntity = new ContinentEntity( 1, "NA", "North", null, null );
            final RequestBuilder request = withHeaders( get( "/location/continent/{id}", 1 ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders );

            when( repository.getReferenceById( eq( 1 ) ) )
                    .thenReturn( continentEntity );


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

            // final String body = response.getContentAsString();
            // TODO need to assert the resulting JSON....
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
                       () -> assertThat( response.getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
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

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
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
            // TODO examine the ProblemDetail
            assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), response.getStatus() ),
                       () -> assertFalse( body.isBlank() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
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
            // TODO need to assert the resulting JSON....

            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
//                       () -> assertFalse( response.getHeaderNames().isEmpty() ),
//                       () -> assertEquals( 1, response.getHeaderNames().size() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
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
            final String body = response.getContentAsString();

            // --- then
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), response.getStatus() ),
                       () -> assertThat( body ).isNullOrEmpty(),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
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
            final MockHttpServletResponse response = result.getResponse();
            final String body = response.getContentAsString();
            // TODO convert body to ProblemDetail....  use ObjectMapper.....

            // --- then
            // TODO examine the ProblemDetail
            assertAll( () -> assertEquals( HttpStatus.NOT_FOUND.value(), response.getStatus() ),
                       () -> assertThat( body ).isNotBlank(),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
                     );
        }


        @Test
        @DisplayName( "get all - 200: Success - body is list of entities" )
        void restGetAll_returnsSuccess() throws Exception
        {
            // --- given
            // ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
            final List<ContinentEntity> resultList =
                    List.of(
                            new ContinentEntity( 1, "XX", "::XNAMEX::", null, null ),
                            new ContinentEntity( 2, "YY", "::YNAMEY::", null, null ),
                            new ContinentEntity( 3, "ZZ", "::ZNAMEZ::", null, null )
                           );
            final RequestBuilder request = withHeaders( get( "/location/continent" ) )
                    .headers( requestHeaders );

            when( repository.findAll() )
                    .thenReturn( resultList );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$[0].id" ).value( 1 ) )
                    // .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
                    // .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                    // .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                    // .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();
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

            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
//                       () -> assertFalse( response.getHeaderNames().isEmpty() ),
//                       () -> assertEquals( 1, response.getHeaderNames().size() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
                     );
        }
    }


    /**
     * Tests for the POST http method.
     */
    @Nested
    @DisplayName( "HTTP POST" )
    class PostMethod
    {
        @Test
        @DisplayName( "Existing resource - 409: Conflict - body ???" )
        void restPost_withExisting_returnsConflict() throws Exception
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
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            // TODO verify the JSON is the created entity

            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() ),
                       // TODO should response include problem details indicating existing entity with same ID
                       () -> verify( repository ).existsByCode( anyString() ),
                       () -> verify( repository, never() ).save( any( ContinentEntity.class ) ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
            );
        }


        @Test
        @DisplayName( "new resource - 200: Success - body is new entity" )
        void restPost_withNew_returnsCreated() throws Exception
        {
            // --- given
            final ContinentEntity entity = new ContinentEntity( 22, "CC", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
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
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            // TODO verify the JSON is the created entity

            // It is desired to have all 'asserts' as soft asserts.
            assertAll( () -> assertEquals( HttpStatus.CREATED.value(), result.getResponse().getStatus() ),
                       () -> assertTrue( result.getResponse().containsHeader( "Location" ) ),
                       () -> verify( repository ).existsByCode( anyString() ),
                       () -> verify( repository ).save( any( ContinentEntity.class ) ),
                       // The newly created resource's address is returned
                       // - only verify the end since the host and context root may vary by environment
                       // - final number (22) is the id of the created row
                       () -> assertThat( result.getResponse().getRedirectedUrl() )
                               .contains( "/location/continent/22" ),
                       () -> assertThat( result.getResponse().getHeader( "Location" ) )
                               .contains( "/location/continent/22" ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
                     );
        }

        @Nested
        @DisplayName( "validations" )
        class Validation
        {

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
                // TODO verify the JSON is the created entity

                // It is desired to have all 'asserts' as soft asserts.
                assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus() ),
                           // TODO Use a JSON assertion instead of a plain string
                           () -> assertThat( body ).contains( "A 2-character code is required; provided: [null]" ),
                           () -> assertThat( body ).contains( "Name is required; provided: [null]" ),
                           () -> assertThat( result.getResponse().getHeaderNames() )
                                   .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
                );
            }

            @Test
            @DisplayName( "blank name and code - 400: Bad Request - problem details indicate blank values" )
            void restPost_withBlankRequiredParams_returnsValidationError() throws Exception
            {
                // --- given
                final String jsonString =
                        """
                        {
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
                                   .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
                           // TODO Use a JSON assertion instead of a plain string
                           () -> assertThat( body )
                                   .contains( "A 2-character code is required; provided: [  ]" )
                                   .contains( "Code must be 2 uppercase characters; provided: [  ]" ),
                           () -> assertThat( body )
                                   .contains( "Name is required; provided: [     ]" )
                );
            }

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
                                   .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
                           () -> assertThat( result.getResponse().getContentAsString() )
                                   .contains( "Cannot deserialize value of type `java.net.URI` from String" )
                );

            }
        }
    }



    /**
     * Tests for the PUT http method.
     */
    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod
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

            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
                       () -> verify( repository ).existsById( eq( 77 ) ),
                       () -> verify( repository, never() ).save( any( ContinentEntity.class ) )
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

            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
                       () -> verify( repository )
                               .existsById( eq( 77 ) ),
                       () -> verify( repository, times( 1 ) )
                               .save( any( ContinentEntity.class ) )
            );
        }
    }


//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }



    /**
     * Tests for the DELETE http method.
     */
    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod
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

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
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
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
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

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
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
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" ),
                       () -> verify( repository ).delete( any( ContinentEntity.class ) )
            );
        }
    }



    /**
     * Tests for the PATCH http method.
     */
    @Nested
    @DisplayName( "/continent - HTTP PATCH" )
    class PatchMethod
    {
    }



    /**
     * Tests for the INFO http method.
     */
    @Nested
    @DisplayName( "/continent - HTTP INFO" )
    class InfoMethod
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



    /**
     * Tests for the TRACE http method.
     */
    @Nested
    @DisplayName( "HTTP TRACE" )
    class TraceMethod
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
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
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
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
                     );
        }

    }

    /**
     * Tests for the HEAD http method.
     */
    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod
    {
        @Test
        @DisplayName( "no parameters - 204: No Content" )
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
            // final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() )
            );
        }

        @Test
        @DisplayName( "with ID - 204: No Content - has headers" )
        void restHeadWithId_returnsHeaders() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( head( "/location/continent/{continentId}", 123 ) )
                    .headers( requestHeaders );

            final ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
            when( repository.getReferenceById( anyInt() ) )
                    .thenReturn( entity );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
//                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            assertAll( //() -> assertEquals( 204, response.getStatus() ),
                       () -> assertEquals( HttpStatus.NO_CONTENT.value(), response.getStatus() ),
                       () -> assertFalse( response.getHeaderNames().isEmpty() ),
                       // TODO use AssertJ to test for trace headers and content-type
//                       () -> assertEquals( 1, response.getHeaderNames().size() ),
                       () -> assertTrue( response.getContentAsString().isEmpty()),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
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

    /**
     * Tests for the OPTIONS http method.
     */
    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod
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

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertEquals( "application/json,application/yaml,application/xml",
                                           response.getHeader( HttpHeaders.ACCEPT ) ),
                       () -> assertThat( response.getHeader( HttpHeaders.ALLOW ) )
                               .contains( "DELETE" )
                               .contains( "GET" )
                               .contains( "HEAD" )
                               .contains( "OPTIONS" )
                               .contains( "PATCH" )
                               .contains( "POST" )
                               .contains( "PUT" )
                               .contains( "TRACE" ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type" , "TRACESTATE", "TRACEPARENT" )
            );
        }
    }
}
