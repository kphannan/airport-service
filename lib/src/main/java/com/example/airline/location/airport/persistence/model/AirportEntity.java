/* (C) 2025 */

package com.example.airline.location.airport.persistence.model;


import java.math.BigDecimal;
import java.net.URI;

import com.example.airline.location.persistence.model.UriConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity;


/*
SELECT continent, COUNT(ident) from Airports GROUP BY continent;

SELECT iso_country, COUNT(ident) from Airports GROUP BY iso_country;

SELECT iso_region, COUNT(ident) from Airports GROUP BY iso_region;

select ae1_0.continent,count(*) from airports ae1_0 group by ae1_0.continent;

-- Count of airports by continent
select a.continent,
           count(a.id)
from airports a
INNER JOIN continents c on c.code = a.continent
group by a.continent;

-- Count of airports by continent with continent (code, name)
SELECT a.continent,
               c.name,
               count( a.id )
FROM airports a
INNER JOIN continents c on c.code = a.continent
GROUP BY a.continent;

-- Count of airports by country
SELECT c.code,
               c.name,
               count( a.id )
FROM airports a
INNER JOIN countries c on c.code = a.iso_country
--WHERE a.iso_country = 'US'
GROUP BY a.iso_country
;

-- List of airport (ident, name) in a country
SELECT a.ident,
               a.name
--               count( a.id )
FROM airports a
INNER JOIN countries c on c.code = a.iso_country
WHERE a.iso_country = 'US'
--GROUP BY a.iso_country
;


-- Number of airports in a specified region
SELECT r.code,
               r.name,
               count( a.id )
FROM airports a
INNER JOIN regions r on r.code = a.iso_region
WHERE r.code = 'US-GA'
GROUP BY a.iso_region
;

-- List of airports in a specified region
SELECT a.ident,
               a.name
--               count( a.id )
FROM airports a
INNER JOIN regions r on r.code = a.iso_region
WHERE r.code = 'US-GA'
--GROUP BY a.iso_region
;
*/


/**
 * Entity definition for the {@code Airport} table.
 */
@Entity
@Table( name = "AIRPORTS" )
@Data
@EqualsAndHashCode( callSuper = false )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners( AuditingEntityListener.class )
@SuppressWarnings( "PMD.TooManyFields" )
@NamedQuery( name="AirportEntity.countAirportsByContinent",
             query="""
                    SELECT new com.example.airline.location.airport.persistence.model.AirportCountInContinentEntity(
                        a.continent AS continentCode,
                        c.name AS name,
                        COUNT(a.id) AS airportCount
                        )
                      FROM AirportEntity a
                    INNER JOIN ContinentEntity c ON c.code = a.continent
                    GROUP BY a.continent
                    """
)
@NamedQuery( name="AirportEntity.countAirportsByCountry",
             query="""
                    SELECT new com.example.airline.location.airport.persistence.model.AirportCountInCountryEntity(
                           c.code AS countryCode,
                           c.name AS name,
                           COUNT(a.id) AS airportCount
                           )
                      FROM AirportEntity a
                    INNER JOIN CountryEntity c ON c.code = a.isoCountry
                    GROUP BY a.isoCountry
                    """
)
@NamedQuery( name="AirportEntity.countAirportsByRegion",
             query="""
                SELECT a.isoRegion AS regionCode,
                        r.name AS name,
                       COUNT(a.id) AS airportCount
                  FROM AirportEntity a
                INNER JOIN RegionEntity r ON r.code = a.isoRegion
                GROUP BY a.isoRegion
                """
)
public class AirportEntity // extends Auditable<String>
{
    /**
     * T Internal OurAirports integer identifier for the airport. This will stay
     * persistent, even if the airport code changes.
     */
    @Id
    // @GeneratedValue( strategy = GenerationType.AUTO )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "seq_airport_id" )
    @SequenceGenerator( name = "seq_airport_id",
                        sequenceName = "seq_airport_id",
                        allocationSize = 1,
                        initialValue = 596_214 )
    // @GeneratedValue( strategy = GenerationType.IDENTITY )
    @SuppressWarnings( "PMD.ShortVariable" )
    @Column( name = "id", nullable = false )
    @NonNull private Long id;

    /**
     * The text identifier used in the OurAirports URL. This will be the ICAO code
     * if available. Otherwise, it will be a local airport code (if no conflict), or
     * if nothing else is available, an internally-generated code starting with the
     * ISO2 country code, followed by a dash and a four-digit number.
     */
    @Column( name = "ident", length = 7, nullable = false )
    @NonNull private String ident;

