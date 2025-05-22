//package com.example.airline.location.airport.service;
//
//
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import com.example.airline.location.airport.mapper.AirportDtoMapper;
//import com.example.airline.location.airport.mapper.AirportEntityMapper;
//import com.example.airline.location.airport.model.AirportCountInContinent;
//import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
//import com.example.airline.location.airport.persistence.repository.AirportRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mapstruct.factory.Mappers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ExtendWith( SpringExtension.class )
//@ComponentScan( basePackages = { "com.example.airline.location.airport" } )
//class AirportServiceTest
//{
//    @Mock
//    private AirportRepository repository;
//
////    @Autowired
//    private AirportEntityMapper mapper = Mappers.getMapper( AirportEntityMapper.class );
////    @MockitoBean
////    private AirportDtoMapper    dtoMapper;
//
//
//    @InjectMocks
//    private AirportService service;
//
//
//
//
//    @Test
//    void airportCounts_byContinent_returnsList()
//    {
//        List<AirportCountInContinentEntity> entities =
//                List.of( new AirportCountInContinentEntity( "YY", "::YYNAME::", 42L ),
//                         new AirportCountInContinentEntity( "ZZ", "::ZZNAME::", 21L )
//                       );
//
//        when( repository.countAirportsByContinent() )
//                .thenReturn( entities );
//
////        List<AirportCountInContinent> domainInstances =
////                List.of( new AirportCountInContinent( "YY", "::YYNAME::", 42L ),
////                         new AirportCountInContinent( "ZZ", "::ZZNAME::", 21L )
////                       );
////        when( service.countAirportsByContinent() )
////                .thenReturn( domainInstances );
////
//
//        final List<AirportCountInContinent>
//                result = service.countAirportsByContinent();
//
//        assertAll( () -> assertNotNull( result ),
//                   () -> assertEquals( 2, result.size() ),
//                   () -> assertEquals( "YYZ", result.get(0).getContinentCode() ),
//                   () -> assertEquals( "::YYNAME:::", result.get(0).getName() ),
//                   () -> assertEquals( 4242, result.get(0).getAirportCount() )
//                 );
//        ;
//
//    }
//}
