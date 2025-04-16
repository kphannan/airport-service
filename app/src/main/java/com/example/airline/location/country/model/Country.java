/* (C) 2025 */

package com.example.airline.location.country.model;


import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;



/**
 * Domain model object representing a single Country.
 */
@Data
@AllArgsConstructor
public class Country
{
    // @Id
    // @GeneratedValue( strategy = GenerationType.AUTO )
    // private final Long id;

    // @NotNull
    @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;

    // @NotNull
    private String code; // TODO change code to a 2 character code...

    // @NotNull
    private String name;

    // @NotNull
    private String continent;

    // @Convert( converter = UriConverter.class )
    private URI wikipediaLink;

    private String keywords;
}
