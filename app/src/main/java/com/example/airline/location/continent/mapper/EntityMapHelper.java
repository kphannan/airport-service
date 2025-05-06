package com.example.airline.location.continent.mapper;

import java.util.Optional;

import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;


/**
 * Utility methods related to mapping objects between domains.
 */
public final class EntityMapHelper
{
    private static final ContinentEntityMapper MAPPER = ContinentEntityMapper.INSTANCE;


    private EntityMapHelper()
    {
        // Prevent instantiation
    }

    /**
     * Map an entity object to a domain (service) layer object.
     *
     * @param from the Optional persistence entity
     * @return the similar service (domain) optional instance.
     */
    public static Optional<Continent> mapOptionalEntityToDomain( final Optional<ContinentEntity> from )
    {
        if ( from.isPresent() )
        {
            final Continent continent = MAPPER.entityToDomain( from.get() );

            return Optional.ofNullable( continent );
        }

        return Optional.empty();
    }



    /*
    public static <F,T> Optional<T> mapOptionalEntityToDomain( final Optional<F> from )
    {
        if ( from.isPresent() )
        {
            final T item = mapper.entityToDomain( from.get() );

            return Optional.ofNullable( item );
        }

        return Optional.empty();
    }
    */

}
