package com.example.aviation.location;

import org.jspecify.annotations.NonNull;

public class GISDMSCoordinate extends GISCoordinate
{
    public GISDMSCoordinate( @NonNull GISDMSLatitude latitude, @NonNull GISDMSLongitude longitude )
    {
        super( latitude, longitude );
    }
}
