/* (C) 2025 */

package com.example.airline.location.persistence.model;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class UriConverterTest
{
    private UriConverter classUnderTest;

    @BeforeEach
    void setUp()
    {
        classUnderTest = new UriConverter();
    }



    // ----- convertToDatabaseColumn
    @Test
    void toDB_null_returnsNull()
    {
        String result = classUnderTest.convertToDatabaseColumn( null );

        assertNull( result );
    }



    @Test
    void toDB_validUri_returnsString() throws Exception
    {
        final URI    uri    = new URI( "http://foo.bar/baz" );
        final String result = classUnderTest.convertToDatabaseColumn( uri );

        assertEquals( "http://foo.bar/baz", result );
    }



    // ----- convertToEntityAttribute
    @Test
    void toDB_nullUriString_returnsNull()
    {
        assertNull( classUnderTest.convertToEntityAttribute( null ) );
    }



    @Test
    void toDB_validUriString_returnsCorrectURI()
    {
        final URI uri = classUnderTest.convertToEntityAttribute( "https://some.domain/pathh" );

        assertEquals( URI.create( "https://some.domain/pathh" ), uri );
    }



    @Test
    void toDB_invalidURI_returnsNull()
    {
        final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                               () -> classUnderTest.convertToEntityAttribute( " this is bad " ) );

        assertAll( () -> assertEquals( "Illegal character in path at index 0:  this is bad ", thrown.getMessage() ) );
    }
}
