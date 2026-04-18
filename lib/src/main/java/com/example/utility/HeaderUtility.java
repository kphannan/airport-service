package com.example.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;

public final class HeaderUtility
{
    public static final String TRACEID    = "TRACEPARENT";
    public static final String TRACESTATE = "TRACESTATE";

    private static final List<String> usualHeaders =
            Arrays.asList( TRACEID, TRACESTATE, "Content-Type", "Allow" );

    /**
     * Hide constructor of a utility class.
     */
    private HeaderUtility()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }


    public static List<String> usualHeaders()
    {
        return usualHeaders;
    }


    public static HttpHeaders createHeaders( WebRequest request )
    {
        HttpHeaders headers = new HttpHeaders();

        if ( null != request )
        {
            usualHeaders.forEach( u -> headers.set( u, request.getHeader( u ) ) );
        }

        return headers;
    }

    public static HttpHeaders copyNeededHeaders( final WebRequest request )
    {
        return copyNeededHeaders( createHeaders(  request ) );
    }

    public static HttpHeaders copyNeededHeaders( final HttpHeaders headers )
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

        Set<String>  filterSet  = filterList
                .stream()
                .collect( Collectors.toSet() );


        Map<String, List<String>> filteredHeaders =
            headers
                .headerSet()
                .stream()
                .filter( entry -> filterSet.contains( entry.getKey() ))
                .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) );

        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.putAll( filteredHeaders );

        return newHeaders;
    }

}
