/* (C)2025 */

package com.example.airline.location.persistence.audit;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



/**
 * It represents the auditable entity.
 */
@Data
@MappedSuperclass
@EntityListeners( AuditingEntityListener.class )
// public abstract class Auditable<E extends Audit>
public abstract class Auditable<U>
{
    @Column( name = "ADDED_BY" )
    @CreatedBy
    private U addedBy;

    @Column( name = "ADDED_ON" )
    @Temporal( TemporalType.TIMESTAMP )
    @CreatedDate
    private Date addedOn;

    @Column( name = "MODIFIED_BY" )
    @LastModifiedBy
    private U modifiedBy;

    @Column( name = "MODIFIED_ON" )
    @Temporal( TemporalType.TIMESTAMP )
    @LastModifiedDate
    private Date modifiedOn;

    // /**
    // * Returns the id of the entity.
    // * @return
    // */
    // public abstract Long getId();

    // /**
    // * Create the audit record.
    // * @return
    // */
    // public abstract E createAudit();

    // Protected constructor to only allow derived classes to instantiate.
    // protected Auditable<U>()
    // {}
}
