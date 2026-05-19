package com.example.airline.location.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * JPA configuration, specifies the base package where JPA entities may be found.
 */
@Configuration
@EnableJpaRepositories( basePackages = {"com.example.airline.location"} )
public class JpaConfig
{
}
