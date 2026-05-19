package com.example.airline.location.airport.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import com.example.airline.location.airport.api.AirportController;
import com.example.airline.location.airport.mapper.AirportEntityMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInCountryEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInRegionEntity;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import com.example.airline.location.airport.persistence.model.AirportSummaryEntity;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


/**
 * Test cases for the AirportReadService class.
 */
//@ExtendWith( SpringExtension.class )
//@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@WebMvcTest( controllers = AirportController.class )
@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Airport: Service - Read" )
class AirportReadServiceTest
{
    @MockitoBean
    private AirportRepository repository;

    @Autowired
    private AirportReadService readService;

    @MockitoSpyBean
    private AirportEntityMapper mapper;


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



    // ========== Read ==========
    /**
     * Tests for GET Http Methods.
     */
    @Nested
    @DisplayName( "Read" )
    class Read           // NOPMD
    {
        /**
         * Test various ways to find Airports.
         */
        @Nested
        @DisplayName( "Find" )
        class Find
        {
            /**
             * Verify methods that find single instances.
             */
            @Nested
            @DisplayName( "Individual Airport" )
            class IndividualAirports    // NOPMD
            {
                // By Code

                // By Name

                // By ID
            }

            /**
             * Verify methods that find collections.
             */
            @Nested
            @DisplayName( "List of Airports" )
            class ListOfAirports        // NOPMD
            {
                @DisplayName( "by continent" )
                @Test
                void find_byContinent_returnsList()
                {
                    // -- given
                    // -- when
                    //final List<Airport> airports = service.findAirportsByContinent( "NA" );
                    // -- then
                }

                @DisplayName( "by country" )
                @Test
                void find_byCountry_returnsList()
                {
                    // -- given
                    // -- when
                    // -- then
                    //final List<Airport> airports = service.findAirportsbyCountry( "PH" );
                }

                @DisplayName( "by region" )
                @Test
                void find_byRegion_returnListOfAirports()
                {
                    // --- given
                    final List<AirportEntity> entities =
                            List.of( new AirportEntity( 1L,
                                                        "ident", "type",
                                                        "::YYNAME::",
                                                        BigDecimal.valueOf( 12.34 ), BigDecimal.valueOf( 56.78 ),
                                                        50,
                                                        "continent", "country", "region",
                                                        "municiipality",
                                                        "NO",
                                                        "foo", "icao", "iata", "local",
                                                        null, null, null ),
                                     new AirportEntity( 2L,
                                                        "ident", "type",
                                                        "::YYNAME::",
                                                        BigDecimal.valueOf( 12.34 ), BigDecimal.valueOf( 56.78 ),
                                                        50,
                                                        "continent", "country", "region",
                                                        "municiipality",
                                                        "NO",
                                                        "foo", "icao", "iata", "local",
                                                        null, null, null )
                                   );

                    when( repository.findByIsoRegion( anyString() ) )
                            .thenReturn( entities );

                    // --- when
                    final List<Airport> airports = readService.findAirportsByRegion( "US-GA" );

                    // --- then
                    assertAll( () -> assertThat( airports )
                                       .isNotNull()
                                       .hasSize( 2 ),
                               () -> assertEquals( "iata", airports.get( 0 ).getIataCode() ),
                               () -> assertEquals( "::YYNAME::", airports.get( 0 ).getName() ),
                               () -> verify( repository ).findByIsoRegion( anyString() ),
                               () -> verify( mapper ).entityToDomain( anyList() )
                    );
                }
            }

        }

        /**
         * Validate methods that count groups of entities.
         */
        @Nested
        @DisplayName( "Counts" )
        class Counts           // NOPMD
        {
            // count by
            //   continent
            //   countries in a continent
            //   regions in a country
            //   cities by region

