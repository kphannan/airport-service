package com.example.airline.location.airport.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import com.example.airline.location.airport.api.AirportController;
import com.example.airline.location.airport.mapper.AirportDtoMapper;
import com.example.airline.location.airport.mapper.AirportEntityMapper;
import com.example.airline.location.airport.model.AirportCountInContinent;
import com.example.airline.location.airport.model.AirportCountInCountry;
import com.example.airline.location.airport.model.AirportCountInRegion;
import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInCountryEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInRegionEntity;
import com.example.airline.location.airport.persistence.model.AirportSummaryEntity;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import com.example.airline.location.continent.api.ContinentController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@ExtendWith( SpringExtension.class )
//@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@WebMvcTest( controllers = AirportController.class )
@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Airport: Service" )
class AirportServiceTest
{
    @MockitoBean
    private AirportRepository repository;



    @Autowired
    private AirportService service;


    @Nested
    @DisplayName( "Create" )
    class Create           // NOPMD
    {
    }

    @Nested
    @DisplayName( "Read" )
    class Read           // NOPMD
    {
        @Nested
        @DisplayName( "Counts" )
        class Counts           // NOPMD
        {
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

    }

    @Nested
    @DisplayName( "Update" )
    class Update           // NOPMD
    {
    }

    @Nested
    @DisplayName( "Delete" )
    class Delete           // NOPMD
    {
    }

}
