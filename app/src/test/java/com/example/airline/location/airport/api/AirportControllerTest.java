package com.example.airline.location.airport.api;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.example.airline.airport.AirportCountInRegionDTO;
import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.mapper.AirportDtoMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.airport.service.AirportCreateService;
import com.example.airline.location.airport.service.AirportDeleteService;
import com.example.airline.location.airport.service.AirportReadService;
import com.example.airline.location.airport.service.AirportUpdateService;
import com.example.utility.HeaderUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.http.HttpHeadersAssert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


/**
 * REST Controller for Airport entities.
 *
 * <p>Paged list of all airports
 * <pre>
 * By Continent
 *    List of all airports within the continent.
 *    List of Count of airports by Country.
 * By Country
 *    List of all airports within the Country.
 *    List of airport counts grouped by Region.
 * By Region
 *    List of all airports within the Region.
 *
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
class AirportControllerTest //extends RestControllerTestBase
{
    private static final String    TEST_PARENT    = "testParent";
    private static final String    TEST_STATE     = "testState";

    @MockitoBean
    protected AirportRepository    repository;

    @MockitoSpyBean
    private AirportController      controller;

    // Service layer support classes
    @MockitoSpyBean
    private AirportCreateService   createService;
    @MockitoSpyBean
    private AirportReadService     readService;
    @MockitoSpyBean
    private AirportUpdateService   updateService;
    @MockitoSpyBean
    private AirportDeleteService   deleteService;

    @MockitoSpyBean
    private   AirportDtoMapper     dtoMapper;

    private HttpHeaders            requestHeader;

    @BeforeEach
    void init()
    {
        requestHeader = buildDefaultHeaders();
    }



    // ========== CREATE ==========
    // ===== POST =====

    /**
     * Verify Create (HTTP POST) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP POST" )
    class PostMethod            // NOPMD
    {
    }


    // ========== READ ==========
    // ===== GET =====
    /**
     * Verify Read (HTTP GET) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP GET" )
    class GetMethod             // NOPMD
    {

        @Nested
        @DisplayName( "by Key" )
        class ByIdTest          // NOPMD
        {
        }

        @Nested
        @DisplayName( "by Identifier" )
        class ByIdentifierTest  // NOPMD
        {
        }

        /**
         * Test for collections of airports by particular geopolitical division.
         */
        @Nested
        @DisplayName( "Lists of Airports" )
        class ListsOfAirports
        {

            /**
             * Verify a collection of airports within a specific continent.
             */
            @Nested
            @DisplayName( "by Continent" )
            class ByContinent   // NOPMD
            {
            }

            /**
             * Verify a collection of airports within a specific country.
             */
            @Nested
            @DisplayName( "by Country" )
            class ByCountry     // NOPMD
            {
            }

            /**
             * Verify collections of Airports within a single Region.
             */
            @Nested
            @DisplayName( "by Region" )
            class ByRegion      // NOPMD
            {
                @Test
                @DisplayName( "valid region code" )
                void methodGetAirport_byRegionId_returnsList()
                {
                    // --- given
                    final Airport airport = new Airport( 1L, "ATL",
                                                         "Large",
                                                         "Hartsfield",
                                                         BigDecimal.valueOf( 1.23 ),
                                                         BigDecimal.TWO,
                                                         5,
                                                         "NA",
                                                         "US",
                                                         "US-GA",
                                                         "Atlanta",
                                                         "Atlanta",
                                                         null,
                                                         null,
                                                         null, null,
                                                         null, null,
                                                         "Key"
                    );

                    final List<Airport> listOfAirports = new ArrayList<>();
                    listOfAirports.add( airport );

                    when( readService.findAirportsByRegion( anyString() ) )
                        .thenReturn( listOfAirports );

                    // --- when
                    final ResponseEntity<List<AirportDTO>> response = controller.restGetAirportsByRegion( "NA", requestHeader );

                    // --- then
                    final HttpHeaders            headers  = response.getHeaders();
                    final HttpHeadersAssert headersAssert = new HttpHeadersAssert( headers );


                    assertAll( () -> headersAssert
                                   .doesNotContainHeader( "NoWay" )
                                   .hasValue( HeaderUtility.TRACEID, TEST_PARENT )
                                   .hasValue( HeaderUtility.TRACESTATE, TEST_STATE )
                                   .hasValue( HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8" ),
                               () -> assertNotNull( response.getBody() ),
                               () -> verifyNoInteractions( createService ),
                               () -> verify( readService, times( 1 ) )
                                   .findAirportsByRegion(  anyString() ),
                               () -> verifyNoInteractions( updateService ),
                               () -> verifyNoInteractions( deleteService ),
                               () -> verify( dtoMapper ).domainToApi( any( Airport.class ) )
                    );
                }
            }

        }


        @Nested
        @DisplayName( " all" )
        class AllTest           // NOPMD
        {
        }

        /**
         * Tests for search endpoints.
         */
        @Nested
        @DisplayName( "by Query (search)" )
        class SearchTest        // NOPMD
        {
        }


        /**
         * Collection of tests for counts of airports within a Continent, Country or Region.
         */
        @Nested
        @DisplayName( " airport counts.." )
        class AirportCounts     // NOPMD
        {
            @Test
            @DisplayName( "by country grouped by region" )
            void methodGetAirportCount_byCountryCode_returnsList()
            {
                // --- given
                final AirportCountInRegion airport = new AirportCountInRegion( "US-GA", "Georgia", 5

                );

                final List<AirportCountInRegion> listOfAirports = new ArrayList<>();
                listOfAirports.add( airport );

                when( readService.countRegionAirportsByCountry( anyString() ) )
                    .thenReturn( listOfAirports );

                // --- when
                final ResponseEntity<List<AirportCountInRegionDTO>> response =
                    controller.restGetCountAirportsByRegion( "NA", requestHeader );

                // --- then
                final HttpHeaders            headers  = response.getHeaders();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( headers );

                assertAll( () -> headersAssert
                               .doesNotContainHeader( "NoWay" )
                               .hasValue( "TRACEPARENT", "testParent" )
                               .hasValue( "TRACESTATE", "testState" )
                               .hasValue( HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8" ),
                           // TODO validate the body
                           () -> assertNotNull( response.getBody() ),
                           // Check use of service layer classes
                           () -> verifyNoInteractions( createService ),
                           () -> verify( readService, times( 1 ) )
                               .countRegionAirportsByCountry(  anyString() ),
                           () -> verifyNoInteractions( updateService ),
                           () -> verifyNoInteractions( deleteService ),
                           // With response body, make sure it is converted to a DTO
                           () -> verify( dtoMapper ).domainToApiAirportsInRegion( anyList() )
                );
            }


            @Test
            @DisplayName( "by Region" )
            void restGet_countAirportsByRegion_returnsSuccess() throws Exception
            {
                // --- given
                final List<AirportCountInRegion> entities =
                    List.of( new AirportCountInRegion( "YY", "::YYNAME::", 42L ),
                             new AirportCountInRegion( "ZZ", "::ZZNAME::", 21L )
                           );
                when( readService.countRegionAirportsByCountry( anyString() ) )
                    .thenReturn( entities );

                // --- when
                final ResponseEntity<List<AirportCountInRegionDTO>> response =
                    controller
                        .restGetCountAirportsByRegion( "NA", requestHeader );

                // --- then
                final HttpHeaders            headers  = response.getHeaders();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( headers );

                assertAll( () -> headersAssert
                               .doesNotContainHeader( "NoWay" )
                               .hasValue( HeaderUtility.TRACEID, TEST_PARENT )
                               .hasValue( HeaderUtility.TRACESTATE, TEST_STATE )
                               .hasValue( HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8" ),
                           () -> assertNotNull( response.getBody() ),
                           () -> verifyNoInteractions( createService ),
                           () -> verify( readService, times( 1 ) )
                               .countRegionAirportsByCountry(  anyString() ),
                           () -> verifyNoInteractions( updateService ),
                           () -> verifyNoInteractions( deleteService ),
                           () -> verify( dtoMapper ).domainToApiAirportsInRegion( anyList() )
                );
            }
        }
    }

    // --- Single ---
    // --- Multiple ---

    // ========== UPDATE ==========
    // ===== PATCH =====
    /**
     * Verify Update (HTTP PATCH) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP PATCH" )
    class PatchMethod           // NOPMD
    {
    }

    // ===== PUT =====
    /**
     * Verify Update (HTTP PUT) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod             // NOPMD
    {
    }


    // ========== DELETE ==========
    // ===== DELETE =====
    /**
     * Verify Delete (HTTP DELETE) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod          // NOPMD
    {
    }


    // ========== Administrative ==========
    // ===== HEAD =====
    /**
     * Verify Head (HTTP HEAD) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod            // NOPMD
    {
    }

    // ===== INFO =====
    /**
     * Verify Info (HTTP INFO) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP INFO" )
    class InfoMethod            // NOPMD
    {
    }

    // ===== OPTION =====
    /**
     * Verify Options (HTTP OPTIONS) methods directly without making a REST call.
     */
    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod         // NOPMD
    {
    }

    // ===== TRACE =====

    // ========== Test support ==========

    private HttpHeaders buildDefaultHeaders()
    {
        final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
                                                            StandardCharsets.UTF_8 );
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
        headers.set( "Content-Type", desiredContentType.toString() );
        headers.set( HttpHeaders.ACCEPT_LANGUAGE, "en-US" );
        headers.set( HttpHeaders.ACCEPT_CHARSET, "utf-8" );
        headers.set( HttpHeaders.ACCEPT_ENCODING, "gzip" );
        headers.set( HeaderUtility.TRACESTATE, "testState" );
        headers.set( HeaderUtility.TRACEID, "testParent" );
        headers.set( "NoWay", "Should not exist" );

        return new HttpHeaders( headers );
    }

    // private AirportEntity buildEntity()
    // {
    //     return AirportEntity.builder()
    //                         .id( 1L )
    //                         .ident( "KATL" )
    //                         .type( "large_airport" )
    //                         .name( "::NAME::" )
    //                         .latitude( BigDecimal.valueOf( 123.456 ) )
    //                         .longitude( BigDecimal.valueOf( 987.654 ) )
    //                         .elevation( 55 )
    //                         .continent( "NA" )
    //                         .isoCountry( "USA" )
    //                         .isoRegion( "GA" )
    //                         .municipality( "Atlanta" )
    //                         .scheduledService( "yes" )
    //                         .gpsCode( "KATL" )
    //                         .iataCode( "KATL" )
    //                         .icaoCode( "ATL" )
    //                         .localCode( "KATL" )
    //                         .build();
    // }



}
