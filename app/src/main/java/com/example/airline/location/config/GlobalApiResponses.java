package com.example.airline.location.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@ApiResponses( value =
    {
        @ApiResponse( description = "No Content", responseCode = "204" ),
        @ApiResponse( description = "Bad Request",
                      responseCode =  "400",
                      content = {
                          @Content( mediaType = "application/json",
                                    schema =  @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                      }
                    ),
        @ApiResponse( description = "Resource Not Found",
                      responseCode =  "404",
                        content = {
                          @Content( mediaType = "application/json",
                                     schema =  @Schema( implementation = org.springframework.http.ProblemDetail.class )
                           )
                       }
                    ),
        @ApiResponse( description = "Method Not Allowed",
                      responseCode =  "405",
                      content = {
                          @Content( mediaType = "application/json",
                                     schema =  @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                       }
                    ),
        @ApiResponse( description = "Unsupported Media Type",
                      responseCode =  "415",
                      content = {
                          @Content( mediaType = "application/json",
                                     schema =  @Schema( implementation = org.springframework.http.ProblemDetail.class )
                          )
                       }
                    ),
        @ApiResponse( description = "Internal Server Error",
                        responseCode = "500",
                        content = {
                            @Content( mediaType = "application/json",
                                    schema =  @Schema( implementation = org.springframework.http.ProblemDetail.class )
                                )
                        }
                    )
    }
)
public @interface GlobalApiResponses
{}
