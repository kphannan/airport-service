package com.example.airline.location.airport.api;


import static com.example.rest.utility.HeaderTestingSupport.withHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.airline.location.airport.mapper.AirportDtoMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInCountryEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInRegionEntity;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.airport.service.AirportReadService;
import com.example.rest.utility.PageableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


/**
 * REST Controller for Airport entities.
 *
 * <ul>Paged list of all airports
 * <li>By Continent
 *    <ul>
 *    <li>List of all airports within the continent.</li>
 *    <li>List of Count of airports by Country.</li>
 *    </ul>
 * </li>
 * <li>By Country
 *    <ul>
 *    <li>List of all airports within the Country.</li>
 *    <li>List of airport counts grouped by Region.</li>
 *    </ul>
 * </li>
 * <li>By Region
 *    <ul>
 *    <li>List of all airports within the Region.</li>
 *    </ul>
 * </li>
 * </ul>
 * <pre>
 *    Continent
 *       - List of all airports.
 *       - Count of airports by country.
 *       Country
 *          - List of all airports in the country
 *          - Count of airports grouped by Region.
 *          Region (state/province)
 *             - List of airports in the Region.
 *             Airport
 * </pre>
 */
@DisplayName( "Airport: API (/airport)" )
@WebMvcTest( controllers = AirportController.class )
@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@AutoConfigureMockMvc( addFilters = false )
class AirportControllerRestTest //extends RestControllerTestBase
{
    // ===== Fixture =====
    @Autowired
    protected MockMvc mvc;
    @MockitoBean
    protected AirportRepository  repository;
    @MockitoSpyBean
    private   AirportReadService service;

    @MockitoSpyBean
    private   AirportDtoMapper   mapper;


    /*
    @BeforeEach
    void init()
    {
        mvc = MockMvcBuilders.standaloneSetup( service )
                             .setCustomArgumentResolvers( new PageableHandlerMethodArgumentResolver() )
                             // .setControllerAdvice(new SuperHeroExceptionHandler())
                             // .addFilters(new SuperHeroFilter())
                             .build();
    }
    */



    // ========== CREATE ==========
    // ===== POST =====
    /**
     * Tests for POST Http Methods.
     */
    @Nested
    @DisplayName( "HTTP POST" )
    class PostMethod            // NOPMD
    {
    }

    // ========== READ ==========
    // ===== GET =====
    /**
     * Tests for GET Http Methods.
     */
    @Nested
    @DisplayName( "HTTP GET" )
    class GetMethod
    {
        // --- Single ---

        /**
         * Find airport by database key.
         */
        @Nested
        @DisplayName( "by Key" )
        class ByIdTest
        {
            @Test
            void restGetById_withValidId_returnsItem() throws Exception
            {
                // --- given
                final AirportEntity  airportEntity = buildEntity();
                final RequestBuilder request       = withHeaders( get( "/location/airport/{id}", 1 ) )
                                                         .characterEncoding( "UTF-8" );

                when( repository.findById( any() ) )
                    .thenReturn( Optional.of( airportEntity ) );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.id" ).value( 1 ) )
                                     .andExpect( jsonPath( "$.ident" ).value( "KATLident" ) )
                                     .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                                     .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                                     .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                                     .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                );
            }

