package com.example.airline.location.api;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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



/**
 * Tests for the REST endpoints only, no underlying Service or Repository.
 */
@WebMvcTest(controllers = ContinentController.class )
@ComponentScan( basePackages = {"com.example.airline.location.mapper", "com.example.airline.location.service", "com.example.airline.location.api"} )
public abstract class RestControllerTestBase
{
    @Autowired
    protected MockMvc mvc;

    @MockitoBean
    protected ContinentRepository continentRepository;

    // TODO Excludes don't seem to work either as a REGEX, or @Repository annotation or explicitly listed
    @MockitoBean
    protected RegionsRepository regionsRepository;    // ???
    @MockitoBean
    protected AirportRepository airportRepository;    // ???
    @MockitoBean
    protected CountryRepository         countryRepository;    // ???
    @MockitoBean
    protected AirportCodeIcaoRepository airportCodeIcaoRepository;    // ???
    @MockitoBean
    protected AirportCodeIataRepository airportCodeIataRepository;    // ???




    /**
     * Add required headers of Accept and Content-Type.
     *
     * @param builder the request builder
     * @return chain the builder after adding headers
     */
    protected MockHttpServletRequestBuilder withHeaders( MockHttpServletRequestBuilder builder )
    {
        return withHeaders( builder, MediaType.APPLICATION_JSON );
    }

    /**
     * Add required headers of Accept and Content-Type.
     *
     * @param builder the request builder
     * @return chain the builder after adding headers
     */
    protected MockHttpServletRequestBuilder withHeaders( MockHttpServletRequestBuilder builder, MediaType mediaType )
    {
        return builder
                .header( HttpHeaders.ACCEPT, mediaType.toString() )
                .header( HttpHeaders.CONTENT_TYPE, mediaType.toString() );
    }

}
