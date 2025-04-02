/* (C) 2025 */

package com.example.airline.location.persistence.model.location;


import java.net.URI;

import com.example.airline.location.persistence.model.UriConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



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
    @Column( name = "id" )
    @NotNull @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;

    @Column( name = "code" )
    @NotNull private String code;

    @Column( name = "name" )
    @NotNull private String name;

    @Column( name = "wikipedia_link" )
    @Convert( converter = UriConverter.class )
    private URI wikiLink;

    @Column( name = "keywords" )
    private String keywords; // May not need to exchange this
}
