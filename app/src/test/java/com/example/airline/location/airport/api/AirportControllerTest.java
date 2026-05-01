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
import java.util.ArrayList;
import java.util.List;

import com.example.airline.airport.AirportCountInRegionDTO;
import com.example.airline.airport.AirportDTO;
import com.example.airline.location.airport.mapper.AirportDtoMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.airport.service.AirportCreateService;
import com.example.airline.location.airport.service.AirportDeleteService;
import com.example.airline.location.airport.service.AirportReadService;
import com.example.airline.location.airport.service.AirportUpdateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


/**
 * REST Controller for Airport entities.
 *
 * Paged list of all airports
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
 */
@DisplayName( "Airport: API (/airport)" )
@WebMvcTest( controllers = AirportController.class )
@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@AutoConfigureMockMvc( addFilters = false )
class AirportControllerTest //extends RestControllerTestBase
{
    @MockitoBean
    protected AirportRepository    repository;

    @MockitoSpyBean
    private AirportController      controller;

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
    @DisplayName( "HTTP GET" )
    class GetMethod
    {

        @Nested
        @DisplayName( "by Key" )
        class ByIdTest
        {

        }

        @Nested
        @DisplayName( "by Identifier" )
        class ByIdentifierTest
        {
        }

        @Nested
        @DisplayName( "Lists of Airports" )
        class ListsOfAirports
        {

            @Nested
            @DisplayName( "by Continent" )
            class ByContinent
            {
            }

            @Nested
            @DisplayName( "by Country" )
            class ByCountry
            {
            }

            @Nested
            @DisplayName( "by Region" )
            class ByRegion
            {
                @Test
                @DisplayName( "valid region code" )
                void methodGetAirport_ByRegionId_returnsList()
                {
                    // --- given
                    final Airport airport = new Airport( 1L, "ATL", "Large", "Hartsfield",
                                                         BigDecimal.valueOf( 1.23 ), BigDecimal.TWO, 5, "NA", "US", "US-GA", "Atlanta",
                                                         "Atlanta",
                                                         null,
                                                         null,
                                                         null,null,
                                                         null,null,
                                                         "Key"
                    );

                    final List<Airport> listOfAirports = new ArrayList<>();
                    listOfAirports.add( airport );

                    when( readService.findAirportsByRegion( anyString() ) )
                            .thenReturn( listOfAirports );

                    // --- when
                    ResponseEntity<List<AirportDTO>> response = controller.restGetAirportsByRegion( "NA" );

                    // --- then
                    final HttpHeaders            headers  = response.getHeaders();

                    assertAll( () -> assertNotNull( response.getBody() ),
//                               () -> assertEquals( "application/json;charset=UTF-8", headers.getFirst( "Content-Type" )  ),
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
        class AllTest
        {
        }

        /**
         * Tests for search endpoints.
         */
        @Nested
        @DisplayName( "by Query (search)" )
        class SearchTest
        {
        }


        @Nested
        @DisplayName( " airport counts.." )
        class AirportCounts
        {
            @Test
            @DisplayName( "by country grouped by region" )
            void methodGetAirportCount_ByCountryCode_returnsList()
            {
                // --- given
                final AirportCountInRegion airport = new AirportCountInRegion( "US-GA", "Georgia", 5

                );

                final List<AirportCountInRegion> listOfAirports = new ArrayList<>();
                listOfAirports.add( airport );

                when( readService.countRegionAirportsByCountry( anyString() ) )
                        .thenReturn( listOfAirports );

                // --- when
                ResponseEntity<List<AirportCountInRegionDTO>> response = controller.restGetCountAirportsByRegion( "NA" );

                // --- then
//                final HttpHeaders            headers  = response.getHeaders();

                assertAll( () -> assertNotNull( response.getBody() ),
//                               () -> assertEquals( "application/json;charset=UTF-8", headers.getFirst( "Content-Type" )  ),
                           () -> verifyNoInteractions( createService ),
                           () -> verify( readService, times( 1 ) )
                                   .countRegionAirportsByCountry(  anyString() ),
                           () -> verifyNoInteractions( updateService ),
                           () -> verifyNoInteractions( deleteService ),
                           () -> verify( dtoMapper ).domainToApiAirportsInRegion( anyList() )
                         );
            }


            @Test
            @DisplayName( "by Region" )
            void restGet_countAirportsByRegion_returnsSuccess() throws Exception
            {
                List<AirportCountInRegion> entities =
                        List.of( new AirportCountInRegion( "YY", "::YYNAME::", 42L ),
                                 new AirportCountInRegion( "ZZ", "::ZZNAME::", 21L )
                               );

                when( readService.countRegionAirportsByCountry( anyString() ) )
                        .thenReturn( entities );


//                final RequestBuilder request = withHeaders( get( "/location/airport/summary/region/code/{regionCode}",
//                                                                "RE" ) );
//
//                final MvcResult mvcResult = mvc
//                        .perform( request )
//                        .andDo( print() )
//                        .andExpect( status().isOk() )
//                        .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
//                        // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
//                        //      don't complain about lack of assertions in tests
//                        // TODO test for an empty result body (empty list)
//                        .andReturn();
//                MockHttpServletResponse response = mvcResult.getResponse();
//                ObjectMapper mapper = new ObjectMapper();
////                List<AirportDTO> result = mapper.readValue( response.getContentAsString(), new TypeReference<List<AirportDTO>>() );
//                AirportDTO[] resultA = mapper.readValue( response.getContentAsString(), AirportDTO[].class );
//                List<AirportDTO> result = List.of( resultA );

                // --- then
                // TODO need to assert the resulting JSON....

//                assertThat( response.getContentType() )
//                        .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
//                assertAll( () -> assertThat( response.getContentType() )
//                        .contains( MediaType.APPLICATION_JSON_VALUE )

//                           () -> assertThat( result )
//                                   .isNotNull()
//                                   .hasSize( 2 )

//                           () -> assertEquals( "YY", result.get(0).getRegionCode() ),
//                           () -> assertEquals( "::YYNAME::", result.get(0).getName() ),
//                           () -> assertEquals( 42, result.get(0).getAirportCount() )
//                         );
            }
        }
    }


    @Nested
    @DisplayName( "HTTP POST" )
    class PostMethod
    {
    }

    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod
    {
    }

    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod
    {
    }

    @Nested
    @DisplayName( "HTTP PATCH" )
    class PatchMethod
    {
    }

    @Nested
    @DisplayName( "HTTP INFO" )
    class InfoMethod
    {
    }

    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod
    {
    }

    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod
    {
    }

}
