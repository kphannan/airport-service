/* (C) 2025 */

package com.example.airline.location.airport.persistence.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;


/**
 * Definition of the IATA reference table.
 */
@Entity
@Table( name = "icao_airportcode" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportCodeIcaoEntity
{
    @Id
    @Column( name = "icao_code", length = 4, nullable = false, columnDefinition = "char(4)" )
    @NonNull private String icaoCode;

    // /**
    //  * Instantiate a ICAO airport code record.
    //  *
    //  * @param icaoCode the ICAO code string.
    //  */
    // public AirportCodeIcaoEntity( final String icaoCode )
    // {
    //     this.icaoCode = icaoCode;
    // }
}
