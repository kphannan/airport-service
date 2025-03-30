// package com.example;

// public class AirportServiceApplication {

// }



package com.example;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * REST service application.
 */
// @Slf4j
@Log4j2
@SpringBootApplication
// @EnableCaching
// @EnableZuulProxy
// @EnableFeignClients
// @EnableJpaRepositories("com.airline.locationservice.repository")
// @EnableJpaRepositories( "com.airline.locationservice" )
public final class AirportServiceApplication
{

    /**
     * Entry point of the Location REST service.
     *
     * @param args optional commandline arguments.
     */
    public static void main( final String[] args )
    {
        log.info( "Starting the app" );
        SpringApplication.run( AirportServiceApplication.class, args );
    }


    private AirportServiceApplication()
    {
        // Prevent instantiation
    }
}
