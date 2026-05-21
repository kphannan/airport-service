/* (C) 2025 */

package com.example.airline.location.airport.persistence.repository;


import java.util.List;
import java.util.Optional;

import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInCountryEntity;
import com.example.airline.location.airport.persistence.model.AirportCountInRegionEntity;
import com.example.airline.location.airport.persistence.model.AirportEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * Paged repository of {@code Airport}s.
 */
@Repository
public interface AirportRepository extends PagingAndSortingRepository<AirportEntity, Long>
{

    // ========== CREATE ==========

    // ========== READ ==========
    // ----- Test for existence -----

    /**
     * Determine if an element is stored persistently.
     *
     * @param airportId the persistence key of the airport.
     * @return {@code true} if the code corresponds to a {@see AirportEntity},
     *         {@code false} if the code does not correspond to a known {@see AirportEntity}.
     */
    boolean existsById( Long airportId );

    /**
     * Determine if an {@see AirportEntity} element is stored persistently.
     *
     * @param airportCode the unique code (predominantly ICAO) of the airport.
     * @return {@code true} if the code corresponds to a {@see Airport},
     *         {@code false} if the code does not correspond to a known {@see Airport}.
     */
    boolean existsByIdent( String airportCode );

    // ----- Single Entity -----
    /**
     * Lookup a {@code Airport} by its unique identifier.
     *
     * @param id the unique identifier.
     *
     * @return the record if found.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    Optional<AirportEntity> findById( Long id );

    /**
     * Find an airport from its commonly used identifier, often this is the iata
     * airport code.
     *
     * @param ident the airport identifier.
     *
     * @return the DB entry for the target airport.
     */
    Optional<AirportEntity> findByIdent( String ident );

    // ----- List of entities -----
    /**
     * Retrieve all {@code Airport} records by {@code Page}.
     *
     * @param paging the {@code Page} criteria.
     *
     * @return the located page, whose body contains the found records.
     */
    @Override
    @NonNull
    Page<AirportEntity> findAll( @NonNull Pageable paging );


    /**
     * Find an airport from its commonly used identifier, often this is the iata
     * airport code.
     *
     * @param regionCode a geographic region of interest.
     *
     * @return list of airports in the region, empty list if none ore found.
     */
    List<AirportEntity> findByIsoRegion( String regionCode );

    /**
     * Search for {@code Airport} records that contain any of the query parameters.
     *
     * @param iataCode optional IATA code to search on.
     * @param icaoCode optional ICAO code to search on.
     * @param ident    optional identifier which may be the ICAO code to search on.
     * @param name     optional airport name string.
     * @param paging   current {@code Page} specification.
     *
     * @return the target page with {@code Airport} records if any match the
     *         criteria.
     */
    @Query( "from #{#entityName} airport" + " where (:iataCode='' or airport.iataCode like %:iataCode%)"
            + "   and (:icaoCode='' or airport.gpsCode like %:icaoCode%)"
            + "   and (:ident='' or airport.ident like %:ident%)"
            + "   and (:name='' or upper(airport.name) like %:name%)" )
    Page<AirportEntity> advancedQuery( @Param( "iataCode" ) String iataCode,
                                       @Param( "icaoCode" ) String icaoCode,
                                       @Param( "ident" ) String ident,
                                       @Param( "name" ) String name,
                                       Pageable paging );

    /**
     * Find the number of airports contained within each continent.
     *
     * @return a collection of continents with the number of airports in the continent.
     */
    List<AirportCountInContinentEntity> countAirportsByContinent();

    /**
     * Find the count of airports in all the {@see Region}s of a specific {@see Country}.
     *
     * @param continentCode the de facto continent code of the target continent.
     * @return a collection of {@see AirporCountInCountryEntity} instances where each one represents
     *         the number of airports in a single {@see Country}.
     */
    List<AirportCountInCountryEntity> countCountryAirportsByContinent( String continentCode );

    List<AirportCountInRegionEntity> countRegionAirportsByContinentAndIsoCountry( String continentCode, String countryCode );

    AirportCountInRegionEntity countRegionAirportsByContinentAndIsoCountryAndIsoRegion( String continentCode, String countryCode, String regionCode );

    /**
     * Find the count of airports in all the {@see Region}s of a specific {@see Country}.
     *
     * @param countryCode the ISO 3166 code of the target country.
     *
     * @return a collection of {@see AirporCountInReqionEntity} instances where each one represents the number of
     *     airports in a single {@see Region}.
     */
    List<AirportCountInRegionEntity> countRegionAirportsByCountry( String countryCode );

    /**
     * Find the list of the number airports in a specific Region.
     *
     * @param regionCode the target region.
     * @return
     */
    List<AirportCountInRegionEntity> countAirportsByRegion( String regionCode );

    List<AirportCountInCountryEntity> countAirportsByCountry( String countryCode );

    List<AirportCountInCountryEntity> countAirportsGroupedByCountry();

    // List<AirportSummaryEntity> findSummaryByContinent( String continentCode );

    // List<AirportSummaryEntity> findSummaryByCountry( String isoCountry );

    // List<AirportSummaryEntity> findSummaryByRegion( String isoRegion );

    // ========== UPDATE ==========

    // ========== DELETE ==========
    /**
     * Delete a specific Continent row.
     *
     * @param entity must not be {@literal null}.
     */
    void delete( AirportEntity entity );

    /**
     * Delete an Airport row by its primary key.
     *
     * @param airportId must not be {@literal null}.
     */
    void deleteById( Long airportId );


    // Defined as NamedQueries
}
