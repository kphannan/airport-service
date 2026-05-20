/* (C) 2025 */

package com.example.airline.location.continent.model;


import java.net.URI;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


/**
 * Domain model object representing a single Continent.
 *
 * @param id       persistence key of the continent.
 * @param code     de facto abbreviation for the continent.
 * @param name     common name of the continent.
 * @param wikiLink Optional, URI to the Wikipedia page for the Continent
 * @param keywords Optional comma-separated list of keywords for helping with search. May include former names for the
 *                 continent, and/or the continent name in other languages.
 */
public record Continent(
    @NonNull
    Integer id,

    @Pattern( regexp = "[A-Z]{2}", message = "Code must be 2 uppercase characters" )
    @NonNull
    String code,     // TODO change code to a 2 character code...

    @Pattern( regexp = "[a-zA-Z][a-zA-Z ]{1,51}", message = "Continent name must be 2 to 52 characters" )
    @NonNull
    String name,

    @Nullable
    URI wikiLink,

    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
    @Nullable
    String keywords // May not need to exchange this
)
{
}
