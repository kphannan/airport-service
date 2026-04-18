package com.example.utility;

import static com.example.utility.HeaderUtility.copyNeededHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.test.http.HttpHeadersAssert;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author kevin
 * @since 2026-03-15
 * <p>
 * Copyright (c) 2020-2026
 */
class HeaderUtilityTest
{

    @Test
    void copyHeaders_nullFilter_returnsOriginalHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        HttpHeaders result = copyNeededHeaders( headers, null );
        assertThat( result )
                .isEqualTo( headers );
    }

    @Test
    void copyHeaders_emptyFilter_returnsOriginalHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        HttpHeaders result = copyNeededHeaders( headers, Collections.emptyList() );
        assertThat( result )
                .isEqualTo( headers );
    }

    @Test
    void copyHeaders_validFilter_returnsFilteredHeaders()
    {
        // -- given
        List<String> copiedHeaders = Arrays.asList( "TRACEPARENT",
                                                    "TRACESTATE",
                                                    "Content-Type",
                                                    "Allow",
                                                    "Accept-Encoding" );

        HttpHeaders headers = new HttpHeaders();
        headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
        headers.set( "Content-Type", "application/json;charset=UTF-8" );
        headers.set( HttpHeaders.ACCEPT_LANGUAGE, "en-US" );
        headers.set( HttpHeaders.ACCEPT_CHARSET, "utf-8" );
        headers.set( HttpHeaders.ACCEPT_ENCODING, "gzip" );
        headers.set( "TRACEPARENT", "traceParent" );
        headers.set( "TRACESTATE", "traceState" );

        // --- when
        HttpHeaders result = copyNeededHeaders( headers, copiedHeaders );

        // --- then
        final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result );
        assertAll( () -> headersAssert
                            .hasValue( "TRACEPARENT", "traceParent" )
                            .hasValue( "TRACESTATE", "traceState" )
                            .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                            .doesNotContainHeader( "NoWay" )
                 );
    }


    @Test
    void copyUsualHeaders_returnsFilteredHeaders()
    {
        // -- given
        HttpHeaders headers = new HttpHeaders();
        headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
        headers.set( "Content-Type", "application/json;charset=UTF-8" );
        headers.set( HttpHeaders.ACCEPT_LANGUAGE, "en-US" );
        headers.set( HttpHeaders.ACCEPT_CHARSET, "utf-8" );
        headers.set( HttpHeaders.ACCEPT_ENCODING, "gzip" );
        headers.set( "TRACEPARENT", "traceParent" );
        headers.set( "TRACESTATE", "traceState" );

        // --- when
        HttpHeaders result = copyNeededHeaders( headers );

        // --- then
        final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result );
        assertAll( () -> headersAssert
                           .hasValue( "TRACEPARENT", "traceParent" )
                           .hasValue( "TRACESTATE", "traceState" )
                           .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                           .doesNotContainHeader( "NoWay" )
                           .doesNotContainHeader( HttpHeaders.ACCEPT_LANGUAGE )
                           .doesNotContainHeader( HttpHeaders.ACCEPT_ENCODING )
                           .doesNotContainHeader( HttpHeaders.ACCEPT_CHARSET )
                           .doesNotContainHeader( "NoWay" )
                 );
    }

    @Test
    @DisplayName( "Includes necessary headers" )
    void includesNecessaryHeaders()
    {
        assertThat( HeaderUtility.usualHeaders() )
                .contains( "Allow" )
                .contains( "Content-Type" )
//                .contains( "Accept-Encoding" )
                .contains( "TRACEPARENT" )
                .contains( "TRACESTATE" );
    }


    @Test
    void copyHeaders_fromWebRequest_returnsFilteredHeaders()
    {
        WebRequest  webRequest = new WebRequest()
        {
            @Override
            public @Nullable Object getAttribute( String name, int scope )
            {
                return null;
            }

            @Override
            public void setAttribute( String name, Object value, int scope )
            {

            }

            @Override
            public void removeAttribute( String name, int scope )
            {

            }

            @Override
            public String[] getAttributeNames( int scope )
            {
                return new String[0];
            }

            @Override
            public void registerDestructionCallback( String name, Runnable callback, int scope )
            {

            }

            @Override
            public @Nullable Object resolveReference( String key )
            {
                return null;
            }

            @Override
            public String getSessionId()
            {
                return "";
            }

            @Override
            public Object getSessionMutex()
            {
                return null;
            }

            @Override
            public @Nullable String getHeader( String headerName )
            {
                Map<String, String[]> foo = new HashMap<>();
                foo.put( "TRACEPARENT", Stream.of( "traceParent" ).toArray( String[]::new ) );
                foo.put( "TRACESTATE", Stream.of( "traceState" ).toArray( String[]::new ) );
                foo.put( "Content-Type", Stream.of("application/json;charset=UTF-8").toArray( String[]::new ) );

                return foo.containsKey( headerName ) ? foo.get( headerName )[0] : "";
            }

            @Override
            public String @Nullable [] getHeaderValues( String headerName )
            {
//                Map<String, String[]> foo = new HashMap<>();
//                foo.put( "TRACEPARENT", Stream.of( "traceParent" ).toArray( String[]::new ) );
//                foo.put( "TRACESTATE", Stream.of( "traceState" ).toArray( String[]::new ) );
//
//                return foo.get( headerName );
                return new String[0];
            }

            @Override
            public Iterator<String> getHeaderNames()
            {
                return null;
            }

            @Override
            public @Nullable String getParameter( String paramName )
            {
                return "";
            }

            @Override
            public String @Nullable [] getParameterValues( String paramName )
            {
                return new String[0];
            }

            @Override
            public Iterator<String> getParameterNames()
            {
                return null;
            }

            @Override
            public Map<String, String[]> getParameterMap()
            {
                return Map.of();
            }

            @Override
            public Locale getLocale()
            {
                return null;
            }

            @Override
            public String getContextPath()
            {
                return "";
            }

            @Override
            public @Nullable String getRemoteUser()
            {
                return "";
            }

            @Override
            public @Nullable Principal getUserPrincipal()
            {
                return null;
            }

            @Override
            public boolean isUserInRole( String role )
            {
                return false;
            }

            @Override
            public boolean isSecure()
            {
                return false;
            }

            @Override
            public boolean checkNotModified( long lastModifiedTimestamp )
            {
                return false;
            }

            @Override
            public boolean checkNotModified( String etag )
            {
                return false;
            }

            @Override
            public boolean checkNotModified( @Nullable String etag, long lastModifiedTimestamp )
            {
                return false;
            }

            @Override
            public String getDescription( boolean includeClientInfo )
            {
                return "";
            }
        };

        HttpHeaders       headers = copyNeededHeaders( webRequest );


        final HttpHeadersAssert headersAssert = new HttpHeadersAssert( headers );
        assertAll( () -> headersAssert
                           .hasValue( "TRACEPARENT", "traceParent" )
                           .hasValue( "TRACESTATE", "traceState" )
                           .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                           .doesNotContainHeader( "NoWay" )
                 );
    }


    @Test
    @DisplayName( "Is a Proper Utility class" )
    void copyHeaders_IsUtilityClass()
    {
        final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

        assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( HeaderUtility.class, reason ) ),
                   () -> assertEquals( "[]", reason.toString() ) );

    }

}
