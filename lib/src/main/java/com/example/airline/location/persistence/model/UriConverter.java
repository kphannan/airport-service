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
        // StringBuilder sb = new StringBuilder();
        // sb.append(color.getRed()).append(SEPARATOR)
        // .append(color.getGreen())
        // .append(SEPARATOR)
        // .append(color.getBlue())
        // .append(SEPARATOR)
        // .append(color.getAlpha());
        return uri.toString();
    }

    @Override
    public URI convertToEntityAttribute( final String dbData )
    {
        try
        {
            return new URI( dbData );
        }
        catch ( URISyntaxException e )
        {
            log.error( () -> String.format( "Error converting '%s' to a URI", dbData ), e );
        }

        return null;
    }
}
