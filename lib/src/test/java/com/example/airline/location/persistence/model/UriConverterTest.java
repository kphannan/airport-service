/* (C) 2025 */

package com.example.airline.location.persistence.model;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;



class UriConverterTest
{
    private UriConverter classUnderTest;

    @BeforeEach
    void setUp()
    {
        classUnderTest = new UriConverter();
    }


    @Nested
    @DisplayName( "Convert Entity -> DB Column" )
    class EntityToDB
    {

        // ----- convertToDatabaseColumn
        @Test
        @DisplayName( "null converts to null" )
        void toDB_null_returnsNull()
        {
            String result = classUnderTest.convertToDatabaseColumn( null );

            assertNull( result );
        }


        @Test
        @DisplayName( "URI converts  to string" )
        void toDB_validUri_returnsString() throws Exception
        {
            final URI    uri    = new URI( "http://foo.bar/baz" );
            final String result = classUnderTest.convertToDatabaseColumn( uri );

            assertEquals( "http://foo.bar/baz", result );
        }
    }


    @Nested
    @DisplayName( "Convert DB Column -> Entity" )
    class DBtoEntity
    {
        // ----- convertToEntityAttribute
        @Test
        @DisplayName( "null converts to null" )
        void toDB_nullUriString_returnsNull()
        {
            assertNull( classUnderTest.convertToEntityAttribute( null ) );
        }



        @Test
        @DisplayName( "proper URI string converts to URI" )
        void toDB_validUriString_returnsCorrectURI()
        {
            final URI uri = classUnderTest.convertToEntityAttribute( "https://some.domain/pathh" );

            assertEquals( URI.create( "https://some.domain/pathh" ), uri );
        }



        @Test
        @DisplayName( "improper string throws exception" )
        void toDB_invalidURI_returnsNull()
        {
            final Throwable thrown = assertThrows( IllegalArgumentException.class,
                                                   () -> classUnderTest.convertToEntityAttribute( " this is bad " ) );

            assertAll( () -> assertEquals( "Illegal character in path at index 0:  this is bad ", thrown.getMessage() ) );
        }
    }


}
