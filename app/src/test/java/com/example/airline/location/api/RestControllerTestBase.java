//package com.example.airline.location.api;
//
//
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//
//import com.example.airline.location.continent.api.ContinentController;
//import com.example.airline.location.persistence.repository.airport.AirportCodeIataRepository;
//import com.example.airline.location.persistence.repository.airport.AirportCodeIcaoRepository;
//import com.example.airline.location.airport.persistence.repository.AirportRepository;
//import com.example.airline.location.continent.persistence.repository.ContinentRepository;
//import com.example.airline.location.country.persistence.repository.CountryRepository;
//import com.example.airline.location.region.persistence.repository.RegionsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//
//
///**
// * Tests for the REST endpoints only, no underlying Service or Repository.
// */
//@WebMvcTest(controllers = ContinentController.class )
//@ComponentScan( basePackages = {"com.example.airline.location.mapper", "com.example.airline.location.service", "com.example.airline.location.api"} )
//public abstract class RestControllerTestBase
//{
//    @Autowired
//    protected MockMvc mvc;
//
//    @MockitoBean
//    protected ContinentRepository continentRepository;
//
//    // TODO Excludes don't seem to work either as a REGEX, or @Repository annotation or explicitly listed
//    @MockitoBean
//    protected RegionsRepository regionsRepository;    // ???
//    @MockitoBean
//    protected AirportRepository airportRepository;    // ???
//    @MockitoBean
//    protected CountryRepository         countryRepository;    // ???
//    @MockitoBean
//    protected AirportCodeIcaoRepository airportCodeIcaoRepository;    // ???
//    @MockitoBean
//    protected AirportCodeIataRepository airportCodeIataRepository;    // ???
//
//
//
//
//    /**
//     * Add required headers of Accept and Content-Type.
//     *
//     * @param builder the request builder
//     * @return chain the builder after adding headers
//     */
//    protected MockHttpServletRequestBuilder withHeaders( MockHttpServletRequestBuilder builder )
//    {
//        return withHeaders( builder, MediaType.APPLICATION_JSON );
//    }
//
//    /**
//     * Add required headers of Accept and Content-Type.
//     *
//     * @param builder the request builder
//     * @return chain the builder after adding headers
//     */
//    protected MockHttpServletRequestBuilder withHeaders( MockHttpServletRequestBuilder builder, MediaType mediaType )
//    {
//        return builder
//                .header( HttpHeaders.ACCEPT, mediaType.toString() )
//                .header( HttpHeaders.CONTENT_TYPE, mediaType.toString() );
//    }
//
//}
