/* (C) 2025 */

package com.example.airline.location.persistence.model;


import java.net.URI;
import java.net.URISyntaxException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.log4j.Log4j2;



/**
 * Map a URI to a form suitable for storing in a database or in JSON.
 */
@Converter
@Log4j2
public class UriConverter implements AttributeConverter<URI, String>
{
    @Override
    public String convertToDatabaseColumn( final URI uri )
    {
        return null == uri ? null : uri.toString();
    }



    @Override
    public URI convertToEntityAttribute( final String dbData )
    {
        try
        {
            return null == dbData ? null : new URI( dbData );
        }
        catch ( URISyntaxException e )
        {
            log.error( () -> String.format( "Error converting '%s' to a URI", dbData ), e );
        }

        return null;
    }
}
