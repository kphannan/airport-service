package com.example.utility;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;


/**
 * Utility class providing a standard handling of HttpHeaders.
 */
@Log4j2
public final class HeaderUtility
{
    public static final String TRACEID    = "TRACEPARENT";
    public static final String TRACESTATE = "TRACESTATE";

    private static final List<String> USUAL_HEADER_NAMES =
            Arrays.asList( TRACEID, TRACESTATE, "Content-Type", "Allow" );

    /**
     * Hide constructor of a utility class.
     */
    private HeaderUtility()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }


    /* default */
    static List<String> usualHeaders()
    {
        return USUAL_HEADER_NAMES;
    }


    /**
     * Extract the required headers from the collection in a web request.
     *
     * @param request the web request that with a set of http headers.
     * @return a new collection of headers with only the required headers.
     */
    public static HttpHeaders createHeaders( final WebRequest request )
    {
        final HttpHeaders headers = new HttpHeaders();

        if ( null != request )
        {
            USUAL_HEADER_NAMES.forEach( u -> headers.set( u, request.getHeader( u ) ) );
        }

        return headers;
    }

    /**
     * Copy headers, preserving only the required headers.
     *
     * @param request a WebRequest containing HttpHeaders.
     * @return a new collection of headers with only the required headers.
     */
    public static HttpHeaders copyNeededHeaders( final WebRequest request )
    {
        return copyNeededHeaders( createHeaders(  request ) );
    }

    /**
     * Copy headers, preserving only the required headers.
     *
     * @param headers the original Http headers to copy.
     * @return a new collection of headers with only the required headers.
     */
    public static HttpHeaders copyNeededHeaders( final HttpHeaders headers )
    {
        return copyNeededHeaders( headers, USUAL_HEADER_NAMES );
    }


    /**
     * Copy only required headers.
     *
     * @param headers the original Http headers to copy.
     * @param filterList collection of required header names, optional.
     * @return a new collection of headers with only the required headers.
     */
    public static HttpHeaders copyNeededHeaders( final HttpHeaders headers, final List<String> filterList )
    {
        if ( null == filterList || filterList.isEmpty() )
        {
            return headers;
        }

        final Set<String>  filterSet  = filterList
                .stream()
                .collect( Collectors.toSet() );


        final Map<String, List<String>> filteredHeaders =
            headers
                .headerSet()
                .stream()
                .filter( entry -> filterSet.contains( entry.getKey() ) )
                .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) );

        final HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.putAll( filteredHeaders );

        return newHeaders;
    }


    /**
     * Display HttpHeaders at info log level.
     *
     * @param headers the Http headers to display.
     */
    public static void printHeaders( final HttpHeaders headers )
    {
        headers.forEach(  ( name, value ) -> log.info( String.format( "%s : %s", name, value ) ) );
    }

}
