/* (C) 2025 */

package com.example.airline.location.airport.persistence.model;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;


/**
 * Definition of operations on the IATA reference table.
 */
@Entity
@Table( name = "iata_airportcode" )
@Data
@NoArgsConstructor( access = AccessLevel.PROTECTED ) // JPA best practice
public class AirportCodeIataEntity
{
    private static final Pattern regex   = Pattern.compile( "[A-Z]{3}" );

    @Id
    // @Value("#{' matches [A-Z]{3}'}")
    @Column( name = "iata_code", length = 3, nullable = false, columnDefinition = "char(3)" )
    @jakarta.validation.constraints.Pattern( regexp = "[A-Z]{3}", message = "IATA code has three alphabetic characters" )
//    @NonNull private String iataCode;
    @NonNull private String iataCode = "ZZZ";


    /**
     * Instantiate a IATA airport code record.
     *
     * @param value the IATA code string.
     */
    public AirportCodeIataEntity( @NonNull final String value )
    {
        if ( isValidIataCode( value ) )
        {
            iataCode = value;
        }
        else
        {
            throw new IllegalArgumentException( String.format( "IATA code '%s' is invalid", value ) );
        }
    }

    public static boolean isValidIataCode( final String value )
    {
        Matcher matcher = regex.matcher( value );

        return matcher.matches();
    }

}
