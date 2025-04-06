/* (C) 2025 */

package com.example.airline.location.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;



@OpenAPIDefinition( info = @Info( contact = @Contact( name = "Kevin", email = "kphannan@gmail.com" ),
                                  description = "OpenAPI documentation for the Airport Information REST service",
                                  title = "Airport Information Service",
                                  version = "1.0",
                                  license = @License( name = "GNU General Public License, Version 3.0", url = "https://www.gnu.org/licenses/gpl-3.0.en.html" ),
                                  termsOfService = "Terms of Service"
                    ),
                    servers = {
                        @Server( description = "Local developer", url = "http://localhost:8100" ),
                        @Server( description = "Shared Dev", url = "http://localhost:8080" )
                    },
                    security = @SecurityRequirement( name = "bearerAuth" )
)
@SecuritySchemes( @SecurityScheme( name = "bearerAuth", description = "JWT auth token", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER ) )
public class OpenApiConfig
{
}
