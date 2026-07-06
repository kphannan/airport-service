/* (C) 2025 */

package com.example.airline.location.airport.service;


import com.example.airline.location.airport.mapper.AirportEntityMapper;
import com.example.airline.location.airport.model.Airport;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
public class AirportDeleteService
{
    private final AirportRepository   repository;

    private final AirportEntityMapper mapper;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper     maps entities to/from the domain model
     */
    public AirportDeleteService( final AirportRepository repository, final AirportEntityMapper mapper )
    {
        this.repository = repository;
        this.mapper     = mapper;
    }


    // ========== CREATE ==========
    // ===== POST =====

    // ========== READ ==========
    // ===== GET =====
    // --- Single ---
    // --- Multiple ---

    // ========== UPDATE ==========
    // ===== PATCH =====
    // ===== PUT =====

    // ========== DELETE ==========
    // ===== DELETE =====
    /**
     * Delete the given entity.
     *
     * @param entity the entity to delete.
     * @return true if the entity existed before delete.
     */
    public boolean delete( final Airport entity )
    {
        final boolean existing = repository.existsById( entity.getId() );

        if ( existing )
        {
            repository.delete( mapper.domainToEntity( entity ) );
        }

        return existing;
    }

    /**
     * Delete the given entity.
     *
     * @param airportId the ID of the entity to delete.
     * @return true if the entity existed before delete.
     */
    public boolean deleteById( final Long airportId )
    {
        final boolean existing = repository.existsById( airportId );

        if ( existing )
        {
            repository.deleteById( airportId );
        }

        return existing;
    }
    // ========== Administrative ==========
    // ===== HEAD =====
    // ===== INFO =====
    // ===== OPTION =====
    // ===== TRACE =====


}
