
package com.example.airline.location;

import lombok.Data;


// TODO convert to a Java record
@Data
public class ContinentDTO
{
    private Long id;
    private String code;
    private String name;
    private String wikiLink;
    private String keywords;        // May not need to exchange this
}

