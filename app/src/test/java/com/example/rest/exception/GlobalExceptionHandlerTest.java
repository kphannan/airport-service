package com.example.rest.exception;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

        @Nested
        @DisplayName( "Resources" )
        class UriIssues
        {

            @Test
            @DisplayName( "404 for resource (URI) not found" )
            void exceptionUri_notFound_formatsProblemDetails()
            {
                // --- given
                final NoResourceFoundException exception =
                        new NoResourceFoundException( HttpMethod.GET, "/some/resource/path" );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleResourceNotFoundException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Not Found", detail.getTitle() ),
                           () -> assertEquals( 404, detail.getStatus() ),
                           () -> assertEquals( "No static resource /some/resource/path.", detail.getDetail() )
                         );
            }
        }

        @Nested
        @DisplayName( "with bad HttpMethod" )
        class HttpMethodIssues
        {

            @Test
            @DisplayName( "405 for HttpMethod not implemented" )
            void exceptionHttpMethod_notSupported_formatsProblemDetails()
            {
                // --- given
                final HttpRequestMethodNotSupportedException exception =
                        new HttpRequestMethodNotSupportedException( "GET", List.of( "PUT", "POST", "DELETE") );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleMethodNotSupportedException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Method Not Allowed", detail.getTitle() ),
                           () -> assertEquals( 405, detail.getStatus() ),
                           () -> assertEquals( "Method 'GET' is not supported.", detail.getDetail() )
                         );
            }
        }


        @Nested
        @DisplayName( "with malformed message" )
        class MisunderstoodMessage
        {
            @Test
            @DisplayName( "400 for MessageNotReadable" )
            void exceptionMessage_notReadable_formatsProblemDetails()
            {
                // --- given
                final HttpInputMessage input = new HttpInputMessage()
                {
                    @Override
                    public InputStream getBody() throws IOException
                    {
                        return null;
                    }

                    @Override
                    public HttpHeaders getHeaders()
                    {
                        return null;
                    }
                };

                final HttpMessageNotReadableException exception =
                        new HttpMessageNotReadableException( "Test exception", input );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleMessageNotReadableException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Malformed Request", detail.getTitle() ),
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertEquals( "Test exception", detail.getDetail() )
                         );
            }

            @Test
            @DisplayName( "501 for MessageNotWritable" )
            void exceptionMessage_notWritable_formatsProblemDetails()
            {
                // --- given
                final HttpInputMessage input = new HttpInputMessage()
                {
                    @Override
                    public InputStream getBody() throws IOException
                    {
                        return null;
                    }

                    @Override
                    public HttpHeaders getHeaders()
                    {
                        return null;
                    }
                };

                final HttpMessageNotWritableException exception =
                        new HttpMessageNotWritableException( "Test writable exception" );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleMessageNotWritableException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Unable to produce requested response format", detail.getTitle() ),
                           () -> assertEquals( 501, detail.getStatus() ),
                           () -> assertEquals( "Test writable exception", detail.getDetail() )
                         );
            }
        }


        @Nested
        @DisplayName( "with parameter errors" )
        class ParameterErrors
        {
            @Test
            @DisplayName( "missing parameter returns 400" )
            void exceptionParameter_missing_formatsProblemDetails()
            {
                // --- given
                final MissingServletRequestParameterException exception =
                        new MissingServletRequestParameterException( "Param1", "String" );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleMissingServletRequestParameterException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Missing Parameter", detail.getTitle() ),
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertEquals( "Required parameter 'Param1' is not present.", detail.getDetail() )
                         );
            }

            @Test
            @DisplayName( "parameter type mismatch 400" )
            void exceptionParameter_typeMismatch_formatsProblemDetails() throws NoSuchMethodException
            {
                // --- given
                final MethodArgumentTypeMismatchException exception =
                        new MethodArgumentTypeMismatchException( "value", ArrayList.class, "name", null, null );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleMethodArgumentTypeMismatchException( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Parameter Type Mismatch", detail.getTitle() ),
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertEquals( "Method parameter 'name': Failed to convert value of type 'java.lang.String' to required type 'java.util.ArrayList'",
                                               detail.getDetail() )
                         );
            }

        }


        @Nested
        @DisplayName( "with validation violations" )
        class ValidationViolations
        {
            @Test
            @DisplayName( "constraint violation returns 400" )
            void exceptionValidation_constraints_formatsProblemDetails()
            {
                // --- given
                Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
                constraintViolations.add( buildConstraintViolation() );
                final ConstraintViolationException exception =
                        new ConstraintViolationException( "Constraint violation message", constraintViolations );

                // --- when
                ResponseEntity<ProblemDetail> result =
                        handler.handleConstraintViolations( exception );
                ProblemDetail detail = result.getBody();

                // --- then
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Constraint Violation", detail.getTitle() ),
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertEquals( "Constraint violation message",
                                               detail.getDetail() )
                         );
            }

        }


        ConstraintViolation buildConstraintViolation()
        {
            ConstraintViolation v = new ConstraintViolation() {
                @Override
                public String getMessage()
                {
                    return "constraint message";
                }

                @Override
                public String getMessageTemplate()
                {
                    return "";
                }

                @Override
                public Object getRootBean()
                {
                    return null;
                }

                @Override
                public Class getRootBeanClass()
                {
                    return null;
                }

                @Override
                public Object getLeafBean()
                {
                    return null;
                }

                @Override
                public Object[] getExecutableParameters()
                {
                    return new Object[0];
                }

                @Override
                public Object getExecutableReturnValue()
                {
                    return null;
                }

                @Override
                public Path getPropertyPath()
                {
                    return new Path(){

                        @Override
                        public Iterator<Node> iterator()
                        {
                            return null;
                        }

                        public String toString() { return "property path"; }
                    };
                }

                @Override
                public Object getInvalidValue()
                {
                    return "Invalid Value";
                }

                @Override
                public ConstraintDescriptor<?> getConstraintDescriptor()
                {
                    return null;
                }

                @Override
                public Object unwrap( Class type )
                {
                    return null;
                }
            };

            return v;
        }

    }





    @Nested
    @DisplayName( "When trouble with persistence" )
    class Persistence
    {
        @Test
        void exceptionPersistence_notFound_formatsProblemDetails()
        {
            // --- given
            Exception except = new Exception( "Test cause" );
            final EntityNotFoundException exception =
                    new EntityNotFoundException( "Dummy message", except );

            // --- when
            ResponseEntity<ProblemDetail> result =
                    handler.handleEntityNotFoundException( exception );
            ProblemDetail detail = result.getBody();

            // --- then
            assertAll( () -> assertNotNull( result ),
                       () -> assertEquals( "Not Found", detail.getTitle() ),
                       () -> assertEquals( 410, detail.getStatus() ),
                       () -> assertEquals( "Dummy message", detail.getDetail() )
                     );
        }
    }
}
