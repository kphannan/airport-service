package com.example.airline.location.airport.api;


import static com.example.rest.utility.HeaderUtility.withHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.airline.location.airport.mapper.AirportMapper;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.airport.service.AirportService;
import com.example.airline.location.persistence.model.airport.AirportEntity;
import com.example.rest.utility.PageableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest( controllers = AirportController.class )
@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
class AirportControllerRestTest //extends RestControllerTestBase
{
    @Autowired
    protected MockMvc mvc;
    @MockitoBean
    protected AirportRepository repository;
    @Autowired
    private AirportService service;
    @Autowired
    private AirportMapper mapper;


//    @BeforeEach
//    void setup()
//    {
//        mvc = MockMvcBuilders.standaloneSetup( service )
//                             .setCustomArgumentResolvers( new PageableHandlerMethodArgumentResolver() )
//                             // .setControllerAdvice(new SuperHeroExceptionHandler())
//                             // .addFilters(new SuperHeroFilter())
//                             .build();
//    }


    private AirportEntity buildEntity()
    {
        return AirportEntity.builder()
                            .id( 1L )
                            .ident( "KATL" )
                            .type( "large_airport" )
                            .name( "::NAME::" )
                            .latitude( BigDecimal.valueOf( 123.456 ) )
                            .longitude( BigDecimal.valueOf( 987.654 ) )
                            .elevation( 55 )
                            .continent( "NA" )
                            .isoCountry( "USA" )
                            .isoRegion( "GA" )
                            .municipality( "Atlanta" )
                            .scheduledService( "yes" )
                            .gpsCode( "KATL" )
                            .iataCode( "KATL" )
                            .icaoCode( "ATL" )
                            .localCode( "KATL" )
                            .build();
    }


    @Nested
    @DisplayName( "/airport - HTTP GET" )
    class Get
    {

        @Nested
        @DisplayName( "by Key" )
        class ById
        {

            @Test
            void restGetById_withValidId_returnsItem() throws Exception
            {
                // --- given
                final AirportEntity  airportEntity = buildEntity();
                final RequestBuilder request       = withHeaders( get( "/location/airport/{id}", 1 ) );

                when( repository.findById( any() ) )
                        .thenReturn( Optional.of( airportEntity ) );


                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                        // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                        //      don't complain about lack of assertions in tests
                        .andExpect( jsonPath( "$.id" ).value( 1 ) )
                        .andExpect( jsonPath( "$.ident" ).value( "KATL" ) )
                        .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                        .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                        .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                        .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                        .andReturn();
                MockHttpServletResponse response = result.getResponse();

                // --- then
                // TODO need to assert the resulting JSON....

                assertThat( response.getContentType() )
                        .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }

            @Test
            void restGetById_withBadId_returnsNoContent() throws Exception
            {
                // --- given
                final RequestBuilder request = withHeaders( get( "/location/airport/{id}", 99 ) );

                when( repository.findById( anyLong() ) )
                        .thenReturn( Optional.empty() );


                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isNoContent() )
                        .andReturn();
                MockHttpServletResponse response = result.getResponse();

                // --- then
                assertThat( response.getStatus() )
                        .isEqualTo( HttpStatus.NO_CONTENT.value() );
                //        assertThat( response.getContentType() )
                //                .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }
        }

        @Nested
        @DisplayName( "by Identifier" )
        class ByIdentifier
        {
            @Test
            void restGetByIdent_withValidIdent_returnsItem() throws Exception
            {
                // --- given
                final AirportEntity  airportEntity = buildEntity();
                final RequestBuilder request       = withHeaders( get( "/location/airport/code/{code}", 1 ) );

                when( repository.findByIdent( anyString() ) )
                        .thenReturn( Optional.of( airportEntity ) );


                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                        // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                        //      don't complain about lack of assertions in tests
                        .andExpect( jsonPath( "$.id" ).value( 1 ) )
                        .andExpect( jsonPath( "$.ident" ).value( "KATL" ) )
                        .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                        .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                        .andExpect( jsonPath( "$.isoCountry" ).value( "USA" ) )
                        .andExpect( jsonPath( "$.isoRegion" ).value( "GA" ) )
                        .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                        .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                        .andReturn();
                MockHttpServletResponse response = result.getResponse();

                // --- then
                // TODO need to assert the resulting JSON....

                assertThat( response.getContentType() )
                        .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }

            @Test
            void restGetByIdent_withBadIdent_returnsNoContent() throws Exception
            {
                // --- given
                final RequestBuilder request = withHeaders( get( "/location/airport/code/{code}", "ZZ" ) );

                when( repository.findByIdent( anyString() ) )
                        .thenReturn( Optional.empty() );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isNoContent() )
                        //                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                        .andReturn();
                MockHttpServletResponse response = result.getResponse();

                // --- then
                assertThat( response.getStatus() )
                        .isEqualTo( HttpStatus.NO_CONTENT.value() );
                //        assertThat( response.getContentType() )
                //                .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }
        }

