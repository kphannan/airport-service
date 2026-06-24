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
 * Centralized definition of REST response objects.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@ApiResponses(
    value = {
        @ApiResponse( responseCode = "204",
                      description =
                      """
                      **No Content**
                      
                      The **HTTP 204 No Content** status code indicates that a server
                      has successfully processed a request but intentionally returns
                      no response body.
                      As a **2xx** success code, it confirms the operation
                      (such as a `DELETE`, `PUT`, or `PATCH`) completed as
                      expected without requiring the client to navigate away or
                      update the UI with new data.
                      
                      Key characteristics include:
                      
                      - **Empty Body**: The response message body must be empty; sending
                        content violates the HTTP specification.
                      - **Headers Only**: The response includes only HTTP headers,
                        such as `Cache-Control`, `ETag`, or `X-RateLimit`,
                        which remain valid for the resource.
                      - **Common Use Cases**: It is ideal for **silent background operations**
                        like clearing caches, updating settings, dismissing notifications,
                         or deleting resources where the client already knows the final state.
                      - **Distinction from 200 OK**: Unlike **200 OK**, which
                        returns a payload, **204** is used when returning data would
                         be **redundant** or unnecessary.
                      - **Distinction from 202 Accepted**: **204** signifies
                        immediate completion, whereas **202** indicates the request
                        is accepted for **asynchronous processing** that is not yet finished.
                      """,
                      content = @Content( schema = @Schema( implementation = Void.class ) )
        ),
        @ApiResponse( responseCode = "400",
                      description =
                          """
                          **Bad Request**
                          
                          The **HTTP 400 Bad Request** status code indicates a
                          **client error** where the server cannot process the request
                          due to **malformed syntax**, invalid message framing,
                          or deceptive routing.  It is a **4xx** response,
                          meaning the issue lies with the client’s request rather
                          than the server itself.
                          
                          Common causes include:
                          
                          - **Malformed URLs** containing illegal characters (e.g., unencoded spaces or curly braces).
                          - **Invalid request bodies**, such as broken JSON or mismatched `Content-Type` headers.
                          - **Corrupted or oversized** cookies that exceed server buffer limits.
                          - **Missing or incorrect headers**, such as a malformed Host or `Content-Length` field.
                          
                          Clients should **not retry** the same request without modification; instead, users should
                          **clear browser cache and cookies**, verify the **URL syntax**, or check for **browser extension
                          interference**.
                          
                          For developers, inspecting the raw request headers and validating the payload format against
                          API specifications is essential to resolve the error.
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        ),
        @ApiResponse( responseCode = "404",
                      description =
                          """
                          **Resource Not Found**
                          
                          **HTTP status 404 Not Found** is a client error code
                          indicating that the server cannot find the requested resource.
                          The first digit "4" signifies a client-side error, such as
                          a mistyped URL, while "04" specifies the exact
                          "Not Found" condition.
                          
                          - **Meaning**: The server was reachable, but the specific page,
                          file, or endpoint does not exist at the requested address.
                          - **Causes**: Common triggers include **broken** or **dead links**,
                          **deleted or moved** content without redirection, and typos in the URL.
                          - **Distinction**: Unlike **403 Forbidden** (which blocks access to existing resources),
                           **404** means the resource is missing.  If a resource is permanently removed,
                           **410 Gone** is the more accurate status.
                          - **SEO Impact**: Search engines remove URLs returning 404 from their index.
                            Frequent 404 errors signal poor site maintenance and can negatively
                            affect rankings.
                          - **Fixes**: Verify URL spelling and case sensitivity (critical on Linux servers),
                            implement **301 redirects** for moved pages, and fix broken internal links.
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        ),
        @ApiResponse( responseCode = "405",
                      description =
                          """
                          **Method Not Allowed**
                          
                          **HTTP 405 Method Not Allowed** is a client error status code indicating that the
                          server recognizes the requested resource and understands the HTTP method used
                          (such as `GET`, `POST`, `PUT`, or `DELETE`),
                          but refuses to process the request because that specific method is
                          not supported for that particular endpoint.
                          
                          Unlike a **404 Not Found** error, which signifies the resource does not exist,
                          a **405 error** confirms the URL is valid but the action attempted is prohibited.
                          It is distinct from **401 Unauthorized** or **403 Forbidden**,
                          which relate to authentication and permissions, as **405** is
                          strictly a method restriction issue.
                          
                          **Key Characteristics**
                          - **Allow Header**: The server response must include an
                          `Allow header` listing the HTTP methods that are permitted for the resource
                          (e.g., `Allow: GET, HEAD`, OPTIONS).
                          - **Common Causes**:
                            - Using the wrong HTTP verb for an endpoint (e.g., sending a `POST` request to a read-only
                            `GET` endpoint).
                            - Incorrect URL structure or routing misconfiguration in the backend framework
                              (e.g., Flask, Express, Spring Boot).
                            - Server-level restrictions in web servers like Nginx or Apache
                              (e.g., `limit_except` directives).
                            - *CORS* preflight failures where the server does not properly respond to `OPTIONS` requests.
                          
                          **Troubleshooting**
                          
                          To resolve a **405** error, verify the API documentation to ensure the correct HTTP
                          method is being used for the target endpoint. Send an `OPTIONS` request to the
                          URL to check the `Allow` header and identify supported methods.
                          If you control the server, ensure your routing rules explicitly define
                          all necessary methods for the route and that server configurations
                          do not inadvertently block valid verbs.
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        ),
        @ApiResponse( responseCode = "406",
                      description =
                          """
                          **Not Acceptable**
                          
                          **HTTP 406 Not Acceptable** is a **client error** status code indicating
                          that the server cannot provide a response matching the criteria specified
                          in the client’s `Accept` headers.  This error occurs during **content negotiation**
                          when there is a mismatch between the formats
                          (e.g., JSON, HTML, XML) or languages the client requests and what the
                          server is capable of delivering.
                          
                          **Common Causes**
                          
                          - **Misconfigured Accept Headers**: The client explicitly requests a content type
                            (e.g., `Accept`: `application/json`) that the server does not support.
                          - **Server Misconfiguration** The web server (Apache/Nginx) or application
                            framework is incorrectly configured to reject specific content types.
                          - **Web Application Firewall (WAF)**: Security tools like *mod_security*
                            may block legitimate requests if they detect suspicious patterns,
                            such as specific keywords in form submissions.
                          - **Browser Extensions or Cache**: Outdated cache files or faulty
                            browser extensions may send incorrect or restrictive headers.
                          
                          **How to Fix**
                          
                          - **Adjust Accept Headers**: Ensure the client requests a format the
                            server supports, such as adding `text/html` or using `*/*` to accept any type.
                          - **Clear Cache and Cookies**: Clearing browser data can resolve issues
                            caused by outdated or incorrect request headers.
                          - **Check Server Logs**: Review logs for *mod_security* rules or
                            server configuration errors that may be blocking the request.
                          - **Disable Extensions:** Temporarily disable browser extensions to rule
                            out interference with request headers.
                          - **Verify API Configuration**: For REST APIs, ensure the server’s response
                            format aligns with the client’s expected output format.
                          
                          **Distinction from Other Errors**
                          - **406 vs. 415**: A **406** error relates to the response format the
                            client wants to receive, while a **415 Unsupported Media Type** error
                            relates to the request body format the client sent.
                          - **406 vs. 404**: A **404** means the resource does not exist,
                            whereas a **406** means the resource exists but cannot be served
                            in the requested format.
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        ),
        @ApiResponse( responseCode = "410",
                      description =
                        """
                        **Gone**
                        
                        The **HTTP 410 Gone** status code indicates that a requested resource has been
                        **permanently removed** from the server and is **not expected to return**.
                        Unlike the **404 Not Found** error, which is ambiguous regarding whether
                        a missing page is temporary or permanent, the **410** code explicitly
                        signals intentional deletion with no forwarding address.
                        
                        **Key characteristics and implications include:**
                        
                        - **Permanent Removal**: It confirms the resource once existed
                          but has been intentionally deleted, telling clients and
                          search engines not to retry the request.
                        - **SEO Impact**: Search engines treat **410** responses as a strong signal
                          **to remove the URL from their index more quickly** than they
                          would for a **404** error, helping to clean up search results
                          and optimize crawl budget.
                        - **Common Use Cases**: It is typically used for expired promotions,
                          discontinued products, outdated blog posts, or spam-injected
                          pages that have been purged.
                        - **Implementation**: Developers can serve this code via server configurations
                          (e.g., `.htaccess` or Nginx rules) or application code
                          (e.g., ^^res.status(410)^ in Node.js).
                        - **No Redirection**: A **410** response should not include a
                          redirect header, as that would contradict the message that
                          the resource is permanently gone.
                        """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        ),
        @ApiResponse( responseCode = "415",
                      description =
                          """
                          **Unsupported Media  Type**
                          
                          The **HTTP 415 Unsupported Media Type** status code indicates
                          that the server refuses to accept the request because the
                          payload format or `Content-Type` header does not match the
                          media types supported by the target resource.
                          This is a **client-side error (4xx)** occurring when the
                          client sends data in a format (e.g., JSON, XML) that the
                          endpoint cannot process, often due to a missing, incorrect,
                          or mismatched Content-Type header.
                          
                          Common causes include sending **JSON** data with a
                          `text/plain` header, omitting the `Content-Type `header entirely,
                           or specifying a charset format the server rejects
                           (e.g., `UTF8` instead of `UTF-8`).
                           To resolve this, ensure the request body format aligns
                           with the API documentation and explicitly set the
                           correct `Content-Type` header, such as
                           `application/json` or `application/xml`
                          
                          **Key distinctions include:**
                          
                          - **415 vs. 400**: A **400** Bad Request implies
                            general invalidity, while 415 specifically targets
                            the media type format.
                          - **415 vs. 406**: A **415** error concerns the format
                          of the data *sent* (request), whereas a **406 Not Acceptable**
                          concerns the format of the data requested to receive (response).
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        ),
        @ApiResponse( responseCode = "500",
                      description =
                          """
                          **Internal Server Error**
                          
                          **HTTP status 500** is a generic *"Internal Server Error"*
                          indicating that the server encountered an unexpected condition
                          that prevented it from fulfilling the request.
                          It serves as a catch-all response when the server cannot determine
                          a more specific 5xx error code, meaning the issue lies entirely
                          on the **server side** rather than with the client.
                          
                          Common causes include **improper server configuration**
                          (such as corrupted `.htaccess` files or incorrect file permissions),
                          **programming errors** (syntax flaws or unhandled exceptions in scripts like PHP),
                          **resource exhaustion** (out-of-memory or disk space issues),
                          or **database connectivity failures**.
                          
                          Because the error message is intentionally vague to prevent exposing
                          sensitive infrastructure details to potential attackers,
                          administrators must investigate server logs to identify the specific root cause.
                          
                          For end-users, the error is not their fault; standard
                          troubleshooting steps include **refreshing the page**,
                          **clearing browser cache**, or **trying a different browser**.
                          If the issue persists, it requires intervention from the
                          website owner or hosting provider to debug application code and server settings.
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        ),
        @ApiResponse( responseCode = "501",
                      description =
                          """
                          **Not Implemented**
                          
                          The **HTTP 501 Not Implemented** status code indicates that the
                          server **does not recognize the request method** or
                          **lacks the functionality** required to fulfill the request.
                          It is a permanent server-side error, distinct from a **500** error
                          (generic failure) or a **405** error (method recognized but forbidden).
                          
                          **Key Characteristics:**
                          
                          - **Definition**: The server cannot execute the requested
                            action because it is not implemented in the server
                            software or configuration.
                          - **Common Triggers**: Using unsupported HTTP methods
                            (like `PUT`, `DELETE`, or `PATCH`) on a server that only
                            accepts `GET`/`POST`, or requesting WebDAV/extension
                            features not configured on the server.
                          - **Resolution**: Fixes typically involve **updating server software**,
                          **correcting HTTP method configurations** in server files
                          (like `.htaccess` or *nginx.conf*), or contacting the hosting provider
                          if the server is missing necessary modules.
                          - **Caching**: These responses are **cacheable by default**
                            unless specific caching headers dictate otherwise.
                          """,
                      content = {
                          @Content( mediaType = "application/json",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/yaml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          ),
                          @Content( mediaType = "application/xml",
                                    schema = @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
        )
    }
)
public @interface GlobalApiResponses
{
}
