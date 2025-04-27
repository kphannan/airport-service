/* (C) 2025 */

package com.example.airline.location.airport.persistence.repository;


import java.util.Optional;

import com.example.airline.location.airport.persistence.model.AirportCodeIcaoEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**
 * Definition of the operations on the ICAO reference table.
 */
@Repository
public interface AirportCodeIcaoRepository extends PagingAndSortingRepository<AirportCodeIcaoEntity, String>
{

    /**
     * Lookup a ICAO airport reference code by its code.
     *
     * @param icaoCode the code to locate.
     *
     * @return the record if found.
     */
    @Query( "from #{#entityName} a where a.icaoCode = :icaoCode" )
    Optional<AirportCodeIcaoEntity> findById( @Param( "icaoCode" ) String icaoCode );

    /**
     * Lookup ICAO airport code by a partial, full id. If no ID is specified, the
     * complete list is returned page by page.
     *
     * @param paging the {@code Page} criteria.
     *
     * @return the located page, whose body contains the found records.
     */
    @Override
    @Query( "FROM #{#entityName} a WHERE a.icaoCode IS NOT NULL"
            + " AND LENGTH(a.icaoCode) = 4 AND SUBSTRING(a.icaoCode, 1,1 ) >= 'A'"
            + " AND SUBSTRING(a.icaoCode, 2,1 ) >= 'A'" + " AND SUBSTRING(a.icaoCode, 3,1 ) >= 'A'"
            + " AND SUBSTRING(a.icaoCode, 4,1 ) >= 'A'" )
    @NonNull Page<AirportCodeIcaoEntity> findAll( @NonNull Pageable paging );
}
