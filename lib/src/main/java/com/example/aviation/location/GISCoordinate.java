package com.example.aviation.location;


import java.math.BigDecimal;

import org.jspecify.annotations.NonNull;

/**
 * Abstract representation of a geographic coordinate pair consisting of
 * a latitude and a longitude.
 */
public abstract class GISCoordinate
{
    /** The latitude component of the coordinate pair. */
    @NonNull final GISLatitude  latitude;

    /** The longitude component of the coordinate pair. */
    @NonNull final GISLongitude longitude;


    /**
     * Constructs a coordinate pair from specific latitude and longitude objects.
     *
     * @param latitude  The {@link GISLatitude} component.
     * @param longitude The {@link GISLongitude} component.
     */
    protected GISCoordinate( @NonNull final GISLatitude latitude, @NonNull final GISLongitude longitude )
    {
        this.latitude  = latitude;
        this.longitude = longitude;
    }


    public @NonNull GISLatitude getLatitude()
    {
        return latitude;
    }

    public @NonNull GISLongitude getLongitude()
    {
        return longitude;
    }
}
