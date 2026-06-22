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

import com.example.rest.utility.ProblemDetailTester;
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
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
                                     .hasTitle( "Unsupported Media Type" )
                                     .hasDetail( "Test Message" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception",
                                                             "org.springframework.web.HttpMediaTypeNotSupportedException: Test Message" ),
                                                      entry( "Unsupported content:", "application/rss+xml" ),
                                                      entry( "Supported content:", "application/json, application/yaml" )
                                                   )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
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
                           // -- Problem Details
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.NOT_ACCEPTABLE )
                                     .hasTitle( "Unacceptable Media Type" )
                                     .hasDetail( "No acceptable representation" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties( entry( "Exception", "org.springframework.web.HttpMediaTypeNotAcceptableException: No acceptable representation" ),
                                                     entry( "Supported content:", "application/json, application/yaml" )
                                                   )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
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
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.ALREADY_REPORTED )
                                     .hasTitle( "Bad Media Type" )
                                     .hasDetail( "Kilroy was here" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception",
                                                             "com.example.rest.exception.GlobalExceptionHandlerTest$Rest$Media$1: Kilroy was here" ),
                                                      // entry( "Unsupported content:", "application/rss+xml" ),
                                                      entry( "Supported content:", "application/json, application/yaml" )
                                                   )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.NOT_FOUND )
                                     .hasTitle( "Not Found" )
                                     .hasDetail( "No static resource /some/resource/path for request 'request-uri'." )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception",
                                                             "org.springframework.web.servlet.resource.NoResourceFoundException: No static resource /some/resource/path for request 'request-uri'." )
                                                      // entry( "Unsupported content:", "application/rss+xml" ),
                                                      // entry( "Supported content:", "application/json, application/yaml" )
                                                   )
                                     .doesNotContainKey( "Cause" )
                                     .hasInstance( "/some/resource/path" )
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.METHOD_NOT_ALLOWED )
                                     .hasTitle( "Method Not Allowed" )
                                     .hasDetail( "Request method 'GET' is not supported" )
                                     .hasProperties(  entry( "Exception",
                                                             "org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' is not supported" )
                                                      // entry( "", "" ),
                                                      // entry( "", "" )
                                                   )
                                     // TODO Does not have properties
                                     // .doesNotHaveProperty(  "Cause" )
                                         // entry( "", "" )
                                         //           )
                                     .blankInstance()
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.NOT_IMPLEMENTED )
                                     // .hasInstance( "/" )
                                     .hasTitle( "Not Implemented" )
                                     .hasDetail( "Test UnsupportedOperation" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception",
                                                             "java.lang.UnsupportedOperationException: Test UnsupportedOperation" ),
                                                      entry( "x-exception", "java.lang.UnsupportedOperationException" )
                                         // entry( "Cause", "java.lang.Throwable: throwable cause" ),
                                         //              entry( "x-exception", "java.lang.UnsupportedOperationException" ),
                                         //              entry( "x-TRACEPARENT", "traceParent" ),
                                         //              entry( "x-TRACESTATE", "traceState" )
                                                   )
                                     .hasPropertiesSatisfying( entry( "x-logref", uuidPattern )
                                         // entry( "x-Cause", "java.lang.Throwable: throwable cause" )
                                                             )
                                     .blankInstance(),
                           // TODO make checks fluent
                           () -> assertThat( detail.getProperties() )
                                   .hasEntrySatisfying( "x-logref",
                                                        value -> assertThat( value.toString() )
                                                                .matches( uuidPattern ) )
                                   .doesNotContainKey( "Cause" )
                                   .doesNotContainKey( "x-TRACEPARENT" )
                                   .doesNotContainKey( "x-TRACESTATE" )
                                   .doesNotContainKey( "x-Cause" )
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.NOT_IMPLEMENTED )
                                     // .hasInstance( "/" )
                                     .hasTitle( "Not Implemented" )
                                     .hasDetail( "Test UnsupportedOperation" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception",
                                                             "java.lang.UnsupportedOperationException: Test UnsupportedOperation" ),
                                                      // entry( "Cause", "java.lang.Throwable: throwable cause" ),
                                                      entry( "x-exception", "java.lang.UnsupportedOperationException" ),
                                                      entry( "x-TRACEPARENT", "traceParent" ),
                                                      entry( "x-TRACESTATE", "traceState" )
                                                   )
                                     .hasPropertiesSatisfying( entry( "x-logref", uuidPattern )
                                                               // entry( "x-Cause", "java.lang.Throwable: throwable cause" )
                                                             )
                                     .blankInstance(),

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
                                   .doesNotContainKey( "x-Cause" )
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.NOT_IMPLEMENTED )
                                     // .hasInstance( "/" )
                                     .hasTitle( "Not Implemented" )
                                     .hasDetail( "Test UnsupportedOperation" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception",
                                                             "java.lang.UnsupportedOperationException: Test UnsupportedOperation" ),
                                                      entry( "Cause", "java.lang.Throwable: throwable cause" ),
                                                      entry( "x-exception", "java.lang.UnsupportedOperationException" ),
                                                      entry( "x-TRACEPARENT", "traceParent" ),
                                                      entry( "x-TRACESTATE", "traceState" )
                                                   )
                                     .hasPropertiesSatisfying( entry( "x-logref", uuidPattern ),
                                                               entry( "x-Cause", "java.lang.Throwable: throwable cause" )
                                                             )
                                     .blankInstance(),
                           () -> assertThat( detail.getProperties() )
                                   .hasEntrySatisfying( "x-logref",
                                                        value -> assertThat( value.toString() )
                                                                .matches( uuidPattern ) )
                                   .hasEntrySatisfying( "x-Cause",
                                                        value -> assertThat( value.toString() )
                                                                .matches( "java.lang.Throwable: throwable cause" ) )
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Malformed Request" )
                                     .hasDetail( "Test exception" )
                                     .hasProperties(  entry( "Exception",
                                                             "org.springframework.http.converter.HttpMessageNotReadableException: Test exception" ),
                                                      entry( "Possibility 1", "Malformed request body" ),
                                                      entry( "Possibility 2", "Invalid request parameters" ),
                                                      entry( "Possibility 3", "Incompatible data format" ),
                                                      entry( "Possibility 4", "Serialization errors" )
                                                   )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
                );
            }

            @Test
            @DisplayName( "501 for MessageNotWritable" )
            void exceptionMessage_notWritable_formatsProblemDetails()
            {
                // --- given
                final HttpMessageNotWritableException exception =
                        new HttpMessageNotWritableException( "Test writable exception" );

                // --- when
                final ResponseEntity<ProblemDetail> result =
                        handler.handleMessageNotWritableException( request, exception );

                // --- then
                final HttpHeadersAssert headersAssert = new HttpHeadersAssert( result.getHeaders() );
                final ProblemDetail detail = result.getBody();
                assertAll( () -> assertNotNull( result ),
                           //
                           () -> headersAssert
                                     .hasValue( "TRACEPARENT", "traceParent" )
                                     .hasValue( "TRACESTATE", "traceState" )
                                     .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                     .doesNotContainHeader( "NoWay" ),
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.NOT_IMPLEMENTED )
                                     .hasTitle( "Unable to produce requested response format" )
                                     .hasDetail( "Test writable exception" )
                                     .hasProperties( entry("Exception",
                                                            "org.springframework.http.converter.HttpMessageNotWritableException: Test writable exception" )
                                                   )
                                     .blankInstance()
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
                           //
                           () -> headersAssert
                                   .hasValue( "TRACEPARENT", "traceParent" )
                                   .hasValue( "TRACESTATE", "traceState" )
                                   .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                   .doesNotContainHeader( "NoWay" ),
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Missing Parameter" )
                                     .hasDetail( "Required parameter 'Param1' is not present." )
                                     .hasProperties(  entry( "Exception",
                                                             "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'Param1' for method parameter type String is not present" ),
                                                      entry( "Possibility 1", "Missing Path Variables" ),
                                                      entry( "Possibility 2", "Missing Query Parameter" ),
                                                      entry( "Possibility 3", "Missing Form Data" )
                                                   )
                                     .blankInstance()
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
                           //
                           () -> headersAssert
                                     .hasValue( "TRACEPARENT", "traceParent" )
                                     .hasValue( "TRACESTATE", "traceState" )
                                     .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                     .doesNotContainHeader( "NoWay" ),
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Parameter Type Mismatch" )
                                     .hasDetail( "Method parameter 'name': Failed to convert value of type 'java.lang.String' to required type 'java.util.ArrayList'" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties( entry("Exception",
                                                            "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Method parameter 'name': Failed to convert value of type 'java.lang.String' to required type 'java.util.ArrayList'" )
                                                   )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
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
                           () -> headersAssert
                                     .hasValue( "TRACEPARENT", "traceParent" )
                                     .hasValue( "TRACESTATE", "traceState" )
                                     .hasValue( "Content-Type", "application/json;charset=UTF-8" ),
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Bad Request" )
                                     .hasDetail( "value" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception", "java.lang.IllegalArgumentException: value" ),
                                                      entry( "Cause", "java.lang.Exception: Just Cause" )
                                                   )
                                     .blankInstance()
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
                           () -> headersAssert
                                     .hasValue( "TRACEPARENT", "traceParent" )
                                     .hasValue( "TRACESTATE", "traceState" )
                                     .hasValue( "Content-Type", "application/json;charset=UTF-8" ),
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Bad Request" )
                                     .hasDetail( "value" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception", "java.lang.IllegalArgumentException: value" ) )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
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
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Bad Request" )
                                     .hasDetail( "test illegal state" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "Exception", "java.lang.IllegalStateException: test illegal state" ) )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Constraint violation message" )
                                     .hasDetail( "Constraint violation message" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties(  entry( "property path","constraint message" ) )
                                     .doesNotContainKey( "Cause" )
                                     .blankInstance()
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
            @DisplayName( "constraint - Method Arguments, returns 400" )
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
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Validation failed on 'TestStandIn'" )
                                     .hasDetail( "Invalid request content." )
                                     .hasProperties( entry("foo", "default foo error message, provided: [Value]" ),
                                                     entry("bar", "default bar error message, provided: [null]" )
                                                   )
                                     .blankInstance()
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
                           //
                           () -> headersAssert
                                     .hasValue( "TRACEPARENT", "traceParent" )
                                     .hasValue( "TRACESTATE", "traceState" )
                                     .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                                     .doesNotContainHeader( "NoWay" ),
                           // -- Problem Detail
                           () -> assertThat( ProblemDetailTester.of( detail ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.INTERNAL_SERVER_ERROR )
                                     .hasTitle( "Internal Server Error" )
                                     // .hasDetail( "Test Message" )
                                     //  TODO need to map properties from JSON
                                     .hasProperties( entry("Exception", "java.lang.Exception: Catch all test" ),
                                                     entry("Cause", "java.lang.Exception: Test Cause" )
                                                   )
                                     .containsKey( "logref" )
                                     .blankInstance()
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
                       //
                       () -> headersAssert
                               .hasValue( "TRACEPARENT", "traceParent" )
                               .hasValue( "TRACESTATE", "traceState" )
                               .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                               .doesNotContainHeader( "NoWay" ),
                       // -- Problem Detail
                       () -> assertThat( ProblemDetailTester.of( detail ) )
                                 .blankType()
                                 .hasStatus( HttpStatus.GONE )
                                 .hasTitle( "Not Found" )
                                 .hasDetail( "Dummy message" )
                                 //  TODO need to map properties from JSON
                                 .hasProperties( entry( "Exception", "jakarta.persistence.EntityNotFoundException: Dummy message" ),
                                                 entry( "Cause", "java.lang.Exception: Test cause" )
                                               )
                                 .blankInstance()
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
                       //
                       () -> headersAssert
                               .hasValue( "TRACEPARENT", "traceParent" )
                               .hasValue( "TRACESTATE", "traceState" )
                               .hasValue( "Content-Type", "application/json;charset=UTF-8" )
                               .doesNotContainHeader( "NoWay" ),
                       // -- Problem Detail
                       () -> assertThat( ProblemDetailTester.of( detail ) )
                                 .blankType()
                                 .hasStatus( HttpStatus.INTERNAL_SERVER_ERROR )
                                 .hasTitle( "Internal Server Error" )
                                 .hasDetail( "Test JPA cause" )
                                 //  TODO need to map properties from JSON
                                 .hasProperties(  entry( "Exception", "org.springframework.orm.jpa.JpaSystemException: Test JPA cause" ),
                                                  entry( "Cause", "java.lang.ClassCastException: Test JPA cause" )
                                               )
                                 .blankInstance(),
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
