/* (C) 2025 */

package com.example.airline.location.region.service;


import com.example.airline.location.region.mapper.RegionEntityMapper;
import com.example.airline.location.region.model.Region;
import com.example.airline.location.region.persistence.repository.RegionRepository;
import org.springframework.stereotype.Service;


/**
 * Spring Service (business logic) supporting the Region domain object.
 */
@Service
public class RegionUpdateService
{
    private final RegionRepository repository;

    private final RegionEntityMapper mapper;

    /**
     * Create a RegionUpdateService supported by autowire.
     *
     * @param repository jpa repository of Region instances
     * @param mapper     maps entities to/from the domain model
     */
    public RegionUpdateService( final RegionRepository repository, final RegionEntityMapper mapper )
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
