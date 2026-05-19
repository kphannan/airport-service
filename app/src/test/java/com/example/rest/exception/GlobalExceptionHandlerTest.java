package com.example.rest.exception;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.utility.HeaderUtility;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.core.MethodParameter;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.http.HttpHeadersAssert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


/**
 * Validation of the spring @ControllerAdvice global exception handler.
 */
@Log4j2
@DisplayName( "Exceptions - Global Handling" )
public class GlobalExceptionHandlerTest
{
    private final String    uuidPattern =
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
    private final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
                                                                StandardCharsets.UTF_8 );

    private GlobalExceptionHandler handler;
    private ServletWebRequest request;

    @BeforeEach
    void init()
    {
        handler = new GlobalExceptionHandler();

        final MockHttpServletRequest mock = new MockHttpServletRequest();
        mock.addHeader( HeaderUtility.TRACEID, "traceParent" );
        mock.addHeader( HeaderUtility.TRACESTATE, "traceState" );
        mock.addHeader( "Content-Type", desiredContentType.toString() );
        // mock.setContentType( desiredContentType.getType() );
        mock.addHeader( "NoWay", "should not be in the response" );

        request = null;  // TODO build out a reasonable request object
        request = new ServletWebRequest( mock );
    }


    /**
     * Verify processing of REST related exceptions.
     */
    @Nested
    @DisplayName( "REST Api" )
    class Rest
    {
        /**
         * Verify the handling of HttpMedia exceptions.
         */
        @Nested
        @DisplayName( "MediaTypes" )
        class Media
        {
            // - HttpMediaTypeNotSupportedException
            @Test
            @DisplayName( "Unsupported Media Type (415)" )
            void exceptionMediaType_notSupported_formatsProblemDetails()
            {
                // --- given
                final List<MediaType> supported = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );
                final HttpMediaTypeNotSupportedException exception =
                        new HttpMediaTypeNotSupportedException( MediaType.APPLICATION_RSS_XML,
                                                                supported,
                                                                HttpMethod.GET,
                                                                "Test Message"
                                                                );


                // --- when
                final ResponseEntity<ProblemDetail> result =
                    handler.handleUnsupportedMediaTypeException( request, exception );

                // --- then
                // NullPointerExceptions are a form of test failure.
                // detail should always exist
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           // -- status
                           () -> assertEquals( HttpStatus.UNSUPPORTED_MEDIA_TYPE, result.getStatusCode() ),
                           // -- headers
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           // -- Problem Detail
                           () -> assertEquals( 415, detail.getStatus() ),
                           () -> assertThat( detail.getTitle() )
                                   .contains( "Unsupported Media Type" ),
                           () -> assertThat( detail.getDetail() )
                                   .contains(  "Test Message" ),
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception",
                                                   "org.springframework.web.HttpMediaTypeNotSupportedException: Test Message" )
                                   .doesNotContainKey( "Cause" )
                                   .containsEntry( "Unsupported content:", "application/rss+xml" )
                                   .containsEntry( "Supported content:", "application/json, application/yaml" )
                );
            }


            // - HttpMediaTypeNotAcceptableException
            @Test
            @DisplayName( "Not Acceptable (406)" )
            void exceptionMediaType_notAcceptable_formatsProblemDetails()
            {
                // --- given
                final List<MediaType> supported = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );
                final HttpMediaTypeNotAcceptableException exception =
                        new HttpMediaTypeNotAcceptableException( supported );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleUnacceptableMediaTypeException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           // -- status
                           () -> assertEquals( HttpStatus.NOT_ACCEPTABLE, result.getStatusCode() ),
                           // -- headers
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           // -- Details
                           () -> assertEquals( 406, detail.getStatus() ),
                           () -> assertThat( detail.getTitle() )
                                   .contains( "Unacceptable Media Type" ),
                           () -> assertThat( detail.getDetail() )
                                   .contains(  "No acceptable representation" ),
                           //    -- detail properties
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception", "org.springframework.web.HttpMediaTypeNotAcceptableException: No acceptable representation" )
                                   .doesNotContainKey( "Cause" )
                                   .containsEntry( "Supported content:", "application/json, application/yaml" )
                );
            }

            // - HttpMediaTypeException
            @Test
            @DisplayName( "Already Reported (208)" )
            void exceptionMediaType_notMediaTypeException_formatsProblemDetails()
            {
                // --- given
                final List<MediaType> supported = List.of( MediaType.APPLICATION_JSON, MediaType.APPLICATION_YAML );
                final HttpMediaTypeException exception =
                    new HttpMediaTypeException( "Kilroy was here", supported, "detail code", null )
                    {
                        @Override
                        public HttpStatusCode getStatusCode()
                        {
                            return HttpStatus.ALREADY_REPORTED;
                        }
                    };

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleMediaTypeException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           // -- status
                           () -> assertEquals( HttpStatus.ALREADY_REPORTED, result.getStatusCode() ),
                           // -- headers
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           // -- Problem Detail
                           () -> assertEquals( 208, detail.getStatus() ),
                           () -> assertThat( detail.getTitle() )
                                   .contains( "Bad Media Type" ),
                           () -> assertThat( detail.getDetail() )
                                   .contains(  "Kilroy was here" ),
                           //    -- detail properties
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception",
                                                   "com.example.rest.exception.GlobalExceptionHandlerTest$Rest$Media$1: Kilroy was here" )
                                   .doesNotContainKey( "Cause" )
                                   .containsEntry( "Supported content:", "application/json, application/yaml" )
                );
            }
        }

        /**
         * Test processing of URI related exceptions.
         */
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
                        new NoResourceFoundException( HttpMethod.GET, "request-uri", "/some/resource/path" ); // TODO 3rd param and change in behavior

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleResourceNotFoundException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           //
                           () -> assertThat( result.getHeaders().getLocation() )
                                   .isNotNull(),
                           //
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception", "org.springframework.web.servlet.resource.NoResourceFoundException: No static resource /some/resource/path for request 'request-uri'." )
                                   // .doesNotContainKey( "Exception" )
                                   // .containsEntry( "Cause", "java.lang.Exception: Just Cause"  ),
                                   .doesNotContainKey( "Cause" ),
                           //
                           () -> assertEquals( "Not Found", detail.getTitle() ),
                           () -> assertEquals( 404, detail.getStatus() ),
                           () -> assertEquals( "No static resource /some/resource/path for request 'request-uri'.",
                                               detail.getDetail() )
                );
            }
        }

        /**
         * Test processing of HTTP Method related exceptions.
         */
        @Nested
        @DisplayName( "Bad HttpMethod" )
        class HttpMethodIssues
        {
            @Test
            @DisplayName( "405 - Method not allowed" )
            void exceptionHttpMethod_notSupported_formatsProblemDetails()
            {
                // --- given
                final HttpRequestMethodNotSupportedException exception =
                    new HttpRequestMethodNotSupportedException( "GET", List.of( "PUT", "POST", "DELETE" ) );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleMethodNotSupportedException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception",
                                                   "org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' is not supported" )
                                   .doesNotContainKey( "Cause" ),
                           //
                           () -> assertEquals( "Method Not Allowed", detail.getTitle() ),
                           () -> assertEquals( 405, detail.getStatus() ),
                           () -> assertEquals( "Request method 'GET' is not supported", detail.getDetail() )
                           // TODO check for accepted methods
                );
            }



            @Test
            @DisplayName( "unsupported operation - no cause or request" )
            void exception_unsupportedOperationNoCauseNoRequest_formatsProblemDetails()
            {
                // --- given
                final UnsupportedOperationException exception =
                        new UnsupportedOperationException( "Test UnsupportedOperation" );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleUnsupportedOperationException( null, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           //
                           () -> headersAssert
                                   .doesNotContainHeader( "TRACEPARENT" )
                                   .doesNotContainHeader( "TRACESTATE" )
                                   .doesNotContainHeader( "Content-Type" )
                                   .doesNotContainHeader( "NoWay" ),
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception",
                                                   "java.lang.UnsupportedOperationException: Test UnsupportedOperation" )
                                   .containsEntry( "x-exception", "java.lang.UnsupportedOperationException" )
                                   .hasEntrySatisfying( "x-logref",
                                                        value -> assertThat( value.toString() )
                                                                .matches( uuidPattern ) )
                                   .doesNotContainKey( "Cause" )
                                   .doesNotContainKey( "x-TRACEPARENT" )
                                   .doesNotContainKey( "x-TRACESTATE" )
                                   .doesNotContainKey( "x-Cause" ),
                           //
                           () -> assertEquals( "Internal Server Error", detail.getTitle() ),
                           () -> assertEquals( 500, detail.getStatus() ),
                           () -> assertEquals( "Test UnsupportedOperation", detail.getDetail() )
                );

            }

            @Test
            @DisplayName( "unsupported operation - no cause" )
            void exception_unsupportedOperationNoCause_formatsProblemDetails()
            {
                // --- given
                final UnsupportedOperationException exception =
                        new UnsupportedOperationException( "Test UnsupportedOperation" );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleUnsupportedOperationException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           //
                           // () -> assertThat( result.getHeaders().getLocation() )
                           //           .isNotNull(),
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception",
                                                   "java.lang.UnsupportedOperationException: Test UnsupportedOperation" )
                                   .containsEntry( "x-exception", "java.lang.UnsupportedOperationException" )
                                   .containsEntry( "x-TRACEPARENT", "traceParent" )
                                   .containsEntry( "x-TRACESTATE", "traceState" )
                                   .hasEntrySatisfying( "x-logref",
                                                        value -> assertThat( value.toString() )
                                                                .matches( uuidPattern ) )
                                   .doesNotContainKey( "Cause" )
                                   .doesNotContainKey( "x-Cause" ),
                           //
                           () -> assertEquals( "Internal Server Error", detail.getTitle() ),
                           () -> assertEquals( 500, detail.getStatus() ),
                           () -> assertEquals( "Test UnsupportedOperation", detail.getDetail() )
                );

            }


            @Test
            @DisplayName( "unsupported operation" )
            void exception_unsupportedOperation_formatsProblemDetails()
            {
                // --- given
                final UnsupportedOperationException exception =
                        new UnsupportedOperationException( "Test UnsupportedOperation",
                                                           new Throwable( "throwable cause" ) );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleUnsupportedOperationException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           //
                           // () -> assertThat( result.getHeaders().getLocation() )
                           //         .isNotNull(),
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception",
                                                   "java.lang.UnsupportedOperationException: Test UnsupportedOperation" )
                                   .containsEntry( "x-exception", "java.lang.UnsupportedOperationException" )
                                   .containsEntry( "x-TRACEPARENT", "traceParent" )
                                   .containsEntry( "x-TRACESTATE", "traceState" )
                                   .hasEntrySatisfying( "x-logref",
                                                        value -> assertThat( value.toString() )
                                                                .matches( uuidPattern ) )
                                   .hasEntrySatisfying( "x-Cause",
                                                        value -> assertThat( value.toString() )
                                                                .matches( "java.lang.Throwable: throwable cause" ) )
                                   .containsEntry( "Cause", "java.lang.Throwable: throwable cause" ),
                           //
                           () -> assertEquals( "Internal Server Error", detail.getTitle() ),
                           () -> assertEquals( 500, detail.getStatus() ),
                           () -> assertEquals( "Test UnsupportedOperation", detail.getDetail() )
                );

            }

        }


        @Nested
        @DisplayName( "Malformed message" )
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
                final ResponseEntity<ProblemDetail> result =
                        handler.handleMessageNotReadableException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception",
                                                   "org.springframework.http.converter.HttpMessageNotReadableException: Test exception" )
                                   .doesNotContainKey( "Cause" )
                                   .containsEntry( "Possibility 1", "Malformed request body" )
                                   .containsEntry( "Possibility 2", "Invalid request parameters" )
                                   .containsEntry( "Possibility 3", "Incompatible data format" )
                                   .containsEntry( "Possibility 4", "Serialization errors" ),
                           //
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
                // final HttpInputMessage input = new HttpInputMessage()
                // {
                //     @Override
                //     public InputStream getBody() throws IOException
                //     {
                //         return null;
                //     }
                //
                //     @Override
                //     public HttpHeaders getHeaders()
                //     {
                //         return null;
                //     }
                // };

                final HttpMessageNotWritableException exception =
                        new HttpMessageNotWritableException( "Test writable exception" );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleMessageNotWritableException( request, exception );

                // --- then
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                final ProblemDetail detail = result.getBody();
                assertAll( () -> assertNotNull( result ),
                           () -> assertNotNull( detail ),
                           () -> assertEquals( "Unable to produce requested response format", detail.getTitle() ),
                           () -> assertEquals( 501, detail.getStatus() ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           () -> assertEquals( "Test writable exception", detail.getDetail() )
                );
            }
        }


        /**
         * Test handling of exceptions denoting HTTP / REST parameter errors.
         */
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
                final ResponseEntity<ProblemDetail> result =
                        handler.handleMissingServletRequestParameterException( request, exception );
                final ProblemDetail detail = result.getBody();

                // --- then
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Missing Parameter", detail.getTitle() ),
                           () -> assertEquals( 400, detail.getStatus() ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           //
                           () -> assertThat( detail.getProperties() )
                                   // .hasEntrySatisfying( "Exception",  ) // TODO add a predicate regex
                                   .containsEntry( "Exception", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'Param1' for method parameter type String is not present" )
                                   .containsEntry( "Possibility 1", "Missing Path Variables" )
                                   .containsEntry( "Possibility 2", "Missing Query Parameter" )
                                   .containsEntry( "Possibility 3", "Missing Form Data" ),
                           () -> assertEquals( "Required parameter 'Param1' is not present.",
                                               detail.getDetail() )
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
                final ResponseEntity<ProblemDetail> result =
                        handler.handleMethodArgumentTypeMismatchException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Parameter Type Mismatch",
                                               detail.getTitle() ),
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertEquals( "Method parameter 'name': Failed to convert value of type 'java.lang.String' to required type 'java.util.ArrayList'",
                                               detail.getDetail() ),
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception", "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Method parameter 'name': Failed to convert value of type 'java.lang.String' to required type 'java.util.ArrayList'" )
                                   .doesNotContainKey( "Cause" ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" )
                );
            }


            @Test
            @DisplayName( "Illegal Argument with cause" )
            void exceptionParameter_illegalArgument_formatsProblemDetails() throws NoSuchMethodException
            {
                // --- given
                final IllegalArgumentException exception =
                        new IllegalArgumentException( "value", new Exception( "Just Cause" ) );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleIllegalArgumentException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Bad Request", detail.getTitle() ),
                           () -> assertEquals( 400,           detail.getStatus() ),
                           () -> assertEquals( "value",       detail.getDetail() ),
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception", "java.lang.IllegalArgumentException: value" )
                                   .containsEntry( "Cause", "java.lang.Exception: Just Cause"  ),
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                );
            }

            @Test
            @DisplayName( "Illegal Argument w/o" )
            void exceptionParameter_illegalArgumentWithoutCause_formatsProblemDetails() throws NoSuchMethodException
            {
                // --- given
                final IllegalArgumentException exception =
                        new IllegalArgumentException( "value" );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleIllegalArgumentException( request, exception );
                final ProblemDetail detail = result.getBody();


                // --- then
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Bad Request", detail.getTitle() ),
                           () -> assertEquals( 400,           detail.getStatus() ),
                           () -> assertEquals( "value",       detail.getDetail() ),
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception", "java.lang.IllegalArgumentException: value" )
                                   .doesNotContainKey( "Cause" ),
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                );
            }

            @Test
            @DisplayName( "Illegal State (400)" )
            void exceptionParameter_illegalState_formatsProblemDetails()
            {
                // --- given
                final IllegalStateException exception = new IllegalStateException( "test illegal state" );


                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleIllegalStateException( request, exception );

                // --- then
                // NullPointerExceptions are a form of test failure.
                // detail should always exist
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           // -- status
                           () -> assertEquals( HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode() ),
                           // -- headers
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           // -- Problem Detail
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertThat( detail.getTitle() )
                                   .contains( "Bad Request" ),
                           () -> assertThat( detail.getDetail() )
                                   .contains(  "test illegal state" ),
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception", "java.lang.IllegalStateException: test illegal state" )
                                   .doesNotContainKey( "Cause" )
                );
            }
        }

        /**
         * Test handling of Validation exceptions.
         */
        @Nested
        @DisplayName( "Validation violations" )
        class ValidationViolations
        {
            @Test
            @DisplayName( "constraint violation returns 400" )
            void exceptionValidation_constraints_formatsProblemDetails()
            {
                // --- given
                final Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
                constraintViolations.add( buildConstraintViolation() );
                final ConstraintViolationException exception =
                        new ConstraintViolationException( "Constraint violation message", constraintViolations );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleConstraintViolations( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           // -- status
                           () -> assertEquals( HttpStatus.BAD_REQUEST, result.getStatusCode() ),
                           // -- headers
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           // -- Problem Details
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertThat( detail.getTitle() )
                                   .contains( "Constraint violation message" ),
                           () -> assertThat( detail.getDetail() )
                                   .contains(  "Constraint violation message" ),
                           //    -- detail properties
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "property path", "constraint message" )
                                   .doesNotContainKey( "Cause" )
                );
            }


            private <T> ConstraintViolation<T> buildConstraintViolation()
            {
                return new ConstraintViolation<T>()
                {
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
                    public T getRootBean()
                    {
                        return null;
                    }

                    @Override
                    public Class<T> getRootBeanClass()
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
                        return new Path()
                        {
                            @Override
                            public Iterator<Node> iterator()
                            {
                                return null;
                            }

                            @Override
                            public String toString()
                            {
                                return "property path";
                            }
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
                    public <U> U unwrap( Class<U> type )
                    {
                        return null;
                    }
                };
            }


            private BindingResult buildBindingResult()
            {
                final BindingResult foo = new BindingResult()
                {
                    @Override
                    public @Nullable Object getTarget()
                    {
                        return null;
                    }

                    @Override
                    public Map<String, Object> getModel()
                    {
                        return Map.of();
                    }

                    @Override
                    public @Nullable Object getRawFieldValue( String field )
                    {
                        return null;
                    }

                    @Override
                    public @Nullable PropertyEditor findEditor( @Nullable String field, @Nullable Class<?> valueType )
                    {
                        return null;
                    }

                    @Override
                    public @Nullable PropertyEditorRegistry getPropertyEditorRegistry()
                    {
                        return null;
                    }

                    @Override
                    public String[] resolveMessageCodes( String errorCode )
                    {
                        return new String[0];
                    }

                    @Override
                    public String[] resolveMessageCodes( String errorCode, String field )
                    {
                        return new String[0];
                    }

                    @Override
                    public void addError( ObjectError error )
                    {
                        // Intentionally left blank
                    }

                    @Override
                    public String getObjectName()
                    {
                        return "TestStandIn";
                    }

                    @Override
                    public void reject( String errorCode, Object @Nullable [] errorArgs, @Nullable String defaultMessage )
                    {
                        // Intentionally left blank
                    }

                    @Override
                    public void rejectValue( @Nullable String field,
                                             String errorCode,
                                             Object @Nullable [] errorArgs,
                                             @Nullable String defaultMessage )
                    {
                        // Intentionally left blank
                    }

                    @Override
                    public List<ObjectError> getGlobalErrors()
                    {
                        return List.of();
                    }

                    @Override
                    public List<FieldError> getFieldErrors()
                    {
                        final FieldError fe1 = new FieldError( "TestStandIn", "foo",
                                                               "Value",
                                                               false, null, null,
                                                               "default foo error message" );
                        final FieldError fe2 = new FieldError( "TestStandIn", "bar", "default bar error message" );

                        return List.of( fe1, fe2 );
                    }

                    @Override
                    public @Nullable Object getFieldValue( String field )
                    {
                        return null;
                    }
                };

                return foo;
            }


            // @Disabled
            @Test
            @DisplayName( "constraint violation returns 400" )
            void exceptionValidation_methodArguments_formatsProblemDetails() throws NoSuchMethodException
            {
                // --- given
                final Method toStringMethod     = Object.class.getDeclaredMethod( "toString" );
                final MethodParameter parameter = new MethodParameter( toStringMethod, -1 );

                final MethodArgumentNotValidException exception =
                    new MethodArgumentNotValidException( parameter, buildBindingResult() );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleRestValidationException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           // -- status
                           () -> assertEquals( HttpStatus.BAD_REQUEST, result.getStatusCode() ),
                           // -- headers
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           // -- Problem Details
                           () -> assertEquals( 400, detail.getStatus() ),
                           () -> assertThat( detail.getTitle() )
                                   .contains( "Validation failed on 'TestStandIn'" ),
                           () -> assertThat( detail.getDetail() )
                                   .contains(  "Invalid request content" ),
                           //    -- detail properties
                           () -> assertThat( detail.getProperties() )
                                   .containsOnly( entry( "foo", "default foo error message, provided: [Value]" ),
                                                  entry( "bar", "default bar error message, provided: [null]" )
                                                )
                );
            }

        }


        /**
         * Test handling of Exceptions that are more generic.
         */
        @Nested
        @DisplayName( "Catch All" )
        class CatchAll
        {

            @Test
            @DisplayName( "constraint violation returns 400" )
            void exceptionValidation_constraints_formatsProblemDetails()
            {
                // --- given
                final Exception exception =
                        new Exception( "Catch all test", new Exception( "Test Cause" ) );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleGenericException( request, exception );

                // --- then
                final ProblemDetail detail = result.getBody();
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                assertAll( () -> assertNotNull( result ),
                           () -> assertEquals( "Internal Server Error", detail.getTitle() ),
                           () -> assertEquals( 500, detail.getStatus() ),
                           () -> assertNotNull( detail.getProperties().get( "logref" ) ),
                           //
                           () -> assertThat( detail.getProperties() )
                                   .containsEntry( "Exception", "java.lang.Exception: Catch all test" )
                                   .containsEntry( "Cause", "java.lang.Exception: Test Cause"  ),
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" )
                );
            }
        }

    }


    /**
     * Tests for Exceptions related to Persistence functions.
     */
    @Nested
    @DisplayName( "Persistence" )
    class Persistence
    {
        @Test
        @DisplayName( "Resource not present (Gone 410)" )
        void exceptionPersistence_notFound_formatsProblemDetails()
        {
            // --- given
            final Exception except = new Exception( "Test cause" );
            final EntityNotFoundException exception =
                    new EntityNotFoundException( "Dummy message", except );

            // --- when
            final ResponseEntity<ProblemDetail> result =
                    handler.handleEntityNotFoundException( request, exception );

            // --- then
            final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
            final ProblemDetail detail = result.getBody();
            assertAll( () -> assertNotNull( result ),
                       () -> assertEquals( "Not Found", detail.getTitle() ),
                       () -> assertEquals( 410, detail.getStatus() ),
                       () -> assertEquals( "Dummy message", detail.getDetail() ),
                       //
                       () -> assertThat( detail.getProperties() )
                               .containsEntry( "Exception", "jakarta.persistence.EntityNotFoundException: Dummy message" )
                               .containsEntry( "Cause", "java.lang.Exception: Test cause"  ),
                       //
                       () -> headersAssert
                               .hasValue( "TRACEPARENT", "traceParent" )
                               .hasValue( "TRACESTATE", "traceState" )
                               .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                               .doesNotContainHeader( "NoWay" )
            );
        }


        @Test
        @DisplayName( "Generic JPA" )
        void persistence_generic_formatsProblemDetails()
        {
            // --- given
            final RuntimeException except = new ClassCastException( "Test JPA cause" );
            final JpaSystemException exception =
                    new JpaSystemException( except );

            // --- when
            final ResponseEntity<ProblemDetail> result =
                    handler.handleJpaSystemException( request, exception );

            // --- then
            final ProblemDetail detail = result.getBody();
            final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
            assertAll( () -> assertNotNull( result ),
                       () -> assertEquals( "Internal Server Error", detail.getTitle() ),
                       () -> assertEquals( 500, detail.getStatus() ),
                       //
                       () -> headersAssert
                               .hasValue( "TRACEPARENT", "traceParent" )
                               .hasValue( "TRACESTATE", "traceState" )
                               .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                               .doesNotContainHeader( "NoWay" ),
                       //
                       () -> assertEquals( "Test JPA cause", detail.getDetail() ),
                       //
                       () -> assertThat( detail.getProperties() )
                               .containsEntry( "Exception", "org.springframework.orm.jpa.JpaSystemException: Test JPA cause" )
                               .containsEntry( "Cause", "java.lang.ClassCastException: Test JPA cause"  ),
                       // TODO look for an assertThat()...matches( "key", <regex> ) or ....contains( "key", <regex> )
                       () -> assertThat( detail.getProperties() )
                               .hasEntrySatisfying( "Exception",  //Exception -> org.springframework.orm.jpa.JpaSystemException
                                                    value -> assertThat( value.toString() )
                                                            .matches( ".*JpaSystemException.*Test JPA cause$" ) ),
                       () -> assertThat( detail.getProperties() )
                               .hasEntrySatisfying( "logref",
                                                    value -> assertThat( value.toString() )
                                                            .matches( uuidPattern ) )
            );
        }
    }
}
