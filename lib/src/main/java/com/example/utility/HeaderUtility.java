package com.example.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.WebRequest;

public final class HeaderUtility
{
    public static final String TRACEID    = "TRACEPARENT";
    public static final String TRACESTATE = "TRACESTATE";

    private static final List<String> usualHeaders = Arrays.asList( TRACEID, TRACESTATE, "Content-Type", "Allow" );

    /**
     * Hide constructor of a utility class.
     */
    private HeaderUtility()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }


    public static Collection<String> usualHeaders()
    {
        return usualHeaders;
    }


    public static HttpHeaders createHeaders( WebRequest request )
    {
        HttpHeaders headers = new HttpHeaders();

        usualHeaders.forEach( u -> headers.set(  u, request.getHeader( u ) ) );

        return headers;
    }

    public static HttpHeaders copyNeededHeaders( WebRequest request )
    {
        return copyNeededHeaders( createHeaders(  request ) );
    }

    public static HttpHeaders copyNeededHeaders( HttpHeaders headers )
    {
        return copyNeededHeaders( headers, usualHeaders );
    }

    // TODO Move to utility class with filter list as a static....
    public static HttpHeaders copyNeededHeaders( final HttpHeaders headers, final List<String> filterList )
    {
        if ( null == filterList || filterList.isEmpty() )
        {
            return headers;
        }

        //        List<String> filterList = Arrays.asList( "TRACEPARENT", "TRACESTATE", "Content-Type" );
        Set<String>  filterSet  = filterList.stream().collect( Collectors.toSet() );

        headers
                .headerSet()
                .stream()
                .filter( entry -> filterSet.contains( entry.getKey() ))
                .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) );

        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.putAll( headers );

        return newHeaders;
    }

    // TODO extract to a utility class
//    private HttpHeaders copyTraceHeaders( final HttpHeaders requestHeader )
//    {
//        return copyNeededHeaders( requestHeader );
//    }

//    private HttpHeaders responseHeaders()
//    {
//        return responseHeaders( desiredContentType );
//    }


//    private HttpHeaders responseHeaders( final HttpHeaders baseHeaders, final MediaType desiredContentType )
//    {
//        final HttpHeaders headers = new HttpHeaders( copyTraceHeaders( baseHeaders ) );
//        headers.setContentType( desiredContentType );
//
//        return headers;
//    }

//    private HttpHeaders responseHeaders( final HttpHeaders baseHeaders )
//    {
//        return responseHeaders( baseHeaders, desiredContentType );
//    }

//    private HttpHeaders responseHeaders( final MediaType desiredContentType )
//    {
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType( desiredContentType );
//
//        return headers;
//    }


}
