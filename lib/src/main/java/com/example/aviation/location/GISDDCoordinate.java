package com.example.aviation.location;


import org.jspecify.annotations.NonNull;

/**
 * A coordinate pair represented in Decimal Degrees (DD) format.
 */
public class GISDDCoordinate extends GISCoordinate
{
    /**
     * Constructs a DD coordinate pair.
     *
     * @param latitude  The {@link GISDDLatitude} component.
     * @param longitude The {@link GISDDLongitude} component.
     */
    public GISDDCoordinate( @NonNull GISDDLatitude latitude, @NonNull GISDDLongitude longitude )
    {
        super( latitude, longitude );
    }
}
