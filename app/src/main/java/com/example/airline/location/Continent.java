/* (C) 2025 */

package com.example.airline.location;


import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


/**
 * Domain model object representing a single Continent.
 */
@Data
@AllArgsConstructor
public class Continent
{
    @SuppressWarnings( "PMD.ShortVariable" )
    @NonNull private Integer   id;
    @NonNull private String    code;     // TODO change code to a 2 character code...
    @NonNull private String    name;
    @Nullable private URI      wikiLink;
    @Nullable private String   keywords; // May not need to exchange this
}
