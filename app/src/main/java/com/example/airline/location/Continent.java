/* (C)2025 */

package com.example.airline.location;


import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;

// TODO change wikiLink to a URI
// TODO change code to a 2 character code...



@Data
@AllArgsConstructor
public class Continent
{
    private Integer id;
    private String  code;
    private String  name;
    private URI     wikiLink;
    private String  keywords; // May not need to exchange this
}
