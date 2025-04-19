package com.example.rest.utility;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


/**
 * Utilty supporting HttpHeaders.
 */
public final class HeaderUtility
{

    private HeaderUtility()
    {
        // Intentional -- needed as a utility class to prevent in stantiation
    }

    /**
     * Add required headers of Accept and Content-Type.
     *
     * @param builder the request builder
     * @return chain the builder after adding headers
     */
    public static MockHttpServletRequestBuilder withHeaders( final MockHttpServletRequestBuilder builder )
    {
        return withHeaders( builder, MediaType.APPLICATION_JSON );
    }

    /**
     * Add required headers of Accept and Content-Type.
     *
     * @param builder the request builder
     * @param mediaType the media type to add to the header
     * @return chain the builder after adding headers
     */
    public static  MockHttpServletRequestBuilder withHeaders( final MockHttpServletRequestBuilder builder, final MediaType mediaType )
    {
        return builder
                .header( HttpHeaders.ACCEPT, mediaType.toString() )
                .header( HttpHeaders.CONTENT_TYPE, mediaType.toString() );
    }
}