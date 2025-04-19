package com.example.airline.location.continent.api;


import static com.example.rest.utility.HeaderUtility.withHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.mapper.ContinentMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import com.example.airline.location.continent.service.ContinentService;
import com.example.airline.location.persistence.model.location.ContinentEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


//public class YourControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    // your test methods
//}


/**
 * Tests for the REST endpoints only, no underlying Service or Repository.
 */
//@ExtendWith( SpringExtension.class )
//@WebMvcTest( value = ContinentController.class, excludeFilters = @ComponentScan.Filter( type = FilterType.ANNOTATION, classes = Repository.class ) )
@WebMvcTest(controllers = ContinentController.class )
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
    private ContinentService service;

    @Autowired
    @SuppressWarnings( "unused" )
    private ContinentMapper mapper;

    @MockitoBean
    private ContinentRepository repository;

    // TODO Excludes don't seem to work either as a REGEX, or @Repository annotation or explicitly listed


    @Nested
    @DisplayName( "/continent - HTTP GET")
    class Get
    {
        @Test
        void restGetById_withId_returnsItem() throws Exception
        {
            final ContinentEntity continentEntity = new ContinentEntity( 1, "NA", "North", null, null );
//            final Continent continent = new Continent( 1, "NA", "North", null, null );
//        RequestBuilder request = withHeaders( get("/api/v1/location/continent/{id}", 123 ) );
            final RequestBuilder request = withHeaders( get("/location/continent/{id}", 1 ) );

//        when(repository.findById( any() ))
//                .thenReturn( Optional.ofNullable( null ) );
            when(repository.getReferenceById( eq( 1 ) ))
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
            final RequestBuilder request = withHeaders( get("/location/continent/{id}", 1 ) );

            when(repository.findById( anyInt() ))
                    .thenReturn( Optional.ofNullable( null ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() )
                     );
        }



        @Test
        void restGetById_withTextId_returnsBadRequestWithProblemDetail() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code" ) );

            when(repository.findByCode( anyString() ))
                    .thenReturn( Optional.ofNullable( null ) );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isBadRequest() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            // TODO examine the ProblemDetail
//        assertThat( response.getContentAsString() )
//                .isBlank();
        }


        @Test
        void restGetByCode_withCode_returnsItem() throws Exception
        {
            // --- given
            final ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
            final RequestBuilder request = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) );

            when(repository.findByCode( eq("ZZ" ) ))
                    .thenReturn( Optional.of( continentEntity ) );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$.id" ).value( 1 ) )
                    .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
                    .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                    .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                    .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final String jsonString =
                    """
                    {
                       "id": 1,
                       "code: "ZZZ",
                       "name": "foo"
                    }
                    """;

//        JSONObject json = new JSONObject( response.getContentAsString() );
//        JSONAssert jsa = new JsonAssert( response.getContentAsString() );
//        assertThat( json.get( "id") )
//                .isEqualTo( 1 );

//        assertThat( json ).
            // --- then
            // TODO need to assert the resulting JSON....

            assertThat( response.getContentType() )
                    .isEqualTo( MediaType.APPLICATION_JSON_VALUE );

            //        assertThat( result.getResponse() )
////                .startsWith( "Foo" )
//                .isEqualToIgnoreCase( "foo" );
//        assertThat( response )
//                .isNotNull()
//                .returns( "RP" );

//        JSONAssert.assertEquals

        }

        @Test
        void restGetByCode_withInvalidCode_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) );

            when(repository.findByCode( anyString() ))
                    .thenReturn( Optional.ofNullable( null ) );


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

            when(repository.findByCode( anyString() ))
                    .thenReturn( Optional.ofNullable( null ) );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isNotFound() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            // TODO examine the ProblemDetail
//        assertThat( response.getContentAsString() )
//                .isBlank();
        }


        @Test
        void restGetAll_returnsSuccess() throws Exception
        {
            // --- given
            // ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
            final List<ContinentEntity> resultList =
                    List.of(
                            new ContinentEntity( 1, "XX", "::XNAMEX::", null, null  ),
                            new ContinentEntity( 2, "YY", "::YNAMEY::", null, null  ),
                            new ContinentEntity( 3, "ZZ", "::ZNAMEZ::", null, null  )
                           );
            final RequestBuilder request = withHeaders( get( "/location/continent" ) );

            when(repository.findAll())
                    .thenReturn( resultList );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
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
////                .startsWith( "Foo" )
//                .isEqualToIgnoreCase( "foo" );
//        assertThat( response )
//                .isNotNull()
//                .returns( "RP" );

//        JSONAssert.assertEquals

        }
    }

    @Nested
    @DisplayName( "/continent - HTTP POST")
    class Post
    {}

    @Nested
    @DisplayName( "/continent - HTTP PUT")
    class Put
    {
        @Test
        void restPutById_withWrongId_returnsConflict() throws Exception
        {
            // --- given
            ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null  );
            final String jsonString =
                    """
                    {
                       "id": 77,
                       "code": "CC",
                       "name": "foo"
                    }
                    """;
//            final RequestBuilder request = withHeaders( put("/location/continent/{id}", 55 ) )
            final RequestBuilder request = withHeaders( put("/location/continent" ) )
                    .content( jsonString );

            when(repository.existsById( anyInt() ))
                    .thenReturn( false );
            when(repository.getReferenceById( anyInt() ))
                    .thenReturn( entity );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() )
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

    @Nested
    @DisplayName( "/continent - HTTP DELETE")
    class Delete
    {}



}
