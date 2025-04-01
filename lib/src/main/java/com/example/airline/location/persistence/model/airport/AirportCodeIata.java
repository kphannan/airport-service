/* (C)2025 */

package com.example.airline.location.persistence.model.airport;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * Definition of operations on he IATA reference table.
 */
@Entity
@Table( name = "iata_airportcode" )
@Data
@NoArgsConstructor
public class AirportCodeIata
{
    @Id
    @Column( name = "IATA_CODE" )
    // @Value("#{' matches [A-Z]{3}'}")
    private String iataCode;

    /**
     * Instantiate a IATA airport code record.
     *
     * @param iataCode the IATA code string.
     */
    public AirportCodeIata( final String iataCode )
    {
        this.iataCode = hasText( iataCode ) ? iataCode : "ZZZ";
    }



    private boolean hasText( final String value )
    {
        if ( null != value )
        {
            return !value.trim().isBlank();
        }

        return false;
    }
}
