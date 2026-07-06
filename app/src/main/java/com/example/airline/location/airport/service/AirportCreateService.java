/* (C) 2025 */

package com.example.airline.location.airport.service;


import com.example.airline.location.airport.mapper.AirportEntityMapper;
import com.example.airline.location.airport.persistence.repository.AirportRepository;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Continent domain object.
 */
@Service
public class AirportCreateService
{
    private final AirportRepository   repository;

    private final AirportEntityMapper mapper;

    /**
     * Create a ContinentService supported by autowire.
     *
     * @param repository jpa repository of Continents
     * @param mapper     maps entities to/from the domain model
     */
    public AirportCreateService( final AirportRepository repository, final AirportEntityMapper mapper )
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

    // ========== Administrative ==========
    // ===== HEAD =====
    // ===== INFO =====
    // ===== OPTION =====
    // ===== TRACE =====

}