            // --- airports in continents
            @DisplayName( "by continent" )
            @Test
            void airportCounts_byContinent_returnsList()
            {
                // --- given
                final List<AirportCountInContinentEntity> entities =
                        List.of( new AirportCountInContinentEntity( "YY", "::YYNAME::", 42L ),
                                 new AirportCountInContinentEntity( "ZZ", "::ZZNAME::", 21L )
                               );

                when( repository.countAirportsByContinent() )
                        .thenReturn( entities );

                // --- when
                final List<AirportCountInContinent>
                        result = readService.countAirportsByContinent();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( 2, result.size() ),
                           () -> assertEquals( "YY", result.get( 0 ).getContinentCode() ),
                           () -> assertEquals( "::YYNAME::", result.get( 0 ).getName() ),
                           () -> assertEquals( 42, result.get( 0 ).getAirportCount() )
                );
            }


            // --- airports in a continent grouped by country

            // --- airports in a specific country grouped by region

            /**
             * Tests for finding Airports within a Continent, country or Region.
             */
            @Nested
            @DisplayName( "by Country" )
            class ByCountry
            {
                @DisplayName( "by country" )
                @Test
                void airportCounts_byCountry_returnsList()
                {
                    // --- given
                    final List<AirportCountInCountryEntity> entities =
                            List.of( new AirportCountInCountryEntity( "YY", "::YYNAME::", 42L ),
                                     new AirportCountInCountryEntity( "ZZ", "::ZZNAME::", 21L )
                                   );

                    when( repository.countAirportsByCountry( anyString() ) )
                            .thenReturn( entities );

                    // --- when
                    final List<AirportCountInCountry>
                            result = readService.countAirportsByCountry( anyString() );

                    // --- then
                    assertAll( () -> assertThat( result )
                                       .isNotNull()
                                       .hasSize( 2 ),
                               () -> assertEquals( "YY", result.get( 0 ).countryCode() ),
                               () -> assertEquals( "::YYNAME::", result.get( 0 ).name() ),
                               () -> assertEquals( 42, result.get( 0 ).airportCount() )
                    );
                }

                @DisplayName( "by country / region" )
                @Test
                void airportCounts_byCountryRegion_returnsList()
                {
                    // --- given
                    final List<AirportCountInRegionEntity> entities =
                            List.of( new AirportCountInRegionEntity( "YY", "::YYNAME::", 42L ),
                                     new AirportCountInRegionEntity( "ZZ", "::ZZNAME::", 21L )
                                   );

                    when( repository.countRegionAirportsByCountry( anyString() ) )
                            .thenReturn( entities );

                    // --- when
                    final List<AirportCountInRegion>
                            result = readService.countRegionAirportsByCountry( anyString() );

                    // --- then
                    assertAll( () -> assertThat( result )
                                       .isNotNull()
                                       .hasSize( 2 ),
                               () -> assertEquals( "YY", result.get( 0 ).getRegionCode() ),
                               () -> assertEquals( "::YYNAME::", result.get( 0 ).getName() ),
                               () -> assertEquals( 42, result.get( 0 ).getAirportCount() )
                    );
                }
            }

