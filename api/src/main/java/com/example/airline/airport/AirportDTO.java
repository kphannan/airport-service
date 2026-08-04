/* (C) 2025 */

package com.example.airline.airport;


import java.math.BigDecimal;
import java.net.URI;

import com.example.aviation.location.GISPatterns;
import com.example.airline.location.LocationCodePatterns;
import com.example.aviation.reference.AviationCodePatterns;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


//  TODO Convert to Java Record.
/**
 * API representation of an Airport.
 */
@Schema( description = "Represents a single airport." )
@Data
@AllArgsConstructor
@Builder
@SuppressWarnings( "PMD.TooManyFields" )
public class AirportDTO
{
    /**
     * Internal integer identifier for the airport. This will stay
     * persistent, even if the airport code changes.
     */
    @SuppressWarnings( "PMD.ShortVariable" )
    @JsonProperty( "id" )
    @Schema( name = "id",
             description = "Unique identifier",
             requiredMode = Schema.RequiredMode.REQUIRED )
    @NotNull( message = "An airport id is required" )
    private Long id;

    /**
     * The text identifier used in the URL. This will be the ICAO code
     * if available. Otherwise, it will be a local airport code (if no conflict), or
     * if nothing else is available, an internally generated code starting with the
     * ISO2 country code, followed by a dash and a four-digit number.
     */
    @NonNull
    @JsonProperty( "ident" )
    @Schema( name = "ident",
             description =
                 """
                 Unique abbreviation, of the airport; typically the ICAO code or
                 local_code or ISO 3166-1:alpha2 country code followed by dash
                 and a 4 digit number
                 """,
             requiredMode = Schema.RequiredMode.REQUIRED,
             // minLength = 4,
             // maxLength = 7,
             pattern = AviationCodePatterns.AIRPORT_IDENTIFIER, //"(([A-Z]{3,4})|([A-Z]{2}[0-9]{2})|([A-Z]{2}-[0-9]{4}))",
             example = "KORD" )
    @NotBlank( message = "A 4 to 7 character airport ident code is required" )
    @lombok.NonNull
    // @Pattern( regexp =
    //             """^(([0-9]{1,2}[A-Z]{1,2}[0-9]?)
    //                |([A-Z]{3,4}[0-9]?)
    //                |([A-Z]{1,2}-([0-9]{2,5}
    //                |[A-Z]{3,4}))
    //                |([A-Z]{1,2}[0-9]{1,2}[A-Z]{1,2}?[0-9]?)
    //                |([A-Z]{1,2}[0-9]{1,2})
    //                |([A-Z]{1,2}-(([0-9]{1,2}[A-Z]{1,2}[0-9]?)
    //                |([A-Z]{2,3}[0-9]{1,2}))))$
    //             """,
    @Pattern( regexp = AviationCodePatterns.AIRPORT_IDENTIFIER,
              message = "Airport ident must a unique 4 to 7 character code following a specific pattern" )
    private String ident;  // char-8

    /**
     * The type of the airport. Allowed values are "closed_airport", "heliport",
     * "large_airport", "medium_airport", "seaplane_base", and "small_airport". See
     * the map legend for a definition of each type.
     */
    @Schema( description = "Type of airport",
             requiredMode = Schema.RequiredMode.REQUIRED,
             maxLength = 14,
             pattern =
                 """
                 BALLOONPORT|CLOSED_AIRPORT|HELIPORT|LARGE_AIRPORT|MEDIUM_AIRPORT|SEAPLANE_BASE|SMALL_AIRPORT
                 """,
             example = "LARGE_AIRPORT0"
    )
    @NonNull private String type;  // char-14 - should become an enum

    /**
     * The official airport name, including "Airport", "Airstrip", etc.
     */
    @Schema( description = "The official airport name",
             requiredMode = Schema.RequiredMode.REQUIRED,
             maxLength = 128
    )
    @NonNull private String name;  // char-128

    /**
     * The airport latitude in decimal degrees (positive for north).
     */
    @Schema( description = "he airport longitude in decimal degrees (positive for north)",
             requiredMode = Schema.RequiredMode.REQUIRED,
             pattern = GISPatterns.DD_LATITUDE, //"^[+-]?(?:0|[1-9]{1,6})\\.(?:[0-9]{1,14})?$",
             example = "33.6367"
             )
    @NonNull private BigDecimal latitude;

