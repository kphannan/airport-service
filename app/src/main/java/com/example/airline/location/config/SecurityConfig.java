package com.example.airline.location.config;

import java.util.Arrays;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity( debug = true )
@Log4j2
public class SecurityConfig         // TODO create security tests
{
    public SecurityConfig()
    {
        log.error( "SecurityConfig.constructor...." );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        log.error( "SecurityConfig.corsConfigurationSource...." );
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins( Arrays.asList( "http://localhost:5173" ) );
        configuration.setAllowedMethods( Arrays.asList( "GET", "POST", "PUT", "DELETE", "INFO", "PATCH" ) );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", configuration );

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception
    {
        log.error( "SecurityConfig.securityFilterChain...." );
        http.headers( headers ->
                              headers.frameOptions( options ->
                                                            options.sameOrigin() ))  // For H2 console access
//                .headers( headers ->
//                                  headers.frameOptions( frameOptions ->
//                                                                frameOptions.mode(SAMEORIGIGN))
//                        )
                                                       .cors( c ->
                               c.configurationSource( corsConfigurationSource() ) )
//                .exceptionHandling( customizer ->
//                                            customizer.authenticationEntryPoint( new HttpStatusEntryPoint( HttpStatus.UNAUTHORIZED ) ) )
                                                       .csrf( AbstractHttpConfigurer::disable )
                                                       .sessionManagement( customizer ->
                                            customizer.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                                                       .authorizeHttpRequests( (requests) ->
                                                requests.anyRequest().permitAll() )
                ;

        return http.build();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain( final HttpSecurity http ) throws Exception
    // {
    //     http
    //         .csrf()
    //         .disable()
    //         .authorizeRequests()
    //         .antMatchers( "/location/airport/**" ).permitAll()
    //         .anyRequest().authenticated();

    //     return http.build();
    // }



}
