/* (C) 2025 */

package com.example.utility;


import java.util.Collection;

import lombok.extern.log4j.Log4j2;


/**
 * Helper methods for logging.
 */
@Log4j2
public final class LogUtility
{

    private LogUtility()
    {
        throw new IllegalStateException( "Instantiation of utility class is not allowed." );
    }

    static <T> void printList( final String msg, final Collection<T> collection )
    {
        log.info( "Display collection {}", msg );
        if ( null != collection )
        {
            collection.forEach( log::error );
        }
    }

}
