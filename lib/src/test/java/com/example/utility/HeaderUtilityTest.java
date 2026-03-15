package com.example.utility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.http.HttpHeadersAssert;

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
        HttpHeaders result = HeaderUtility.copyNeededHeaders( headers, null );
        assertThat( result )
                .isEqualTo( headers );
    }

    @Test
    void copyHeaders_emptyFilter_returnsOriginalHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        HttpHeaders result = HeaderUtility.copyNeededHeaders( headers, Collections.emptyList() );
        assertThat( result )
                .isEqualTo( headers );
    }

    @Test
    void copyHeaders_validFilter_returnsFilteredHeaders()
    {
        // -- given
        List<String> copiedHeaders = Arrays.asList( "TRACEID",
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
        HttpHeaders result = HeaderUtility.copyNeededHeaders( headers, copiedHeaders );

        // --- then
        final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result );
        assertAll( () -> headersAssert
                            .hasValue( "TRACEPARENT", "traceParent" )
                            .hasValue( "TRACESTATE", "traceState" )
                            .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                            .doesNotContainHeader( "NoWay" )
                 );
    }



}
