/* (C) 2025 */

package com.example.airline.location.airport.persistence.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;


/**
 * Definition of operations on the IATA reference table.
 */
@Entity
@Table( name = "iata_airportcode" )
@Data
@NoArgsConstructor
public class AirportCodeIataEntity
{
    @Id
    // @Value("#{' matches [A-Z]{3}'}")
    @Column( name = "iata_code", length = 3, nullable = false, columnDefinition = "char(3)" )
    @NonNull private String iataCode;

    /**
     * Instantiate a IATA airport code record.
     *
     * @param iataCode the IATA code string.
     */
    public AirportCodeIataEntity( final String iataCode )
    {
        this.iataCode = hasText( iataCode ) ? iataCode : "ZZZ";
    }



    private boolean hasText( final String value )
    {
        return null != value && !value.trim().isBlank();
    }
}
