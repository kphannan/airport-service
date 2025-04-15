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
 * Persistence object representing a single Continent.
 */
@Entity
@Table( name = "continents" )
@Data
@NoArgsConstructor // required by JPA
@AllArgsConstructor
public class ContinentEntity
{
    @Id
    @Column( name = "id", nullable = false )
    @SuppressWarnings( "PMD.ShortVariable" )
    @NonNull private Integer id;

    @Column( name = "code", length = 2, nullable = false, columnDefinition = "char(2)" )
    @NonNull private String code;

    @Column( name = "name", length = 52, nullable = false )
    @NonNull private String name;

    @Column( name = "wikipedia_link", length = 255 )
    @Convert( converter = UriConverter.class )
    @Nullable private URI wikiLink;

    @Column( name = "keywords", length = 255 )
    @Nullable private String keywords; // May not need to exchange this
}
