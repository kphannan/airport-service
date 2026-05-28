/* (C) 2025 */

package com.example.airline.location.config;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/**
 * Global configuration of API security violations.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@ApiResponses(
    value = {
        @ApiResponse( responseCode = "401",
                      description =
                          """
                          **Unauthorized**
                          
                          **HTTP 401 Unauthorized** is a standard **client error**
                          status code indicating that a request lacks valid authentication credentials
                          for the target resource.  The server returns this code when no credentials
                          are provided, the credentials are incorrect, or the authentication token has expired.
                          
                          **Key characteristics and distinctions include:**
                          
                          - **Authentication vs. Authorization**: A **401** error signals an
                            **authentication failure** (identity verification), whereas a
                            **403 Forbidden** error indicates an authorization failure
                            (the user is identified but lacks permission).
                          - **Server Response**: The server **MUST** include a `WWW-Authenticate` header
                            in the response, specifying the authentication scheme
                            (e.g., `Bearer`, `Basic`) required to access the resource.
                          - **Resolution**: Clients can resolve this error by providing
                            valid credentials, refreshing expired tokens, or correcting
                            formatting issues in the `Authorization` header.
                          - **Common Causes**: Missing headers, typos in usernames/passwords,
                            copy-paste errors introducing invisible characters,
                            or server-side configuration issues.
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class)
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class)
                          )
                      }
        ),
        @ApiResponse( responseCode = "403",
                      description =
                       """
                       **Forbidden**
                       
                       **HTTP 403 Forbidden** is a client error status code indicating that the server understood the
                       request but refuses to authorize it, even if valid credentials are provided.  Unlike a 
                       **401 Unauthorized** error, which suggests re-authentication might succeed, a **403** error
                       persists regardless of login status because the user lacks the necessary permissions or access
                       rights to the specific resource.
                       
                       **Common causes include:**
                       
                       - Insufficient Permissions: The user account does not have the required role or scope 
                       (e.g., trying to access an admin page as a standard user).
                       - IP Blocking: The server or a Web Application Firewall (WAF) has blocked the client's IP address.
                       - Misconfiguration: Incorrect file permissions, restrictive .htaccess rules, or disabled directory listings on the server.
                       - Security Policies: Aggressive security plugins, hotlink protection, or missing SSL client certificates.
                       
                       For website owners, **403** errors can negatively impact SEO by preventing search engine bots 
                       from indexing content and wasting crawl budget. Users encountering this error should typically
                       try clearing their browser cache and cookies, disabling VPNs, or contacting the site administrator
                       if access is unexpectedly denied.
                       """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class)
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class)
                          )
                      }
        )
    }
)
public @interface GlobalApiSecurityResponses
{
}
