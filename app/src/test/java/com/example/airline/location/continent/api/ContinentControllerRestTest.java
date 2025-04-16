package com.example.airline.location.continent.api;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.mapper.ContinentMapper;
import com.example.airline.location.persistence.model.location.ContinentEntity;
import com.example.airline.location.persistence.repository.airport.AirportCodeIataRepository;
import com.example.airline.location.persistence.repository.airport.AirportCodeIcaoRepository;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import com.example.airline.location.country.persistence.repository.CountryRepository;
import com.example.airline.location.region.persistence.repository.RegionsRepository;
import com.example.airline.location.continent.service.ContinentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

//    @MockitoBean
//    @Spy
    @Autowired
    private ContinentService service;

//    @MockitoBean
    @Autowired
    private ContinentMapper mapper;
//    @Spy
//    private ContinentMapper mapper = Mappers.getMapper( ContinentMapper.class );

    @MockitoBean
    private ContinentRepository repository;

    // TODO Excludes don't seem to work either as a REGEX, or @Repository annotation or explicitly listed
    @MockitoBean
    private RegionsRepository regionsRepository;    // ???
    @MockitoBean
    private AirportRepository airportRepository;    // ???
    @MockitoBean
    private CountryRepository         countryRepository;    // ???
    @MockitoBean
    private AirportCodeIcaoRepository airportCodeIcaoRepository;    // ???
    @MockitoBean
    private AirportCodeIataRepository airportCodeIataRepository;    // ???


    @Test
    void restGetById_withId_returns() throws Exception
    {
        ContinentEntity continentEntity = new ContinentEntity( 1, "NA", "North", null, null );
        Continent continent = new Continent( 1, "NA", "North", null, null );
//        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get("/api/v1/location/continent/{id}", 123 ) );
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get("/location/continent/{id}", 1 ) );

//        when(repository.findById( any() ))
//                .thenReturn( Optional.ofNullable( null ) );
        when(repository.findById( anyInt() ))
                .thenReturn( Optional.of( continentEntity ) );
//        when(service.findContinentById( any() ))
//                .thenReturn( Optional.of( continent ) );
//        when(mapper.continentDomainToApi( any(Continent.class) ))
//                .thenCallRealMethod();


        MvcResult result = mvc
                .perform( request )
                //.andExpect( content().contentTypeCompatibleWith( "application/json" ) )
                //.andExpect( result().ok() )
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

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
    void restGetById_withTextId_returnsBadRequestWithProblemDetail() throws Exception
    {
        // --- given
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get( "/location/continent/code" ) );

        when(repository.findByCode( anyString() ))
                .thenReturn( Optional.ofNullable( null ) );


        // --- when
        MvcResult result = mvc
                .perform( request )
                .andDo( print() )
                .andExpect( status().isBadRequest() )
                .andReturn();
        MockHttpServletResponse response = result.getResponse();

        // --- then
        // TODO examine the ProblemDetail
//        assertThat( response.getContentAsString() )
//                .isBlank();
    }


    @Test
    void restGetByCode_withCode_returnsSuccess() throws Exception
    {
        // --- given
        ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get( "/location/continent/code/{code}", "ZZ" ) );

        when(repository.findByCode( eq("ZZ" ) ))
                .thenReturn( Optional.of( continentEntity ) );


        // --- when
        MvcResult result = mvc
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
        MockHttpServletResponse response = result.getResponse();
        String jsonString =
                """
                {
                   "id": 1,
                   "code: "ZZZ",
                   "name": "foo"
                }
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

    @Test
    void restGetByCode_withInvalidCode_returnsNoContent() throws Exception
    {
        // --- given
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get( "/location/continent/code/{code}", "ZZ" ) );

        when(repository.findByCode( anyString() ))
                .thenReturn( Optional.ofNullable( null ) );


        // --- when
        MvcResult result = mvc
                .perform( request )
                .andExpect( status().isNoContent() )
                .andReturn();
        MockHttpServletResponse response = result.getResponse();

        // --- then
        assertThat( response.getContentAsString() )
                .isBlank();
    }

    @Test
    void restGetByCode_withNoCode_returnsNotFoundWithProblemDetail() throws Exception
    {
        // --- given
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get( "/location/continent/code/" ) );

        when(repository.findByCode( anyString() ))
                .thenReturn( Optional.ofNullable( null ) );


        // --- when
        MvcResult result = mvc
                .perform( request )
                .andDo( print() )
                .andExpect( status().isNotFound() )
                .andReturn();
        MockHttpServletResponse response = result.getResponse();

        // --- then
        // TODO examine the ProblemDetail
//        assertThat( response.getContentAsString() )
//                .isBlank();
    }


    @Test
    void restGetAll_returnsSuccess() throws Exception
    {
        // --- given
        ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
        List<ContinentEntity> resultList =
                List.of(
                        new ContinentEntity( 1, "XX", "::XNAMEX::", null, null  ),
                        new ContinentEntity( 2, "YY", "::YNAMEY::", null, null  ),
                        new ContinentEntity( 3, "ZZ", "::ZNAMEZ::", null, null  )
                                            );
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get( "/location/continent" ) );

        when(repository.findAll())
                .thenReturn( resultList );

        // --- when
        MvcResult result = mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                //      don't complain about lack of assertions in tests
                .andDo( print() )
//                .andExpect( jsonPath( "$.id" ).value( 1 ) )
//                .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
//                .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
//                .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
//                .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        String jsonString =
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





    /**
     * Add required headers of Accept and Content-Type.
     *
     * @param builder the request builder
     * @return chain the builder after adding headers
     */
    private MockHttpServletRequestBuilder withHeaders( MockHttpServletRequestBuilder builder )
    {
        return builder
                .header( HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE )
                .header( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE );
    }

}