    /**
     * The airport longitude in decimal degrees (positive for east).
     */
    @Schema( description = "The airport longitude in decimal degrees (positive for east)",
             requiredMode = Schema.RequiredMode.REQUIRED,
             pattern = GISPatterns.DD_LONGITUDE, //"^[+-]?(?:0|[1-9]{1,6})\\.(?:[0-9]{1,14})?$",
             example = "-84.428101"
    )
    @NonNull
    private BigDecimal longitude;

    /**
     * The airport elevation MSL in feet (not metres).
     */
    @Schema( description = "The airport mean sea level elevation in feet.",
             type = "number",
             format = "integer",
             example = "1026"
    )
    @Nullable
    private Integer elevation;

    /**
     * The code for the continent where the airport is (primarily) located. Allowed
     * values are "AF" (Africa), "AN" (Antarctica), "AS" (Asia), "EU" (Europe), "NA"
     * (North America), "OC" (Oceania), or "SA" (South America).
     * {@see ContinentDTO}
     */
    @JsonProperty( "continent" )
    @Schema( name = "continent",
             description = "Unique abbreviation, of the continent where this country is located.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             // minLength = 2,
             // maxLength = 2,
             pattern = LocationCodePatterns.CONTINENT_CODE,
             example = "AS" )
    // @NotBlank( message = "A 2-character continent code is required" )
    @lombok.NonNull
    @Pattern( regexp = LocationCodePatterns.CONTINENT_CODE,
              message =
                  """
                  Code must be one of these character sequences:
                  AF, AN, AS, EU, NA, OC, SA
                  """ )
    //
    // message = "Continent code must be 2 uppercase characters" )
    private String continent;  // char-2

    /**
     * The two-character ISO 3166-1:alpha2 code for the {@see CountryDTO} where the airport is
     * (primarily) located. A handful of unofficial, non-ISO codes are also in use,
     * such as "XK" for Kosovo. Points to the code column in countries.csv.
     */
    @JsonProperty( "isoCountry" )
    @Schema( name = "isoCountry",
             description = "The two-character ISO 3166-1:alpha2 code for the country.",
             requiredMode = Schema.RequiredMode.REQUIRED,
             // minLength = 2,
             // maxLength = 2,
             pattern = LocationCodePatterns.ISO_COUNTRY_CODE, //"[A-Z]{2}",
             example = "US" )
    @NotBlank( message = "An ISO 3166-1:alpha2 country code is required" )
    @lombok.NonNull
    @Pattern( regexp = "[A-Z]{2}", message = "Country code must be a valid ISO 3166-1:alpha2" )
    private String isoCountry;

    /**
     * An alphanumeric code for the high-level administrative subdivision of a
     * country where the airport is primarily located (e.g., province, governorate),
     * prefixed by the ISO2 country code and a hyphen.
     *
     * <p>ISO 3166:2 codes are used whenever possible, preferring higher
     * administrative levels, but also includes some custom codes. See the
     * documentation for regions.csv.
     * {@see RegionDTO}
     */
    @JsonProperty( "isoRegion" )
    @Schema( name = "isoRegion",
             description =
                     """
                        The two-character country code followed by an abbreviation
                        for the administrative subdivision (e.g., province, state)
                     """,
             requiredMode = Schema.RequiredMode.REQUIRED,
             // minLength = 3,
             // maxLength = 7,
             pattern = LocationCodePatterns.ISO_REGION_CODE, //"([A-Z]{2}-[A-Z\\-]{1,4})|(U-A)",
             example = "IE-D" )
    @NotBlank( message = "A unique region code is required" )
    @lombok.NonNull
    @Pattern( regexp = "([A-Z]{2}-[A-Z\\-]{1,4})|(U-A)",
              message = "Region code must be a valid ISO 3166-1:alpha2 followed by '-' and a local code" )
    private String isoRegion;  // char-7

    /**
     * The primary municipality that the airport serves (when available). Note that
     * this is not necessarily the municipality where the airport is physically
     * located.
     */
    //@NonNull
    @Schema( description =
                 """
                 The primary municipality that the airport serves (when available).
                 Note that this is not necessarily the municipality where the airport is physically located.
                 """,
             type = "string",
             minLength = 0,
             maxLength = 80
    )
    @Nullable
    private String municipality;  // char-80

