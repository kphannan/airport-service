/* (C) 2025 */

package com.example.airline.location.persistence.model.location;


import java.net.URI;

import com.example.airline.location.persistence.model.UriConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;



/**
 * Definition of the Country reference table.
 */
@Entity
@Table( name = "countries" )
@Data
@NoArgsConstructor
public class CountryEntity
{
    // @Id
    // @GeneratedValue( strategy = GenerationType.AUTO )
    // private final Long id;

    @Id
    @Column( name = "id", nullable = false )
    @SuppressWarnings( "PMD.ShortVariable" )
    @NonNull private Integer id;

    @Column( name = "code", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String code;

    @Column( name = "name", length = 52, nullable = false )
    @NonNull private String name;

    @Column( name = "continent", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String continent;

    @Column( name = "wikipedia_link", length = 255 )
    @Convert( converter = UriConverter.class )
    @Nullable private URI wikipediaLink;

    @Column( name = "keywords", length = 255 )
    @Nullable private String keywords;

}
