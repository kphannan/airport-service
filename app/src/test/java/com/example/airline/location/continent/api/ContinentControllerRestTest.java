package com.example.airline.location.continent.api;


import static com.example.rest.utility.HeaderUtility.withHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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


/**
 * Tests for the REST endpoints only, no underlying Service or Repository.
 */
//@ExtendWith( SpringExtension.class )
//@WebMvcTest( value = ContinentController.class, excludeFilters = @ComponentScan.Filter( type = FilterType.ANNOTATION, classes = Repository.class ) )
@WebMvcTest( controllers = ContinentController.class )
//@ComponentScan( basePackages = "com.example.airline.location.mapper" )
//@ComponentScan( basePackages = {"com.example.airline.location.continent", "com.example.airline.location.service", "com.example.airline.location.api"} )
@ComponentScan( basePackages = { "com.example.airline.location.continent" } )
//@WebMvcTest( controllers = ContinentController.class,
//             excludeFilters = @ComponentScan.Filter( type = FilterType.REGEX, pattern = ".*Controller" )
//)
//@WebMvcTest( controllers = ContinentController.class,
//             excludeFilters = @ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE,
//                                                     classes = { AirportController.class,
//                                                                 AirportCodeIataRepository.class,
//                                                                 AirportCodeIcaoRepository.class
//             } )
//)
//@WebMvcTest( controllers = ContinentController.class,
//             excludeFilters = { @ComponentScan.Filter( type = FilterType.ANNOTATION, classes = RestController.class ),
//                                @ComponentScan.Filter( type = FilterType.ANNOTATION, classes = Service.class ),
//                                @ComponentScan.Filter( type = FilterType.ANNOTATION, classes = Repository.class )
//             }
//           )
/*
@WebMvcTest(controllers = ContinentController.class,
            useDefaultFilters = true,
            excludeFilters = {
//                               @ComponentScan.Filter(
//                                type = FilterType.ASSIGNABLE_TYPE,
//                                classes = { //JpaRepositoriesAutoConfiguration.class,
//                                            //DataSourceAutoConfiguration.class,
//                                            //HibernateJpaAutoConfiguration.class,
//                                            AirportRepository.class,
//                                            CountryRepository.class,
//                                            RegionsRepository.class,
//                                            AirportCodeIataRepository.class,
//                                            AirportCodeIcaoRepository.class
//                                } ),
                               @ComponentScan.Filter(
                                       type = FilterType.REGEX, pattern = ".*[Repository]" )
//                               @ComponentScan.Filter(
//                                       type = FilterType.REGEX, pattern = "com.example.airline.location.persistence.repository.location.*Repository" )

////                               @ComponentScan.Filter( type = FilterType.ANNOTATION, classes = Repository.class )
                             }
        )
 */
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

    // TODO Excludes don't seem to work either as a REGEX, or @Repository annotation or explicitly listed


    @Nested
    @DisplayName( "/continent - HTTP GET" )
    class Get
    {
        @Test
        void restGetById_withId_returnsItem() throws Exception
        {
            final ContinentEntity continentEntity = new ContinentEntity( 1, "NA", "North", null, null );
//            final Continent continent = new Continent( 1, "NA", "North", null, null );
//        RequestBuilder request = withHeaders( get("/api/v1/location/continent/{id}", 123 ) );
            final RequestBuilder request = withHeaders( get( "/location/continent/{id}", 1 ) );

//        when(repository.findById( any() ))
//                .thenReturn( Optional.ofNullable( null ) );
            when( repository.getReferenceById( eq( 1 ) ) )
                    .thenReturn( continentEntity );
//        when(service.findContinentById( any() ))
//                .thenReturn( Optional.of( continent ) );
//        when(mapper.continentDomainToApi( any(Continent.class) ))
//                .thenCallRealMethod();


            final MvcResult result = mvc
                    .perform( request )
                    //.andExpect( content().contentTypeCompatibleWith( "application/json" ) )
                    //.andExpect( result().ok() )
                    .andReturn();

            final MockHttpServletResponse response = result.getResponse();

            // TODO need to assert the resulting JSON....
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertEquals( MediaType.APPLICATION_JSON_VALUE, response.getContentType() )
//                   () -> assertEquals( "Bar", response.getContentAsString() )
                     );

//        JsonPath jsonPath
            // Use of AssertJ
//        assertThat( result.getResponse() )
//                .startsWith( "Foo" )
//                .isEqualToIgnoreCase( "foo" );
        }


        @Test
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
        void restGetByCode_withInvalidCode_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isNoContent() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            assertThat( response.getContentAsString() )
                    .isBlank();
        }

        @Test
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
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andDo( print() )
                    .andExpect( jsonPath( "$[0].id" ).value( 1 ) )
