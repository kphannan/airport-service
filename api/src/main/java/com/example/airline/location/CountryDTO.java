/* (C)2025 */

package com.example.airline.location;


import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;

// TODO change wikiLink to a URI
// TODO change code to a 2 character code...
// record CountryDTO( Long id, String code, String name, String wikiLink,
// String keywords )
// {}



// TODO convert to a Java record
@Data
@AllArgsConstructor
public class CountryDTO
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

    private URI wikipediaLink;

    private String keywords;
}
