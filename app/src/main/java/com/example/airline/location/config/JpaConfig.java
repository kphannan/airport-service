package com.example.airline.location.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories( basePackages = {"com.example.airline.location"} )
public class JpaConfig
{
}
