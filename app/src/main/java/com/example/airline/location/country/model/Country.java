/* (C) 2025 */

package com.example.airline.location.country.model;


import java.net.URI;

import org.jspecify.annotations.NonNull;


/**
 * Domain model object representing a single Country.
 */
public record Country(
    @NonNull
    @SuppressWarnings( "PMD.ShortVariable" )
    Integer id,

    @NonNull
    String code, // TODO change code to a 2 character code...

    @NonNull
    String name,

    @NonNull
    String continent,

    // @Convert( converter = UriConverter.class )
    URI wikipediaLink,

    String keywords
)
{}