        @Nested
        @DisplayName( " all" )
        class All
        {
            @Test
            void restGetAll_returnsSuccess() throws Exception
            {
                // --- given
                // ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
                final List<AirportEntity> entities =
                        List.of(
                                buildEntity(),
                                buildEntity(),
                                buildEntity()
                               );
                final RequestBuilder request = withHeaders( get( "/location/airport" ) )
                        .param( "page", "5" )
                        .param( "size", "10" )
                        .param( "sort", "id,desc" )    // <-- no space after comma!
                        .param( "sort", "name,asc" );  // <-- no space after comma!

                Page<AirportEntity> page = new PageImpl<>( entities );
                when( repository.findAll( any( Pageable.class ) ) )
                        .thenReturn( page );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                        // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                        //      don't complain about lack of assertions in tests
                        .andDo( print() )
                        .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                        .andExpect( jsonPath( "$.content[0].ident" ).value( "KATL" ) )
                        .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                        .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                        .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() )
                        .andReturn();
                final MockHttpServletResponse response = result.getResponse();

                // --- then
                // TODO need to assert the resulting JSON....
                final ArgumentCaptor<Pageable> pageableCaptor =
                        ArgumentCaptor.forClass( Pageable.class );
                verify( repository ).findAll( pageableCaptor.capture() );
                final PageRequest pageable = (PageRequest)pageableCaptor.getValue();


                PageableAssert
                        .assertThat( pageable )
                        .hasPageNumber( 5 )
                        .hasPageSize( 10 )
                        .hasSort( "name", Sort.Direction.ASC )
                        .hasSort( "id", Sort.Direction.DESC );

                assertThat( response.getContentType() )
                        .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }
        }

        @Nested
        @DisplayName( "by Query (search)" )
        class Search
        {
            List<AirportEntity>           entities;
            MockHttpServletRequestBuilder request;
            Page<AirportEntity>           page;

            @BeforeEach
            void setUp()
            {
                entities =
                        List.of(
                                buildEntity(),
                                buildEntity(),
                                buildEntity()
                               );
                request = withHeaders( get( "/location/airport/search" ) )
                        .param( "page", "1" )
                        .param( "size", "10" )
                        .param( "sort", "id,desc" )    // <-- no space after comma!
                        .param( "sort", "name,asc" );  // <-- no space after comma!

                page = new PageImpl<>( entities );
                when( repository.advancedQuery( anyString(),    // iataCode
                                                anyString(),    // icaoCode
                                                anyString(),    // ident
                                                anyString(),    // name
                                                any( Pageable.class ) ) )
                    .thenReturn( page );
            }

            @Test
            void restGetSearch_name_returnsSuccess() throws Exception
            {
                // --- given
                request.param( "name", "Hartsfield" );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                        // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                        //      don't complain about lack of assertions in tests
                        .andDo( print() )
                        .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                        .andExpect( jsonPath( "$.content[0].ident" ).value( "KATL" ) )
                        .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                        .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                        .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() )
                        .andReturn();

                // --- then
                verifyPagedResponse();

                final MockHttpServletResponse response = result.getResponse();
                // TODO need to assert the resulting JSON....
                assertThat( response.getContentType() )
                        .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }


            @Test
            void restGetSearch_identifier_returnsSuccess() throws Exception
            {
                // --- given
                request.param( "ident", "KATL" );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                        // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                        //      don't complain about lack of assertions in tests
                        .andDo( print() )
                        .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                        .andExpect( jsonPath( "$.content[0].ident" ).value( "KATL" ) )
                        .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                        .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                        .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() )
                        .andReturn();

                // --- then
                verifyPagedResponse();

                final MockHttpServletResponse response = result.getResponse();
                // TODO need to assert the resulting JSON....
                assertThat( response.getContentType() )
                        .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }


            @Test
            void restGetSearch_icaoCode_returnsSuccess() throws Exception
            {
                // --- given
                request.param( "icaoCode", "KATL" );

                // --- when
                final MvcResult result = mvc
                        .perform( request )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                        // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                        //      don't complain about lack of assertions in tests
                        .andDo( print() )
                        .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                        .andExpect( jsonPath( "$.content[0].ident" ).value( "KATL" ) )
                        .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                        .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                        .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() )
                        .andReturn();

                // --- then
                verifyPagedResponse();

                final MockHttpServletResponse response = result.getResponse();
                // TODO need to assert the resulting JSON....
                assertThat( response.getContentType() )
                        .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
            }

        }



        void verifyPagedResponse()
        {
            final ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass( Pageable.class );
            verify( repository ).advancedQuery( anyString(),
                                                anyString(),
                                                anyString(),
                                                anyString(),
                                                pageableCaptor.capture() );
            final PageRequest pageable = (PageRequest)pageableCaptor.getValue();

            PageableAssert
                    .assertThat( pageable )
                    .hasPageNumber( 1 )
                    .hasPageSize( 10 )
                    .hasSort( "name", Sort.Direction.ASC )
                    .hasSort( "id", Sort.Direction.DESC );
        }

    }


    @Nested
    @DisplayName( "/airport - HTTP POST" )
    class Post
    {
    }

    @Nested
    @DisplayName( "/airport - HTTP PUT" )
    class Put
    {
    }

    @Nested
    @DisplayName( "/airport - HTTP DELETE" )
    class Delete
    {
    }

    @Nested
    @DisplayName( "/airport - HTTP PATCH" )
    class Patch
    {
    }

    @Nested
    @DisplayName( "/airport - HTTP INFO" )
    class Info
    {
    }

    @Nested
    @DisplayName( "/airport - HTTP HEAD" )
    class Head
    {
    }

    @Nested
    @DisplayName( "/airport - HTTP OPT" )
    class Opt
    {
    }

}