            @Test
            void restGetById_withBadId_returnsNoContent() throws Exception
            {
                // --- given
                final RequestBuilder request = withHeaders( get( "/location/airport/{id}", 99 ) )
                                                   .characterEncoding( "UTF-8" );

                when( repository.findById( anyLong() ) )
                    .thenReturn( Optional.empty() );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.NO_CONTENT.value() )
                );
            }
        }

        /**
         * Find Airport by code (IATA or ICAO).
         */
        @Nested
        @DisplayName( "by Identifier" )
        class ByIdentifierTest
        {
            @Test
            void restGetByIdentifier_withValidIdentifier_returnsItem() throws Exception
            {
                // --- given
                final AirportEntity  airportEntity = buildEntity();
                final RequestBuilder request       = withHeaders( get( "/location/airport/code/{code}", 1 ) )
                                                         .characterEncoding( "UTF-8" );

                when( repository.findByIdent( anyString() ) )
                    .thenReturn( Optional.of( airportEntity ) );


                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.id" ).value( 1 ) )
                                     .andExpect( jsonPath( "$.ident" ).value( "KATLident" ) )
                                     .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                                     .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                                     .andExpect( jsonPath( "$.isoCountry" ).value( "USA" ) )
                                     .andExpect( jsonPath( "$.isoRegion" ).value( "GA" ) )
                                     .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                                     .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                );
            }

            @Test
            void restGetByIdentifier_withBadIdentifier_returnsNoContent() throws Exception
            {
                // --- given
                final RequestBuilder request = withHeaders( get( "/location/airport/code/{code}",
                                                                 "ZZ" ) )
                                                   .characterEncoding( "UTF-8" );

                when( repository.findByIdent( anyString() ) )
                    .thenReturn( Optional.empty() );


                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.NO_CONTENT.value() )
                );
            }
        }

        // --- Multiple ---

        /**
         * All airports as paged groups.
         */
        @Nested
        @DisplayName( " all" )
        class AllTest
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
                                                   .characterEncoding( "UTF-8" )
                                                   .param( "page", "5" )
                                                   .param( "size", "10" )
                                                   .param( "sort", "id,desc" )    // <-- no space after comma!
                                                   .param( "sort", "name,asc" );  // <-- no space after comma!

                final Page<AirportEntity> page = new PageImpl<>( entities );
                when( repository.findAll( any( Pageable.class ) ) )
                    .thenReturn( page );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();
                final ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass( Pageable.class );

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           () -> verify( repository )
                                     .findAll( pageableCaptor.capture() ),
                           () -> PageableAssert
                                     .assertThat( pageableCaptor.getValue() )
                                     .pageNumberMatches( 5 )
                                     .pageSizeMatches( 10 )
                                     .sortCriteriaMatches( "name", Sort.Direction.ASC )
                                     .sortCriteriaMatches( "id", Sort.Direction.DESC ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                                     .andExpect( jsonPath( "$.content[0].ident" ).value( "KATLident" ) )
                                     .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                                     .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                                     .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() ),
                           // Verify how the data was retrieved
                           () -> verify( mapper, times( 3 ) )
                                     .domainToApi( any( Airport.class ) )
                );
            }
        }

        /**
         * Tests for search endpoints.
         */
        @Nested
        @DisplayName( "by Query (search)" )
        class SearchTest
        {
            private MockHttpServletRequestBuilder request;

            @BeforeEach
            void init()
            {
                final List<AirportEntity> entities =
                    List.of(
                        buildEntity(),
                        buildEntity(),
                        buildEntity()
                           );
                request = withHeaders( get( "/location/airport/search" ) )
                              .characterEncoding( "UTF-8" )
                              .param( "page", "1" )
                              .param( "size", "10" )
                              .param( "sort", "id,desc" )    // <-- no space after comma!
                              .param( "sort", "name,asc" );  // <-- no space after comma!

                final Page<AirportEntity> page = new PageImpl<>( entities );
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
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();
                final ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass( Pageable.class );

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           GetMethod.this::verifyPagedResponse,
                           () -> verify( repository )
                                     .advancedQuery( anyString(),
                                                     anyString(),
                                                     anyString(),
                                                     anyString(),
                                                     pageableCaptor.capture() ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                                     .andExpect( jsonPath( "$.content[0].ident" ).value( "KATLident" ) )
                                     .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                                     .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                                     .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() ),
                           // Verify how the data was retrieved
                           () -> verify( mapper, times( 3 ) )
                                     .domainToApi( any( Airport.class ) )
                );
            }


            @Test
            void restGetSearch_identifier_returnsSuccess() throws Exception
            {
                // --- given
                request.param( "ident", "KATLident" );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();
                final ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass( Pageable.class );

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           GetMethod.this::verifyPagedResponse,
                           () -> verify( repository )
                                     .advancedQuery( anyString(),
                                                     anyString(),
                                                     anyString(),
                                                     anyString(),
                                                     pageableCaptor.capture() ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                                     .andExpect( jsonPath( "$.content[0].ident" ).value( "KATLident" ) )
                                     .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                                     .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                                     .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() ),
                           // Verify how the data was retrieved
                           () -> verify( mapper, times( 3 ) )
                                     .domainToApi( any( Airport.class ) )
                );
            }


            @Test
            void restGetSearch_icaoCode_returnsSuccess() throws Exception
            {
                // --- given
                request.param( "icaoCode", "KATLident" );


                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();
                final ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass( Pageable.class );

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           GetMethod.this::verifyPagedResponse,
                           () -> verify( repository )
                                     .advancedQuery( anyString(),
                                                     anyString(),
                                                     anyString(),
                                                     anyString(),
                                                     pageableCaptor.capture() ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                                     .andExpect( jsonPath( "$.content[0].ident" ).value( "KATLident" ) )
                                     .andExpect( jsonPath( "$.content[0].name" ).value( "::NAME::" ) )
                                     .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                                     .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() ),
                           // Verify how the data was retrieved
                           () -> verify( mapper, times( 3 ) )
                                     .domainToApi( any( Airport.class ) )
                );
            }

        }

        /**
         * Counts of Airports. By hierarchy:
         *     Continent
         *     Country
         *     Region
         */
        @Nested
        @DisplayName( " airport counts.." )
        class AirportCounts
        {
            // --- Continent ---
            // /count/continent                                                 -> List<continents>
            // /count/continent/{continentCode}                                 -> List<countries>
            // /count/continent/{continentCode}/{countryCode}                   -> List<regions>
            // /count/continent/{continentCode}/{countryCode}/{regionCode}      -> region
            @Test
            @DisplayName( "by Continent" )
            void restGet_countContinents_returnsListOfContinents() throws Exception
            {
                // --- given
                final List<AirportCountInContinentEntity> entities =
                    List.of( new AirportCountInContinentEntity( "YY", "::YYNAME::", 42L ),
                             new AirportCountInContinentEntity( "ZZ", "::ZZNAME::", 21L )
                           );

                when( repository.countAirportsByContinent() )
                    .thenReturn( entities );

                final RequestBuilder request       = withHeaders( get( "/location/airport/count/continent" ) )
                                                         .characterEncoding( "UTF-8" );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           () -> verify( repository )
                                     .countAirportsByContinent(),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$[0].continentCode" ).value( "YY" ) )
                                     .andExpect( jsonPath( "$[0].name" ).value( "::YYNAME::" ) )
                                     .andExpect( jsonPath( "$[0].airportCount" ).value( 42 ) )
                                     .andExpect( jsonPath( "$[1].continentCode" ).value( "ZZ" ) )
                                     .andExpect( jsonPath( "$[1].name" ).value( "::ZZNAME::" ) )
                                     .andExpect( jsonPath( "$[1].airportCount" ).value( 21 ) ),
                           // Verify how the data was retrieved
                           () -> verify( mapper, times( 2 ) )
                                     .domainToApi( any( AirportCountInContinent.class ) )
                );
            }

            @Test
            @DisplayName( "by Continent" )
            void restGet_countByContinents_returnsListOfCountries() throws Exception
            {
                // --- given
                final List<AirportCountInCountryEntity> entities =
                    List.of( new AirportCountInCountryEntity( "YY", "::YYNAME::", 42L ),
                             new AirportCountInCountryEntity( "ZZ", "::ZZNAME::", 21L )
                           );

                when( repository.countCountryAirportsByContinent( anyString() ) )
                    .thenReturn( entities );

                final RequestBuilder request       = withHeaders( get( "/location/airport/count/continent/{continentCode}",
                                                                       "NA" ) )
                                                         .characterEncoding( "UTF-8" );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           // () -> assertThat( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // // () -> assertThat( response.getContentType() )
                           //           .isEqualTo( MediaType.APPLICATION_JSON_VALUE ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$[0].isoCountry" ).value( "YY" ) )
                                     .andExpect( jsonPath( "$[0].name" ).value( "::YYNAME::" ) )
                                     .andExpect( jsonPath( "$[0].airportCount" ).value( 42 ) )
                                     .andExpect( jsonPath( "$[1].isoCountry" ).value( "ZZ" ) )
                                     .andExpect( jsonPath( "$[1].name" ).value( "::ZZNAME::" ) )
                                     .andExpect( jsonPath( "$[1].airportCount" ).value( 21 ) ),
                           // Verify how the data was retrieved
                           () -> verify( repository )
                                     .countCountryAirportsByContinent( eq( "NA" ) ),
                           () -> verify( mapper, times( 1 ) )
                                     .domainToApiAirportsInCountry( anyList() )
                );
            }

            @Test
            @DisplayName( "by Continent and Country" )
            void restGet_countByContinentAndCountry_returnsListOfRegions() throws Exception
            {
                // --- given
                final List<AirportCountInRegionEntity> entities =
                    List.of( new AirportCountInRegionEntity( "YY", "::YYNAME::", 42L ),
                             new AirportCountInRegionEntity( "ZZ", "::ZZNAME::", 21L )
                           );

                when( repository.countRegionAirportsByContinentAndIsoCountry( anyString(), anyString() ) )
                    .thenReturn( entities );

                final RequestBuilder request      =
                    withHeaders( get( "/location/airport/count/continent/{continentCode}/{countryCode}",
                                      "NA", "US" ) )
                        .characterEncoding( "UTF-8" );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$[0].isoRegion" ).value( "YY" ) )
                                     .andExpect( jsonPath( "$[0].name" ).value( "::YYNAME::" ) )
                                     .andExpect( jsonPath( "$[0].airportCount" ).value( 42 ) )
                                     .andExpect( jsonPath( "$[1].isoRegion" ).value( "ZZ" ) )
                                     .andExpect( jsonPath( "$[1].name" ).value( "::ZZNAME::" ) )
                                     .andExpect( jsonPath( "$[1].airportCount" ).value( 21 ) )
                );
            }


            // --- Country ---
            @Test
            @DisplayName( "per Country by Continent" )
            void restGet_countAirportsByCountry_returnsSuccess() throws Exception
            {
                // --- given
                final List<AirportCountInCountryEntity> entities =
                    List.of( new AirportCountInCountryEntity( "YY", "::XYNAME::", 42L ),
                             new AirportCountInCountryEntity( "ZZ", "::YZNAME::", 21L )
                           );

                when( repository.countCountryAirportsByContinent( eq( "AS" ) ) )
                    .thenReturn( entities );

                final RequestBuilder request = withHeaders( get( "/location/airport/count/continent/{continentCode}",
                                                                 "AS" ) )
                                                   .characterEncoding( "UTF-8" );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           // () -> assertThat( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // // () -> assertThat( response.getContentType() )
                           //           .isEqualTo( MediaType.APPLICATION_JSON_VALUE ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$[0].isoCountry" ).value( "YY" ) )
                                     .andExpect( jsonPath( "$[0].name" ).value( "::XYNAME::" ) )
                                     .andExpect( jsonPath( "$[0].airportCount" ).value( 42 ) )
                                     .andExpect( jsonPath( "$[1].isoCountry" ).value( "ZZ" ) )
                                     .andExpect( jsonPath( "$[1].name" ).value( "::YZNAME::" ) )
                                     .andExpect( jsonPath( "$[1].airportCount" ).value( 21 ) ),
                           // Verify how the data was retrieved
                           () -> verify( repository )
                                     .countCountryAirportsByContinent( eq( "AS" ) ),
                           () -> verify( mapper, times( 1 ) )
                                     .domainToApiAirportsInCountry( anyList() )
                         );
            }

            @Test
            @DisplayName( "per Country by Continent" )
            void restGet_countAirportsByCountryAndBadContinent_returnsFailure() throws Exception
            {
                // --- given
                final List<AirportCountInCountryEntity> entities =
                    List.of( new AirportCountInCountryEntity( "YY", "::XYNAME::", 42L ),
                             new AirportCountInCountryEntity( "ZZ", "::YZNAME::", 21L )
                           );

                when( repository.countCountryAirportsByContinent( eq( "CC" ) ) )
                    .thenReturn( entities );

                final RequestBuilder request = withHeaders( get( "/location/airport/count/continent/{continentCode}",
                                                                 "CC" ) )
                                                   .characterEncoding( "UTF-8" );


                // --- when
                final ResultActions resultActions = mvc.perform( request );


                // --- then
                resultActions.andDo( print() );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.NOT_FOUND.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.title" ).value( "Not Found" ) )
                                     .andExpect( jsonPath( "$.status" ).value( "404" ) )
                                     // .andExpect( jsonPath( "$.detail" ).value( 42 ) )
                                     .andExpect( jsonPath("$.detail",
                                                          containsString("No static resource location/airport/count/continent/CC for request '/location/airport/count/continent/CC'." ) ) )
                                     .andExpect( jsonPath( "$.instance" )
                                                     .value( "location/airport/count/continent/CC" ) )
                                     // .andExpect( jsonPath( "$exception" ).value( "org.springframework.web.servlet.resource.NoResourceFoundException: No static resource location/airport/count/continent/CC for request '/location/airport/count/continent/CC'." ) )
                                     // .andExpect( jsonPath( "$.exception" ), containsString( "foo" )
                                     // .andExpect(jsonPath("$.performers[1].name", containsString("di Me" ) ) )
                                     .andExpect( jsonPath( "$.Exception",
                                                           containsString( "org.springframework.web.servlet.resource.NoResourceFoundException:" ) ) )
                                     .andExpect( jsonPath( "$.Exception",
                                                           containsString( "No static resource location/airport/count/continent/CC for request '/location/airport/count/continent/CC'." ) ) )
                );
            }

            // --- Region ---
            @Test
            @DisplayName( "by Region" )
            void restGet_countAirportsByRegion_returnsSuccess() throws Exception
            {
                // --- given
                final AirportCountInRegionEntity testResult = new AirportCountInRegionEntity( "YY", "::YYNAMEY::", 42L );

                when( repository.countAirportsByRegion( anyString() ) )
                    .thenReturn( testResult );

                final RequestBuilder request = withHeaders( get( "/location/airport/count/region/{regionCode}",
                                                                 "RE" ) )
                                                   .characterEncoding( "UTF-8" );

                // --- when
                final ResultActions resultActions =
                    mvc
                        .perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.OK.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // verify the resulting JSON....
                           () -> resultActions
                                     .andExpect( jsonPath( "$.isoRegion" ).value( "YY" ) )
                                     .andExpect( jsonPath( "$.name" ).value( "::YYNAMEY::" ) )
                                     .andExpect( jsonPath( "$.airportCount" ).value( 42 ) ),
                           // Verify how the data was retrieved
                           () -> verify( repository )
                                     .countAirportsByRegion( eq( "RE" ) ),
                           () -> verify( mapper, times( 1 ) )
                                     .domainToApi( any( AirportCountInRegion.class ) )
                );
            }
        }

        // ----- Support methods -----
        private AirportEntity buildEntity()
        {
            return AirportEntity.builder()
                                .id( 1L )
                                .ident( "KATLident" )
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
                                .gpsCode( "KATLgps" )
                                .iataCode( "KATLiata" )
                                .icaoCode( "ATL" )
                                .localCode( "KATLlocal" )
                                .build();
        }

        private void verifyPagedResponse()
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
                .pageNumberMatches( 1 )
                .pageSizeMatches( 10 )
                .sortCriteriaMatches( "name", Sort.Direction.ASC )
                .sortCriteriaMatches( "id", Sort.Direction.DESC );
        }
    }

    // ========== UPDATE ==========
    // ===== PATCH =====
    /**
     * Tests for PATCH Http Methods.
     */
    @Nested
    @DisplayName( "HTTP PATCH" )
    class PatchMethod           // NOPMD
    {
    }

    // ===== PUT =====
    /**
     * Tests for PUT Http Methods.
     */
    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod             // NOPMD
    {
    }

    // ========== DELETE ==========
    // ===== DELETE =====
    /**
     * Tests for DELETE Http Methods.
     */
    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod          // NOPMD
    {
    }


    // ========== Administrative ==========
    // ===== HEAD =====
    /**
     * Tests for HEAD Http Methods.
     */
    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod            // NOPMD
    {
    }

    // ===== INFO =====
    /**
     * Tests for INFO Http Methods.
     */
    @Nested
    @DisplayName( "HTTP INFO" )
    class InfoMethod            // NOPMD
    {
    }

    // ===== OPTION =====
    /**
     * Tests for OPTIONS Http Methods.
     */
    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod         // NOPMD
    {
    }

    // ===== TRACE =====

}