    /**
     * The type of the airport. Allowed values are "closed_airport", "heliport",
     * "large_airport", "medium_airport", "seaplane_base", and "small_airport". See
     * the map legend for a definition of each type.
     */
    @Column( name = "type", length = 14, nullable = false )
    @NonNull private String type;

    /**
     * The official airport name, including "Airport", "Airstrip", etc.
     */
    @Column( name = "name", length = 90, nullable = false )
    @NonNull private String name;

    /**
     * The airport latitude in decimal degrees (positive for north).
     */
    @Column( name = "latitude_deg", precision = 20, scale = 14, nullable = false )
    @NonNull private BigDecimal latitude;
    /**
     * The airport longitude in decimal degrees (positive for east).
     */
    @Column( name = "longitude_deg", precision = 20, scale = 14, nullable = false )
    @NonNull private BigDecimal longitude;

    /**
     * The airport elevation MSL in feet (not metres).
     */
    @Column( name = "elevation_ft" )
    @Nullable private Integer elevation;

    /**
     * The code for the continent where the airport is (primarily) located. Allowed
     * values are "AF" (Africa), "AN" (Antarctica), "AS" (Asia), "EU" (Europe), "NA"
     * (North America), "OC" (Oceania), or "SA" (South America).
     */
    @Column( name = "continent", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String continent;

    /**
     * The two-character ISO 3166:1-alpha2 code for the country where the airport is
     * (primarily) located. A handful of unofficial, non-ISO codes are also in use,
     * such as "XK" for Kosovo. Points to the code column in countries.csv.
     */
    @Column( name = "iso_country", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String isoCountry;

    /**
     * 'An alphanumeric code for the high-level administrative subdivision of a
     * country where the airport is primarily located (e.g. province, governorate),
     * prefixed by the ISO2 country code and a hyphen. OurAirports uses ISO 3166:2
     * codes whenever possible, preferring higher administrative levels, but also
     * includes some custom codes. See the documentation for regions.csv.'
     */
    @Column( name = "iso_region", length = 7, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String isoRegion;

    /**
     * The primary municipality that the airport serves (when available). Note that
     * this is not necessarily the municipality where the airport is physically
     * located.
     */
    @Column( name = "municipality", length = 128 )
    @Nullable private String municipality;

    /**
     * "yes" if the airport currently has scheduled airline service; "no" otherwise.
     */
    @Column( name = "scheduled_service", length = 3, nullable = false )
    @NonNull private String scheduledService; // boolean...

    /**
     * The code that an aviation GPS database (such as Jeppesen's or Garmin's) would
     * normally use for the airport. This will always be the ICAO code if one
     * exists. Note that, unlike the ident column, this is not guaranteed to be
     * globally unique.
     */
    @Column( name = "gps_code", length = 4, columnDefinition = "char(4)" )
    @Nullable private String gpsCode;

    /**
     * The three-letter ICAO code for the airport (if it has one).
     */
    @Column( name = "icao_code", length = 4, columnDefinition = "char(4)" )
    @Nullable private String icaoCode;

    /**
     * The three-letter IATA code for the airport (if it has one).
     */
    @Column( name = "iata_code", length = 3, columnDefinition = "char(3)" )
    @Nullable private String iataCode;

    /**
     * The local country code for the airport, if different from the gps_code and
     * iata_code fields (used mainly for US airports).
     */
    @Column( name = "local_code", length = 7 )
    @Nullable private String localCode;

    /**
     * URL of the airport''s official home page on the web, if one exists.
     */
    @Convert( converter = UriConverter.class )
    @Column( name = "home_link", length = 255 )
    @Nullable private URI homeLink; // URI

    /**
     * URL of the airport''s page on Wikipedia, if one exists.
     */
    @Convert( converter = UriConverter.class )
    @Column( name = "wikipedia_link", length = 255 )
    @Nullable private URI wikipediaLink; // URI

    /**
     * Extra keywords/phrases to assist with search, comma-separated. May include
     * former names for the airport, alternate codes, names in other languages,
     * nearby tourist destinations, etc.
     */
    @Column( name = "keywords", length = 255 )
    @Nullable private String keywords;

    // ----- Auditable -----
    // @CreatedBy
    // User creator;

    // @CreatedDate
    // Date createdAt;

    // @LastModifiedDate
    // Date modifiedAt;

    // @LastModifiedBy
    // User modifier;
}
