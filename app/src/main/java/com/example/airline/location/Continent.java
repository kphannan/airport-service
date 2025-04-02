/* (C) 2025 */

package com.example.airline.location;


import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class Continent
{
    @SuppressWarnings( "PMD.ShortVariable" )
    private Integer id;
    private String  code;     // TODO change code to a 2 character code...
    private String  name;
    private URI     wikiLink;
    private String  keywords; // May not need to exchange this
}
