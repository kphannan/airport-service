package com.example.aviation.location;

import java.math.BigDecimal;
import org.jspecify.annotations.NonNull;

/**
 * Base abstract representation of a geographic latitude.
 * <p>
 * Latitudes range from -90 to 90 degrees. This class provides the foundation for
 * various coordinate formats (DD, DDM, DMS) by storing the final calculated
 * value as a {@link BigDecimal} for high precision.
 */
public abstract class GISLatitude
{
    @NonNull
    private final BigDecimal decimalDegrees;

    /**
     * Initializes the latitude with a pre-calculated decimal degree value.
     *
     * @param decimalDegrees The calculated latitude in decimal degrees.
     */
    protected GISLatitude( @NonNull final BigDecimal decimalDegrees )
    {
        this.decimalDegrees = decimalDegrees;
    }

    /**
     * Returns the latitude expressed in decimal degrees.
     *
     * @return The non-null {@link BigDecimal} representation of the latitude.
     */
    @NonNull
    protected BigDecimal getDecimalDegrees()
    {
        return decimalDegrees;
    }
}
