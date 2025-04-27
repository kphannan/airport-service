package com.example.airline.location.continent.mapper;

import java.util.Optional;

import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;

//@Component
public class EntityMapHelper
{
//    @Autowired
    private static final ContinentEntityMapper mapper = ContinentEntityMapper.INSTANCE;

    public static Optional<Continent> mapOptionalEntityToDomain( final Optional<ContinentEntity> from )
    {
        if ( from.isPresent() )
        {
            final Continent continent = mapper.entityToDomain( from.get() );

            return Optional.ofNullable( continent );
        }

        return Optional.empty();
    }
}
