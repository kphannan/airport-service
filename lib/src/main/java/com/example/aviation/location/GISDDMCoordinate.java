package com.example.aviation.location;

import org.jspecify.annotations.NonNull;

public class GISDDMCoordinate extends GISCoordinate
{
    public GISDDMCoordinate( @NonNull GISDDMLatitude latitude, @NonNull GISDDMLongitude longitude )
    {
        super( latitude, longitude );
    }
}
