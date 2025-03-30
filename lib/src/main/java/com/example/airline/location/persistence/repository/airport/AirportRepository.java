package com.example.airline.location.persistence.repository.airport;

import java.util.Optional;

import com.example.airline.location.persistence.model.airport.Airport;
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
public interface AirportRepository extends PagingAndSortingRepository<Airport, Long>
{
    /**
     * Lookup a {@code Airport} by its unique identifier.
     *
     * @param id the unique identifier.
     * @return the record if found.
     */
    @Query( "from #{#entityName} airport where airport.id = :id" )
    @SuppressWarnings( "PMD.ShortVariable" )
    Optional<Airport> findById( @Param( "id" ) final Long id );

    /**
     * Retrieve all {@code Airport} records by {@code Page}.
     *
     * @param paging the {@code Page} criteria.
     * @return the located page, whose body contains the found records.
     */
    @Override
    Page<Airport> findAll( Pageable paging );

    /**
     * Search for {@code Airport} records that contain any of the query parameters.
     *
     * @param iataCode optional IATA code to search on.
     * @param icaoCode optional ICAO code to search on.
     * @param ident optional identifier which may be the ICAO code to search on.
     * @param name optional airport name string.
     * @param paging current {@code Page} specification.
     * @return the target page with {@code Airport} records if any match the criteria.
     */
    @Query( "from #{#entityName} airport"
            + " where (:iataCode='' or airport.iataCode like %:iataCode%)"
            + "   and (:icaoCode='' or airport.gpsCode like %:icaoCode%)"
            + "   and (:ident='' or airport.ident like %:ident%)"
            + "   and (:name='' or upper(airport.name) like %:name%)"
    )
    Page<Airport> advancedQuery(  @Param( "iataCode" ) final String iataCode
                                , @Param( "icaoCode" ) final String icaoCode   // gpsCode
                                , @Param( "ident" ) final String ident
                                , @Param( "name" ) final String name
                                , final Pageable paging );
}

