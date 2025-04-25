package com.example.rest.exception;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

public class GlobalExceptionHandlerTest
{
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup()
    {
        handler = new GlobalExceptionHandler();
    }


    @Nested
    @DisplayName( "REST Api" )
    class Rest
    {
        @Nested
        @DisplayName( "MediaTypes" )
        class Media
        {
            // - HttpMediaTypeNotSupportedException
            @Test
            void exceptionMediaType_notSupported_formatsProblemDetails()
            {
                // --- given
                List<MediaType> supported = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );
                final HttpMediaTypeNotSupportedException exception =
                        new HttpMediaTypeNotSupportedException( MediaType.APPLICATION_RSS_XML,
                                                                supported,
                                                                HttpMethod.GET,
                                                                "Test Message"
                                                                );


                // --- when
                ResponseEntity<ProblemDetail> result =
                    handler.handleUnsupportedMediaTypeException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Unsupported Media Type", detail.getTitle() ),
                           () -> assertEquals( 415, detail.getStatus() ),
                           () -> assertEquals( "Test Message", detail.getDetail() ),
                           () -> assertEquals( "application/rss+xml",
                                               detail.getProperties()
                                                     .get( "Unsupported content:" ) ),
                           () -> assertEquals( "application/json, application/yaml",
                                               detail.getProperties()
                                                     .get( "Supported content:" ) ),
                           () -> assertEquals( HttpStatus.UNSUPPORTED_MEDIA_TYPE, result.getStatusCode() )
                         );
            }


            // - HttpMediaTypeNotAcceptableException
            @Test
            void exceptionMediaType_notAcceptable_formatsProblemDetails()
            {
                // --- given
                List<MediaType> supported = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );
                final HttpMediaTypeNotAcceptableException exception =
                        new HttpMediaTypeNotAcceptableException( supported );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleUnacceptableMediaTypeException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Unacceptable Media Type", detail.getTitle() ),
                           () -> assertEquals( 415, detail.getStatus() ),
                           () -> assertEquals( "No acceptable representation", detail.getDetail() ),
                           () -> assertEquals( "application/json, application/yaml",
                                               detail.getProperties()
                                                     .get( "Supported content:" ) ),
                           () -> assertEquals( HttpStatus.UNSUPPORTED_MEDIA_TYPE, result.getStatusCode() )
                         );
            }

            // - HttpMediaTypeException
            @Test
            void exceptionMediaType_notMediaTypeException_formatsProblemDetails()
            {
                // --- given
                List<MediaType> supported = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );
                final HttpMediaTypeException exception =
                        new HttpMediaTypeException( "Kilroy was here", supported, "detail code", null ) {
                            @Override
                            public HttpStatusCode getStatusCode()
                            {
                                return HttpStatus.ALREADY_REPORTED;
                            }
                        };

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleMediaTypeException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Bad Media Type", detail.getTitle() ),
                           () -> assertEquals( 208, detail.getStatus() ),
                           () -> assertEquals( "Kilroy was here", detail.getDetail() ),
                           () -> assertEquals( "application/json, application/yaml",
                                               detail.getProperties()
                                                     .get( "Supported content:" ) ),
                           () -> assertEquals( HttpStatus.ALREADY_REPORTED, result.getStatusCode() )
                         );
            }
        }
    }
}
