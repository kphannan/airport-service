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
        @ApiResponse( description = "Unauthorized",
                        responseCode = "403",
                        content = {
                        @Content( mediaType = "application/json",
                                    schema =  @Schema( implementation = org.springframework.http.ProblemDetail.class )
                                )
                        }
                    )
    }
)
public @interface GlobalApiSecurityResponses {

}
