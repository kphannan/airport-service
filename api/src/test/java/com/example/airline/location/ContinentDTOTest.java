/* (C) 2025 */

package com.example.airline.location;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;



/**
 * Unit tests for the {@code ContinentDTO}.
 */
@SuppressWarnings( "PMD.AvoidDuplicateLiterals" )
class ContinentDTOTest
{
    @Test
    void constructor_NullArgs_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
    {
        final Throwable thrown = assertThrows( NullPointerException.class,
                                               () -> new ContinentDTO( null, null, null, null, null )
                                               );
        assertEquals( "code is marked non-null but is null", thrown.getMessage() );
//        final ContinentDTO dto = new ContinentDTO( null, null, null, null, null );

//        assertNotNull( dto );
    }

    @Test
    void constructor_withArgs_DoesNotCrash()
    {
        final ContinentDTO dto = new ContinentDTO( null, "EE", "::NAME::", null, null );

        assertNotNull( dto );
    }

    @Test
    void constructor_CodeNull_throwsNullPointer()   // TODO change lombok config to throw IllegalArgument instead of default NPE
    {
        final Throwable thrown = assertThrows( NullPointerException.class,
                                               () -> new ContinentDTO( null, null, ";;NAME;;", null, null )
                                             );
        assertEquals( "code is marked non-null but is null", thrown.getMessage() );
    }

    @Test
    void constructor_NameNull_throws()
    {
        final Throwable thrown = assertThrows( NullPointerException.class,
                                               () -> new ContinentDTO( null, "NN", null, null, null )
                                             );
        assertEquals( "name is marked non-null but is null", thrown.getMessage() );
    }

}