    /**
     * "yes" if the airport currently has scheduled airline service, "no" otherwise.
     */
    @Schema( description = "Indicator if the airport has scheduled service yes or no",
             requiredMode = Schema.RequiredMode.REQUIRED,
             pattern = "yes|YES|no|NO"
    )
    @NonNull
    private String scheduledService;   // char-3 // TODO boolean...

    /**
     * The code that an aviation GPS database (such as Jeppesen's or Garmin's) would
     * normally use for the airport. This will always be the ICAO code if one
     * exists. Note that, unlike the ident column, this is not guaranteed to be
     * globally unique.
     */
    @JsonProperty( "gpsCode" )
    @Schema( name = "gpsCode",
             description =
                     """
                     ICAO codes, assigned by the International Civil Aviation
                     Organization, are four-letter codes. They’re used globally
                     in flight operations and Air Traffic Control.
                     """,
             example = "KATL",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 4 )
    @Pattern( regexp = "[A-HK-Z][A-Z]{3}",
              message = "Country name must be between 2 and 52 characters" )
    @Nullable
    private String gpsCode;  // char-4

    /**
     * The three-letter ICAO code for the airport (if it has one).
     */
    @JsonProperty( "icaoCode" )
    @Schema( name = "icaoCode",
             description =
                     """
                     ICAO codes, assigned by the International Civil Aviation
                     Organization, are four-letter codes. They’re used globally
                     in flight operations and Air Traffic Control.

                     The International Air Transport Association issues IATA
                     codes. These are the three-letter airport codes travelers
                     are most familiar with. Flight ticketing, baggage handling,
                     and cargo shipping primarily use these codes.
                     """,
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             pattern = AviationCodePatterns.AIRPORT_ICAO,
             maxLength = 4,
             example = "KATL"
    )
    @Pattern( regexp = AviationCodePatterns.AIRPORT_ICAO,
              message = "IACO code has four alpha-numeric characters" )
    @Nullable
    private String icaoCode;  // char-4

    /**
     * The three-letter IATA code for the airport (if it has one).
     */
    @JsonProperty( "iataCode" )
    @Schema( name = "iataCode",
             description = """
                           The International Air Transport Association's (IATA)
                           Location Identifier is a unique 3-letter code
                           (also commonly known as IATA code) used in aviation
                           and also in logistics to identify an airport.
                           """,
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             pattern = AviationCodePatterns.AIRPORT_IATA,
             example = "ATL",
             maxLength = 3 )
    @Pattern( regexp = AviationCodePatterns.AIRPORT_IATA,
              message = "IATA code has three alphabetic characters" )
    @Nullable
    private String iataCode;  // char-3

    /**
     * The local country code for the airport, if different from the gps_code and
     * iata_code fields (used mainly for US airports).
     */
    @JsonProperty( "localCode" )
    @Schema( name = "localCode",
             description =
                """
                The local country code for the airport, if different from the gps_code
                and iata_code fields (used mainly for US airports)
                """,
             example = "ATL",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 7 )
    @Nullable
    private String localCode;  // char-7

    /**
     * URL of the airport''s official home page on the web, if one exists.
     */
    @JsonProperty( "homeLink" )
    @Schema( name = "homeLink",
             description = "Link to the official website of the airport",
             example = "http://www.atlanta-airport.com",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    private URI homeLink; // URI  // char-255

    /**
     * URL of the airport''s page on Wikipedia, if one exists.
     */
    @JsonProperty( "wikiLink" )
    @Schema( name = "wikiLink",
             description = "Link to the Wikipedia article about the airport",
             example = "https://en.wikipedia.org/wiki/Hartsfield–Jackson_Atlanta_International_Airport",
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    private URI wikipediaLink; // URI  // char-255

    /**
     * Extra keywords/phrases for search, comma-separated. May include
     * former names for the airport, alternate codes, names in other languages,
     * nearby tourist destinations, etc.
     */
    @JsonProperty( "keywords" )
    @Schema( name = "keywords",
             description =
               """
               Extra keywords/phrases to assist with search, comma-separated.
               May include former names for the airport, alternate codes, names
               in other languages, nearby tourist destinations, etc.
               """,
             requiredMode = Schema.RequiredMode.NOT_REQUIRED,
             maxLength = 255 )
    @Nullable
    @Size( max = 255, message = "List of keywords may not exceed 255 characters" )
    private String keywords;  // char-255
}
