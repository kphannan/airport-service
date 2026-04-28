package com.example.airline.location.continent.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.StringJoiner;

import com.example.utility.ValidateUtilityClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName( "Continent: Mapper" )
class EntityMapHelperTest
{
    // TODO include UtilityClass Verification

    @Test
    void isProperUtilityClass()
    {
        final StringJoiner reason = new StringJoiner( ", ", "[", "]" );

        assertAll( () -> assertTrue( ValidateUtilityClass.isProperUtilityClass( EntityMapHelper.class, reason ) ),
                   () -> assertEquals( "[]", reason.toString() ) );

    }
}
