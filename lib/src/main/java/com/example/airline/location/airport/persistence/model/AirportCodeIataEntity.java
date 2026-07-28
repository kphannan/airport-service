/* (C) 2025 */

package com.example.airline.location.airport.persistence.model;


import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.aviation.reference.AviationCodePatterns;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.jspecify.annotations.NonNull;


/**
 * Definition of operations on the IATA reference table.
 */
@Entity
@Table( name = "iata_airportcode" )
@Getter
@Setter
@ToString
public class AirportCodeIataEntity
{
    private static final Pattern REGEX = Pattern.compile( AviationCodePatterns.AIRPORT_IATA );

    @Id
    @Column( name = "iata_code", length = 3, nullable = false, columnDefinition = "char(3)" )
    @jakarta.validation.constraints.Pattern( regexp = AviationCodePatterns.AIRPORT_IATA,
                                             message = "IATA code has three alphabetic characters" )
    @NonNull
    private String iataCode;


    /**
     * Limited access default constructor are needed by JPA.
     */
    protected AirportCodeIataEntity()
    {
        iataCode = "ZZZ";
    }

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

    /**
     * Verify the code conforms to the format of a IATA geocode of 3 uppercase letters.
     *
     * @see <a href=https://en.wikipedia.org/wiki/International_Air_Transport_Association>IATA</a>
     *
     * @param value the string to validate as an IATA code.
     * @return {@code true} if the input contains only 3 uppercase characters,
     *         {@code false} otherwise.
     */
    protected static boolean isValidIataCode( final String value )
    {
        final Matcher matcher = REGEX.matcher( value );

        return matcher.matches();
    }

    @Override
    public final boolean equals( final Object object )
    {
        if ( null == object || this == object )
        {
            return true;
        }

        final Class<?> oEffectiveClass =
            object instanceof HibernateProxy ? ( (HibernateProxy)object )
                                              .getHibernateLazyInitializer().getPersistentClass()
                                        : object.getClass();
        final Class<?> thisEffectiveClass =
            this instanceof HibernateProxy ? ( (HibernateProxy)this )
                                                 .getHibernateLazyInitializer().getPersistentClass()
                                           : this.getClass();
        if ( thisEffectiveClass != oEffectiveClass )
        {
            return false;
        }

        final AirportCodeIataEntity that = (AirportCodeIataEntity)object;

        return Objects.equals( getIataCode(), that.getIataCode() );
    }

    @Override
    public final int hashCode()
    {
        return this instanceof HibernateProxy ? ( (HibernateProxy)this ).getHibernateLazyInitializer()
                                                                      .getPersistentClass()
                                                                      .hashCode() : getClass().hashCode();
    }
}
