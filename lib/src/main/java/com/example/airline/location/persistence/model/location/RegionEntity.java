/* (C) 2025 */

package com.example.airline.location.persistence.model.location;


import java.net.URI;

import com.example.airline.location.persistence.model.UriConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


/**
 * Each row represents a high-level administrative subdivision of a country. The
 * iso_region column in airports.csv links to the code column in this dataset.
 */
@Entity
@Table( name = "regions" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionEntity
{
    /**
     * Internal OurAirports integer identifier for the region. This will stay
     * persistent, even if the region code changes.
     */
    @Id
    // @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "id", nullable = false )
    @SuppressWarnings( "PMD.ShortVariable" )
    @NonNull private Integer id;

    /**
     * local_code prefixed with the country code to make a globally-unique
     * identifier.
     */
    @Column( name = "code", length = 7, nullable = false )
    @NonNull private String code;

    /**
     * The local code for the administrative subdivision. Whenever possible, these
     * are official ISO 3166:2, at the highest level available, but in some cases
     * OurAirports has to use unofficial codes. There is also a pseudo code "U-A"
     * for each country, which means that the airport has not yet been assigned to a
     * region (or perhaps can't be, as in the case of a deep-sea oil platform).
     */
    @Column( name = "local_code", length = 4, nullable = false )
    @NonNull private String localCode;

    /**
     * The common English-language name for the administrative subdivision. In some
     * cases, the name in local languages will appear in the keywords field assist
     * search.
     */
    @Column( name = "name", length = 52, nullable = false )
    @NonNull private String name;

    /**
     * The two-character ISO 3166:1-alpha2 code for the country containing the
     * administrative subdivision. A handful of unofficial, non-ISO codes are also
     * in use, such as "XK" for Kosovo.
     */
    @Column( name = "iso_country", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String country; // ! Create domain object for the country code

    /**
     * A code for the continent to which the region belongs. See the continent field
     * in airports.csv for a list of codes.
     */
    @Column( name = "continent", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String continent; // ! Create domain object for contient code

    /**
     * A link to the Wikipedia article describing the subdivision.
     */
    @Column( name = "wikipedia_link", length = 255 )
    @Convert( converter = UriConverter.class )
    @Nullable private URI wikipediaLink;

    /**
     * A comma-separated list of keywords to assist with search. May include former
     * names for the region, and/or the region name in other languages.
     */
    @Column( name = "keywords", length = 255 )
    @Nullable private String keywords;
}
