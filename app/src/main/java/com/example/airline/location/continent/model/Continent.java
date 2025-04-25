/* (C) 2025 */

package com.example.airline.location.continent.model;


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
public class Continent //extends NewContinent
{
    @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;
    @NonNull
    private String  code;     // TODO change code to a 2 character code...
    @NonNull
    private String  name;
    @Nullable
    private URI     wikiLink;
    @Nullable
    private String  keywords; // May not need to exchange this


//    public Continent( final NewContinentDTO newContinent )
//    {
//        code = newContinent.getCode();
//        name = newContinent.getName();
//        wikiLink = newContinent.getWikiLink();
//        keywords = newContinent.getKeywords();
//    }

}
