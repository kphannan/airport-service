/* (C)2025 */

package com.example.airline.location;


import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;

// TODO change wikiLink to a URI
// TODO change code to a 2 character code...



@Data
@AllArgsConstructor
public class Country
{
    // @Id
    // @GeneratedValue( strategy = GenerationType.AUTO )
    // private final Long id;

    // @NotNull @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;

    // @NotNull
    private String code;

    // @NotNull
    private String name;

    // @NotNull
    private String continent;

    // @Convert( converter = UriConverter.class )
    private URI wikipediaLink;

    private String keywords;
}
