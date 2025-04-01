/* (C)2025 */

package com.example.airline.location.persistence.audit;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;



/**
 * It represents the audit log.
 */
@Data
@MappedSuperclass
public abstract class Audit
{
    @Column( name = "ACTION" )
    private String action;

    @Column( name = "ACTION_BY" )
    private String actionBy;

    @Column( name = "ACTION_ON" )
    private Date actionOn;

    /**
     * prevent intantiation unless by a derived class.
     */
    protected Audit()
    {
        // Intentionally empty ... for the moment ... auditing is not complete.
    }

}
