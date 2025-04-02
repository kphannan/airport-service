/* (C) 2025 */

package com.example.airline.location;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;



/**
 * Unit tests fro the {@code ContinentDTO}.
 */
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class StopwatchTest
{
    @Test
    void constructor_NullArgs_DoesNotCrash()
    {
        ContinentDTO dto = new ContinentDTO( null, null, null, null, null );

        assertNotNull( dto );
    }
}