//                .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
//                .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
//                .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
//                .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final String jsonString =
                    """
                            [
                                {
                                   "id": 1,
                                   "code: "XX",
                                   "name": "::XNAMEX::"
                                },
                                {
                                   "id": 2,
                                   "code: "YY",
                                   "name": "::YNAMEY::"
                                },
                                {
                                   "id": 3,
                                   "code: "ZZ",
                                   "name": "::ZNAMEZ::"
                                }
                            ]
                            """;

            // --- then
            // TODO need to assert the resulting JSON....

            assertThat( response.getContentType() )
                    .isEqualTo( MediaType.APPLICATION_JSON_VALUE );

            //        assertThat( result.getResponse() )
//                .startsWith( "Foo" )
//                .isEqualToIgnoreCase( "foo" );
//        assertThat( response )
//                .isNotNull()
//                .returns( "RP" );

//        JSONAssert.assertEquals

        }
    }

    @Nested
    @DisplayName( "/continent - HTTP POST" )
    class Post
    {
        @Test
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
                     );
            verify( repository ).existsByCode( anyString() );
            verify( repository, never() ).save( any( ContinentEntity.class ) );
        }


        @Test
        void restPut_withNew_returnsCreated() throws Exception
        {
            // --- given
            ContinentEntity entity = new ContinentEntity( 22, "CC", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( post( "/location/continent" ) )
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
            @DisplayName( "report missing name & code are required" )
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
            @DisplayName( "report blank name & code are required" )
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

    @Nested
    @DisplayName( "/continent - HTTP PUT" )
    class Put
    {
        @Test
        @DisplayName( "can't create a new instance" )
        void restPut_withNewEntity_returnsConflict() throws Exception
        {
            // --- given
            ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( put( "/location/continent" ) )
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
        @DisplayName( "only updates an existing instance" )
        void restPut_withExistingEntity_returnsOK() throws Exception
        {
            // --- given
            ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "CC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( put( "/location/continent" ) )
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
            verify( repository, times( 1 ) ).
                    save( any( ContinentEntity.class ) );
        }
    }


//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Nested
    @DisplayName( "/continent - HTTP DELETE" )
    class Delete
    {
        @Test
        void restDeleteById_withId_returnsGone() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.GONE.value(), result.getResponse().getStatus() ),
                       () -> verify( repository ).deleteById( anyInt() )
                     );
//
//            verify(repository).delete( any( ContinentEntity.class ) );
        }

        @Test
        void restDelete_withEntity_returnsGone() throws Exception
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
                    .content( jsonString );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.GONE.value(), result.getResponse().getStatus() ),
                       () -> verify( repository ).delete( any( ContinentEntity.class ) )
                     );
        }
    }

    @Nested
    @DisplayName( "/continent - HTTP PATCH" )
    class Patch
    {
    }

    @Nested
    @DisplayName( "/continent - HTTP INFO" )
    class Info
    {
//        @Test
//        void restTrace_returnsNoContent() throws Exception
//        {
//            // --- given
//            final RequestBuilder request = withHeaders( MockMvcRequestBuilders.request( HttpMethod.INFO,
//                                                                                        "/location/continent" ) );
//
//            // --- when
//            final MvcResult result = mvc
//                    .perform( request )
//                    .andDo( print() )
//                    .andReturn();
//
//            // --- then
//            final MockHttpServletResponse response = result.getResponse();
//
//            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() )
//                     );
//        }
    }

    @Nested
    @DisplayName( "/continent - HTTP TRACE" )
    class Trace
    {
        @Test
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
//            final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() )
                     );
        }
    }

    @Nested
    @DisplayName( "/continent - HTTP HEAD" )
    class Head
    {
        @Test
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
    }

    @Nested
    @DisplayName( "/continent - HTTP OPT" )
    class Opt
    {
        @Test
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