            @DisplayName( "by region" )
            @Test
            void airportCounts_byRegion_returnsList()
            {
                final List<AirportCountInRegionEntity> entities =
                        List.of( new AirportCountInRegionEntity( "YY", "::YYNAME::", 42L ),
                                 new AirportCountInRegionEntity( "ZZ", "::ZZNAME::", 21L )
                        );

                when( repository.countAirportsByRegion( anyString() ) )
                        .thenReturn( entities );

                final List<AirportCountInRegion>
                        result = readService.countAirportsByRegion( anyString() );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull()
                                   .hasSize( 2 ),
                           () -> assertEquals( "YY", result.get( 0 ).getRegionCode() ),
                           () -> assertEquals( "::YYNAME::", result.get( 0 ).getName() ),
                           () -> assertEquals( 42, result.get( 0 ).getAirportCount() ),
                           () -> verify( mapper ).entityToDomainAirportsInRegion( anyList() )
                );
            }

        }

        /**
         * Verify methods that find subsets of Airports.
         */
        @Nested
        @DisplayName( "Summary" )
        class Summary           // NOPMD
        {
            @DisplayName( "by continent" )
            @Test
            void airportSummary_byContinent_returnsList()
            {
                // --- given

                // --- when
                final List<AirportSummaryEntity>
                        result = readService.findSummaryByContinent( "NA" );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull()
                );
            }


            @DisplayName( "by country" )
            @Test
            void airportSummary_byCountry_returnsList()
            {
                // --- given

                // --- when
                final List<AirportSummaryEntity>
                        result = readService.findSummaryByCountry( "PH" );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull()
                );
            }

            @DisplayName( "by region" )
            @Test
            void airportSummary_byRegion_returnsList()
            {
                // --- given

                // --- when
                final List<AirportSummaryEntity>
                        result = readService.findSummaryByRegion( "US-GA" );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull()
                );
            }

        }


        /**
         * Verify general purpose search methods.
         */
        @Nested
        @DisplayName( "Query" )
        class Queru           // NOPMD
        {
            private ArgumentCaptor<Pageable> pageableCaptor;
            private Pageable                 pageable;


            @BeforeEach
            void init()
            {
                pageableCaptor = ArgumentCaptor.forClass( Pageable.class );
                pageable       = PageRequest.of( 1, 10 );
            }

            @Test
            @DisplayName( "null arguments" )
            void query_nullArgs_returnFullPagedList()
            {
                // --- given
                final List<AirportEntity> entities =
                        List.of(
                                buildEntity(),
                                buildEntity(),
                                buildEntity()
                               );
                final Page<AirportEntity> page = new PageImpl<>( entities );
                when( repository.advancedQuery( anyString(),    // iataCode
                                                anyString(),    // icaoCode
                                                anyString(),    // ident
                                                anyString(),    // name
                                                any( Pageable.class ) ) )
                        .thenReturn( page );



                // --- when
                final Page<Airport>
                        result = readService.advancedQuery( null,
                                                            null,
                                                            null,
                                                            null,
                                                            pageable );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull(),
                           () -> PageableAssert
                                   .assertThat( pageable )
                                   .pageNumberMatches( 1 )
                                   .pageSizeMatches( 10 ),
                           // empty string substituted for null query parms
                           () -> verify( repository )
                                   .advancedQuery( eq( "" ),
                                                   eq( "" ),
                                                   eq( "" ),
                                                   eq( "" ),
                                                   pageableCaptor.capture() )
                );
            }

            @Test
            @DisplayName( "non-null args" )
            void query_allArgs_returnFullPagedList()
            {
                // --- given
                final List<AirportEntity> entities =
                        List.of(
                                buildEntity(),
                                buildEntity(),
                                buildEntity()
                               );
                final Page<AirportEntity> page = new PageImpl<>( entities );
                when( repository.advancedQuery( anyString(),    // iataCode
                                                anyString(),    // icaoCode
                                                anyString(),    // ident
                                                anyString(),    // name
                                                any( Pageable.class ) ) )
                        .thenReturn( page );



                // --- when
                // lowercase params should be made upper before calling repository
                final Page<Airport>
                        result = readService.advancedQuery( "iata",
                                                            "icao",
                                                            "ident",
                                                            "name",
                                                            pageable );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull(),
                           () -> PageableAssert
                                   .assertThat( pageable )
                                   .pageNumberMatches( 1 )
                                   .pageSizeMatches( 10 ),
                           // Query parms are converted to uppercase
                           () -> verify( repository )
                                   .advancedQuery( eq( "IATA" ),
                                                   eq( "ICAO" ),
                                                   eq( "IDENT" ),
                                                   eq( "NAME" ),
                                                   pageableCaptor.capture() )
                );
            }


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
