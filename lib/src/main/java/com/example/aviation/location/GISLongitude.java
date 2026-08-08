package com.example.aviation.location;

import java.math.BigDecimal;
import org.jspecify.annotations.NonNull;

/**
 * Base abstract representation of a geographic longitude.
 * <p>
 * Longitudes range from -180 to 180 degrees. This class provides the foundation for
 * various coordinate formats (DD, DDM, DMS) by storing the final calculated
 * value as a {@link BigDecimal} for high precision.
 */
public abstract class GISLongitude
{
    @NonNull
    private final BigDecimal decimalDegrees;

    /**
     * Initializes the longitude with a pre-calculated decimal degree value.
     *
     * @param decimalDegrees The calculated longitude in decimal degrees.
     */
    protected GISLongitude( @NonNull final BigDecimal decimalDegrees )
    {
        this.decimalDegrees = decimalDegrees;
    }

    /**
     * Returns the longitude expressed in decimal degrees.
     *
     * @return The non-null {@link BigDecimal} representation of the longitude.
     */
    @NonNull
    protected BigDecimal getDecimalDegrees()
    {
        return decimalDegrees;
    }
}
