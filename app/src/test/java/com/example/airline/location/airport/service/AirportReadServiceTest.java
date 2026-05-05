package com.example.airline.location.airport.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
    private AirportReadService service;

    @MockitoSpyBean
    private AirportEntityMapper mapper;


    // ========== Create ==========
    @Nested
    @DisplayName( "Create" )
    class Create           // NOPMD
    {
    }


    // ========== Read ==========
//    @Nested
//    @DisplayName( "Read" )
//    class Read           // NOPMD
//    {
        @Nested
        @DisplayName( "Find" )
        class Find
        {
            @Nested
            @DisplayName( "Individual Airport" )
            class IndividualAirports
            {
                // By Code

                // By Name

                // By ID
            }

            @Nested
            @DisplayName( "List of Airports" )
            class ListOfAirports
            {
    //            @DisplayName( "by continent" )
    //            @Test
    //            void find_byContinent_returnsList()
    //            {
    //                // -- given
    //                // -- when
    //                final List<Airport> airports = service.findAirportsByContinent( "NA" );
    //                // -- then
    //            }

    //            @DisplayName( "by country" )
    //            @Test
    //            void find_byCountry_returnsList()
    //            {
    //                // -- given
    //                // -- when
    //                // -- then
    //                final List<Airport> airports = service.findAirportsbyCountry( "PH" );
    //            }

                @DisplayName( "by region" )
                @Test
                void find_byRegion_returnListOfAirports()
                {
                    // --- given
                    List<AirportEntity>entities =
                            List.of( new AirportEntity( 1L, "ident", "type", "::YYNAME::", BigDecimal.valueOf( 12.34 ), BigDecimal.valueOf( 56.78 ), 50, "continent", "country", "region", "municiipality", "NO", "foo", "icao", "iata", "local", null, null, null ),
                                     new AirportEntity( 2L, "ident", "type", "::YYNAME::", BigDecimal.valueOf( 12.34 ), BigDecimal.valueOf( 56.78 ), 50, "continent", "country", "region", "municiipality", "NO", "foo", "icao", "iata", "local", null, null, null )
                                   );

                    when( repository.findByIsoRegion( anyString() ) )
                            .thenReturn( entities );

                    // --- when
                    final List<Airport> airports = service.findAirportsByRegion( "US-GA" );

                    // --- then
                    assertAll( () -> assertThat( airports )
                                       .isNotNull()
                                       .hasSize( 2 ),
                               () -> assertEquals( "iata", airports.get(0).getIataCode() ),
                               () -> assertEquals( "::YYNAME::", airports.get(0).getName() )
//                               () -> assertEquals( 42, airports.get(0).getAirportCount() )
//                           () -> verify( mapper ).entityToDomainAirportsInRegion( anyList() )
                             );


                }
            }

        }

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
                List<AirportCountInContinentEntity> entities =
                        List.of( new AirportCountInContinentEntity( "YY", "::YYNAME::", 42L ),
                                 new AirportCountInContinentEntity( "ZZ", "::ZZNAME::", 21L )
                               );

                when( repository.countAirportsByContinent() )
                        .thenReturn( entities );

                // --- when
                final List<AirportCountInContinent>
                        result = service.countAirportsByContinent();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( 2, result.size() ),
                           () -> assertEquals( "YY", result.get(0).getContinentCode() ),
                           () -> assertEquals( "::YYNAME::", result.get(0).getName() ),
                           () -> assertEquals( 42, result.get(0).getAirportCount() )
                         );
            }


            // --- airports in a continent grouped by country

            // --- airports in a specific country grouped by region
            @Nested
            @DisplayName( "by Country")
            class ByCountry
            {
                @DisplayName( "by country" )
                @Test
                void airportCounts_byCountry_returnsList()
                {
                    // --- given
                    List<AirportCountInCountryEntity> entities =
                            List.of( new AirportCountInCountryEntity( "YY", "::YYNAME::", 42L ),
                                     new AirportCountInCountryEntity( "ZZ", "::ZZNAME::", 21L )
                                   );

                    when( repository.countAirportsByCountry( anyString() ) )
                            .thenReturn( entities );

                    // --- when
                    final List<AirportCountInCountry>
                            result = service.countAirportsByCountry( anyString() );

                    // --- then
                    assertAll( () -> assertThat( result )
                                       .isNotNull()
                                       .hasSize( 2 ),
                               () -> assertEquals( "YY", result.get( 0 ).getCountryCode() ),
                               () -> assertEquals( "::YYNAME::", result.get( 0 ).getName() ),
                               () -> assertEquals( 42, result.get( 0 ).getAirportCount() )
                             );
                }

                @DisplayName( "by country / region" )
                @Test
                void airportCounts_byCountryRegion_returnsList()
                {
                    // --- given
                    List<AirportCountInRegionEntity> entities =
                            List.of( new AirportCountInRegionEntity( "YY", "::YYNAME::", 42L ),
                                     new AirportCountInRegionEntity( "ZZ", "::ZZNAME::", 21L )
                                   );

                    when( repository.countRegionAirportsByCountry( anyString() ) )
                            .thenReturn( entities );

                    // --- when
                    final List<AirportCountInRegion>
                            result = service.countRegionAirportsByCountry( anyString() );

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
                List<AirportCountInRegionEntity>entities =
                        List.of( new AirportCountInRegionEntity( "YY", "::YYNAME::", 42L ),
                                 new AirportCountInRegionEntity( "ZZ", "::ZZNAME::", 21L )
                               );

                when( repository.countAirportsByRegion( anyString() ) )
                        .thenReturn( entities );

                final List<AirportCountInRegion>
                        result = service.countAirportsByRegion( anyString());

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull()
                                   .hasSize( 2 ),
                           () -> assertEquals( "YY", result.get(0).getRegionCode() ),
                           () -> assertEquals( "::YYNAME::", result.get(0).getName() ),
                           () -> assertEquals( 42, result.get(0).getAirportCount() )
//                           () -> verify( mapper ).entityToDomainAirportsInRegion( anyList() )
                         );
            }

        }

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
                        result = service.findSummaryByContinent( "NA" );

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
                        result = service.findSummaryByCountry( "PH" );

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
                        result = service.findSummaryByRegion( "US-GA" );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull()
                         );
            }

        }


        @Nested
        @DisplayName( "Query" )
        class Queru           // NOPMD
        {
            private ArgumentCaptor<Pageable> pageableCaptor;
            private  Pageable pageable;


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
                List<AirportEntity> entities =
                        List.of(
                                buildEntity(),
                                buildEntity(),
                                buildEntity()
                               );
                Page<AirportEntity> page = new PageImpl<>( entities );
                when( repository.advancedQuery( anyString(),    // iataCode
                                                anyString(),    // icaoCode
                                                anyString(),    // ident
                                                anyString(),    // name
                                                any( Pageable.class ) ) )
                        .thenReturn( page );



                // --- when
                final Page<Airport>
                        result = service.advancedQuery( null,
                                                        null,
                                                        null,
                                                        null,
                                                        pageable );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull(),
                           () -> PageableAssert
                                   .assertThat( pageable )
                                   .hasPageNumber( 1 )
                                   .hasPageSize( 10 ),
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
                List<AirportEntity> entities =
                        List.of(
                                buildEntity(),
                                buildEntity(),
                                buildEntity()
                               );
                Page<AirportEntity> page = new PageImpl<>( entities );
                when( repository.advancedQuery( anyString(),    // iataCode
                                                anyString(),    // icaoCode
                                                anyString(),    // ident
                                                anyString(),    // name
                                                any( Pageable.class ) ) )
                        .thenReturn( page );



                // --- when
                // lowercase params should be made upper before calling repository
                final Page<Airport>
                        result = service.advancedQuery( "iata",
                                                        "icao",
                                                        "ident",
                                                        "name",
                                                        pageable );

                // --- then
                assertAll( () -> assertThat( result )
                                   .isNotNull(),
                           () -> PageableAssert
                                   .assertThat( pageable )
                                   .hasPageNumber( 1 )
                                   .hasPageSize( 10 ),
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

//    }


    // ========== Update ==========
    @Nested
    @DisplayName( "Update" )
    class Update           // NOPMD
    {
    }


    // ========== Delete ==========
    @Nested
    @DisplayName( "Delete" )
    class Delete           // NOPMD
    {
    }

}
