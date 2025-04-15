package com.example.airline.location.api;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Optional;

import com.example.airline.location.Continent;
import com.example.airline.location.mapper.ContinentMapper;
import com.example.airline.location.persistence.model.location.ContinentEntity;
import com.example.airline.location.persistence.repository.airport.AirportCodeIataRepository;
import com.example.airline.location.persistence.repository.airport.AirportCodeIcaoRepository;
import com.example.airline.location.persistence.repository.airport.AirportRepository;
import com.example.airline.location.persistence.repository.location.ContinentRepository;
import com.example.airline.location.persistence.repository.location.CountryRepository;
import com.example.airline.location.persistence.repository.location.RegionsRepository;
import com.example.airline.location.service.ContinentService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;


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
@ComponentScan( basePackages = {"com.example.airline.location.mapper", "com.example.airline.location.service", "com.example.airline.location.api"} )
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
        when(repository.findById( any() ))
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
