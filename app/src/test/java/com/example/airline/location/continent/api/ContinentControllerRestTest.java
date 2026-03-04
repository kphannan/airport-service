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
                    .characterEncoding( "UTF-8" );

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
                       () -> assertFalse( response.getHeaderNames().isEmpty() ),
                       () -> assertEquals( 1, response.getHeaderNames().size() )
            );
        }


        @Test
        @DisplayName( "with invalid id - 204: No Content - Empty body" )
        void restGetById_withWrongId_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/{id}", 1 ) );

            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.empty() );

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
        @DisplayName( "with non-numeric ID - 400: Bad Request - body with Problem Details" )
        void restGetById_withTextId_returnsBadRequestWithProblemDetail() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code" ) );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isBadRequest() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            // TODO examine the ProblemDetail
            assertThat( response.getContentAsString() )
                    .isNotBlank();      // TODO Check for proper ProblemDetails
        }


        @Test
        @DisplayName( "with code - 200: Success - body with entity" )
        void restGetByCode_withCode_returnsItem() throws Exception
        {
            // --- given
            final ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null );
            final RequestBuilder  request         = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) );

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

            assertThat( response.getContentType() )
                    .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }

        @Test
        @DisplayName( "with invalid code - 204: No Content - body is empty" )
        void restGetByCode_withInvalidCode_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isNoContent() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            assertThat( response.getContentAsString() )
                    .isBlank();
        }

        @Test
        @DisplayName( "with no code - 404: Not Found - body is problem detail" )
        void restGetByCode_withNoCode_returnsNotFoundWithProblemDetail() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/" ) );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isNotFound() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            // TODO examine the ProblemDetail
            assertThat( response.getContentAsString() )
                    .isNotBlank();      // TODO Check for proper ProblemDetails
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
            final RequestBuilder request = withHeaders( get( "/location/continent" ) );

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

            assertThat( response.getContentType() )
                    .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
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
        void restPut_withExisting_returnsConflict() throws Exception
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
                    .content( jsonString );
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

            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() )
                       // TODO should response include problem details indicating existing entity with same ID
            );
            verify( repository ).existsByCode( anyString() );
            verify( repository, never() ).save( any( ContinentEntity.class ) );
        }


        @Test
        @DisplayName( "new resource - 200: Success - body is new entity" )
        void restPut_withNew_returnsCreated() throws Exception
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
                    .content( jsonString );

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
                       () -> assertTrue( result.getResponse().containsHeader( "Location" ) )
            );
            verify( repository ).existsByCode( anyString() );
            verify( repository ).save( any( ContinentEntity.class ) );

            // The newly created resource's address is returned
            // - only verify the end since the host and context root may vary by environment
            // - final number (22) is the id of the created row
            assertThat( result.getResponse().getRedirectedUrl() ).contains( "/location/continent/22" );
            assertThat( result.getResponse().getHeader( "Location" ) ).contains( "/location/continent/22" );
        }

        @Nested
        @DisplayName( "validations" )
        class Validation
        {

            @Test
            @DisplayName( "No name or Code - 400: Bad Request - Problem details with messages" )
            void restPut_withNoRequiredParams_returnsValidationError() throws Exception
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
                assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus() )
                );

                // TODO Use a JSON assertion instead of a plain string
                final String body = result.getResponse().getContentAsString();
                assertThat( body )
                        .contains( "A 2-character code is required; provided: [null]" );
                assertThat( body )
                        .contains( "Name is required; provided: [null]" );
            }

            @Test
            @DisplayName( "blank name and code - 400: Bad Request - problem details indicate blank values" )
            void restPut_withBlankRequiredParams_returnsValidationError() throws Exception
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
                assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus() )
                );

                // TODO Use a JSON assertion instead of a plain string
                final String body = result.getResponse().getContentAsString();
                assertThat( body )
                        .contains( "A 2-character code is required; provided: [  ]" )
                        .contains( "Code must be 2 uppercase characters; provided: [  ]" );
                assertThat( body )
                        .contains( "Name is required; provided: [     ]" );
            }

            @Test
            @DisplayName( "invalid Wiki URI - 400: Bad Request - problem detail shows malformed URI" )
            void restPut_withInvalidWikiLink_returnsValidationError() throws Exception
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
                                               result.getResponse().getStatus() )
                );

                assertThat( result.getResponse().getContentAsString() )
                        .contains( "Cannot deserialize value of type `java.net.URI` from String" );
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

            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() )
            );
            verify( repository ).existsById( eq( 77 ) );
            verify( repository, never() ).save( any( ContinentEntity.class ) );
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

            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() )
            );
            verify( repository )
                    .existsById( eq( 77 ) );
            verify( repository, times( 1 ) )
                    .save( any( ContinentEntity.class ) );
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
            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) );

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
                       () -> verify( repository ).deleteById( anyInt() )
            );
        }

        @Test
        @DisplayName( "by invalid ID - 404: Not Found - empty body" )
        void restDeleteByUnknownId_withId_returnsNotFound() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) );

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
                                                                                        "/location/continent" ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() )
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
                                                                                      ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() )
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
            final RequestBuilder request = withHeaders( head( "/location/continent" ) );

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
            final RequestBuilder request = withHeaders( head( "/location/continent/{continentId}", 123 ) );

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

            assertAll( () -> assertEquals( 200, response.getStatus() ),
                       () -> assertEquals( HttpStatus.NO_CONTENT.value(), response.getStatus() ),
                       () -> assertFalse( response.getHeaderNames().isEmpty() ),
                       () -> assertEquals( 1, response.getHeaderNames().size() ),
                       () -> assertTrue( response.getContentAsString().isEmpty())
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
            final RequestBuilder request = withHeaders( options( "/location/continent" ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertEquals( "DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT,TRACE",
                                           response.getHeader( HttpHeaders.ALLOW ) ),
                       () -> assertEquals( "application/json,application/yaml,application/xml",
                                           response.getHeader( HttpHeaders.ACCEPT ) )
            );
        }
    }
}
