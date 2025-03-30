package com.example.airline.location.persistence.model.location;

import java.net.URI;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.example.airline.location.persistence.model.UriConverter;
import lombok.Data;

/**
 * Definition of the Country reference table.
 */
@Entity
@Table( name = "countries" )
@Data
public class Country
{
    // @Id
    // @GeneratedValue( strategy = GenerationType.AUTO )
    // private final Long      id;

    @Id
    @Column( name = "id" )
    @NotNull
    @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;

    @Column( name = "code" )
    @NotNull
    private String code;

    @Column( name = "name" )
    @NotNull
    private String name;

    @Column( name = "continent" )
    @NotNull
    private String continent;

    @Column( name = "wikipedia_link" )
    @Convert( converter = UriConverter.class )
    private URI  wikipediaLink;

    @Column( name = "keywords" )
    private String keywords;

}

