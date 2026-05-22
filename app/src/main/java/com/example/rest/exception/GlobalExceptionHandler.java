/* (C) 2025 */

// Helpful reference - https://exceptiondecoded.com

package com.example.rest.exception;


import java.net.URI;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.utility.HeaderUtility;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// TODO add support for.... MissingPathVariableException
/*
{
    "type": "about:blank",
    "title": "Internal Server Error",
    "status": 500,
    "detail": "Required URI template variable 'code' for method parameter type String is not present",
    "instance": "/api/v1/location/airport/summary/continent/code",
    "logref": "f4562782-074d-4ce7-a354-e0c93f7b5bb5",
    "Exception": "org.springframework.web.bind.MissingPathVariableException"
*/

// TODO split into multiple @ControllerAdvice classes
// https://stackoverflow.com/questions/25495870/working-with-multiple-controlleradvice-classes


/**
 * Exception handler to catch application-specific exceptions and format them consistently.
 *
 */
@ControllerAdvice
// @RestContollerAdvice
@Log4j2
public class GlobalExceptionHandler //extends ResponseEntityExceptionHandler
{
    //    private final MediaType desiredContentType = new MediaType( MediaType.APPLICATION_JSON,
    //                                                                StandardCharsets.UTF_8 );

    // ========== Type / Argument Mismatch ==========
    /**
     * Create a standard error message when an API parameter is the wrong type.
     *
     * @param exception the intercepted exception;
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( MethodArgumentTypeMismatchException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail>
        handleMethodArgumentTypeMismatchException( final ServletWebRequest request,
                                                   final MethodArgumentTypeMismatchException exception )
    {
        final ProblemDetail details = detailForException( HttpStatus.BAD_REQUEST, exception );

        details.setTitle( "Parameter Type Mismatch" );

        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }


    /**
     * Create a standard error message when an API parameter is missing.
     *
     * @param exception the intercepted exception;
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( MissingServletRequestParameterException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail>
        handleMissingServletRequestParameterException( final ServletWebRequest request,
                                                       final MissingServletRequestParameterException exception )
    {
        // TODO potentially a problem with the content-type or lack of mapping to/from the
        // requested format and the internal POJO.
        final ProblemDetail details = exception.getBody();

        details.setTitle( "Missing Parameter" );
        details.setProperty( "Possibility 1", "Missing Path Variables" );
        details.setProperty( "Possibility 2", "Missing Query Parameter" );
        details.setProperty( "Possibility 3", "Missing Form Data" );

        setDetailProperties( details, exception );

        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }




    // ========== MediaType ==========z
    /**
     * Create a standard error message when called requesting an unsupported
     * media type as input or output.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( HttpMediaTypeNotSupportedException.class )
    @ResponseStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
    public ResponseEntity<ProblemDetail>
        handleUnsupportedMediaTypeException( final ServletWebRequest request,
                                             final HttpMediaTypeNotSupportedException exception )
    {
        final ProblemDetail details = detailForException( exception );

        details.setProperty( "Unsupported content:", exception.getContentType().toString() );
        details.setProperty( "Supported content:",
                             exception.getSupportedMediaTypes()
                                      .stream()
                                      .map( MediaType::toString )
                                      .collect( Collectors.joining( ", " ) ) );

        return ResponseEntity
                .status( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }

    /**
     * Create a standard error message when requesting an
     * unsupported media type as input or output.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( HttpMediaTypeNotAcceptableException.class )
    @ResponseStatus( HttpStatus.NOT_ACCEPTABLE )
    public ResponseEntity<ProblemDetail>
        handleUnacceptableMediaTypeException( final ServletWebRequest request,
                                              final HttpMediaTypeNotAcceptableException exception )
    {
        final ProblemDetail details = detailForException( exception );

        details.setTitle( "Unacceptable Media Type" );
        details.setProperty( "Supported content:",
                             exception.getSupportedMediaTypes()
                                      .stream()
                                      .map( MediaType::toString )
                                      .collect( Collectors.joining( ", " ) ) );

        return ResponseEntity
                .status( HttpStatus.NOT_ACCEPTABLE )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }

    /**
     * Create a standard error message when the request is for an unsupported
     * media type.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( HttpMediaTypeException.class )
    @ResponseStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
    public ResponseEntity<ProblemDetail>
        handleMediaTypeException( final ServletWebRequest request,
                                  final HttpMediaTypeException exception )
    {
        final ProblemDetail details = detailForException( exception );

        details.setTitle( "Bad Media Type" );
        details.setProperty( "Supported content:",
                             exception.getSupportedMediaTypes()
                                      .stream()
                                      .map( MediaType::toString )
                                      .collect( Collectors.joining( ", " ) ) );

        // TODO Message should indicate the bad media type as well as the acceptable types
        return ResponseEntity
                .status( exception.getStatusCode() )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }



    // ========== Validation ==========


    /**
     * Create a standard error message when the input request body contains invalid or
     * missing attributes.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( MethodArgumentNotValidException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail>
        handleRestValidationException( final WebRequest request,
                                       final MethodArgumentNotValidException exception )
    {
        final ProblemDetail details = exception.getBody();
        details.setTitle( String.format( "Validation failed on '%s'", exception.getObjectName() ) );

        final Multimap<String, String> validations =  ArrayListMultimap.create();
        for ( final FieldError error : exception.getBindingResult().getFieldErrors() )
        {
            // Need to use a multimap here since a single field may have multiple
            // violations
            // Apache Commons or Google Guava
            validations.put( error.getField(),
                             // Ignore mutation failure for varargs
                             String.format( "%s, provided: [%s]",
                                            error.getDefaultMessage(),
                                            error.getRejectedValue() ) );
        }

        for ( final String key : validations.keySet() )
        {
            final String desc =
                    validations
                            .get( key )
                            .stream()
                            .collect( Collectors.joining( "; " ) );

            details.setProperty( key, desc );
        }

        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }



    /**
     * Create a standard error message when the input request body contains invalid or
     * missing attributes.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( ConstraintViolationException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail>
        handleConstraintViolations( final ServletWebRequest request,
                                    final ConstraintViolationException exception )
    {
        final Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        final ProblemDetail details = detailForException( HttpStatus.BAD_REQUEST, exception );

        details.setTitle( exception.getMessage() );
        details.setProperties( constraintViolations
                                       .stream()
                                       .collect( Collectors
                                                         .toMap( violation -> violation.getPropertyPath().toString(),
                                                                 ConstraintViolation::getMessage
                                                               ) ) );

        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }



    // ========== Malformed ==========
    /**
     * Create a standard error message when the input request body contains a
     * malformed request body.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( HttpMessageNotReadableException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail>
        handleMessageNotReadableException( final ServletWebRequest request,
                                           final HttpMessageNotReadableException exception )
    {
        // TODO potentially a problem with the content-type or lack of mapping to/from the
        // requested format and the internal POJO.
        final ProblemDetail details = detailForException( HttpStatus.BAD_REQUEST, exception );

        details.setTitle( "Malformed Request" );
        details.setProperty( "Possibility 1", "Malformed request body" );
        details.setProperty( "Possibility 2", "Invalid request parameters" );
        details.setProperty( "Possibility 3", "Incompatible data format" );
        details.setProperty( "Possibility 4", "Serialization errors" );

        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }

    /**
     * Create a standard error message when the input request body contains a
     * malformed request body.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( HttpMessageNotWritableException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail>
        handleMessageNotWritableException( final ServletWebRequest request,
                                           final HttpMessageNotWritableException exception )
    {
        final ProblemDetail details = detailForException( HttpStatus.NOT_IMPLEMENTED, exception );
        details.setTitle( "Unable to produce requested response format" );

        return ResponseEntity
                .status( HttpStatus.NOT_IMPLEMENTED )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }



    // ========== Unsupported Method ==========
    /**
     * Create a standard error message when an unsupported method (GET, PUT,
     * DELETE...) is specified.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( HttpRequestMethodNotSupportedException.class )
    @ResponseStatus( HttpStatus.METHOD_NOT_ALLOWED )
    public ResponseEntity<ProblemDetail>
        handleMethodNotSupportedException( final ServletWebRequest request,
                                           final HttpRequestMethodNotSupportedException exception )
    {
        final ProblemDetail details = detailForException( exception );

        return ResponseEntity
                .status( HttpStatus.METHOD_NOT_ALLOWED )
                .header( "Allow", exception.getSupportedMethods() )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }



    // ========== ResourceNotFound ==========
    /**
     * Create a standard error message when the specified resource (URI) does not
     * exist in the API. A NOT_FOUND does not mean the requested entity could not be
     * found. This case is represented in a NO_CONTENT response.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( NoResourceFoundException.class )
    @ResponseStatus( HttpStatus.NOT_FOUND )
    public ResponseEntity<ProblemDetail>
        handleResourceNotFoundException( final WebRequest request,
                                         final NoResourceFoundException exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should

        final ProblemDetail details = detailForException( exception );
        details.setInstance( URI.create( exception.getResourcePath() ) );

        return ResponseEntity
                .status( details.getStatus() )
                .location( details.getInstance() )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }

    /**
     * Create a standard error message when an API parameter is the wrong type.
     *
     * @param exception the intercepted exception;
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( EntityNotFoundException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail>
        handleEntityNotFoundException( final ServletWebRequest request,
                                       final EntityNotFoundException exception )
    {
        final ProblemDetail details = detailForException( HttpStatus.GONE, exception );
        details.setTitle( "Not Found" );

        return ResponseEntity
                .status( HttpStatus.GONE )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }


    // ========== General JPA Exception ==========
    /**
     * Create a standard error message as a catch-all for any unanticipated JPA exception.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( JpaSystemException.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    public ResponseEntity<ProblemDetail>
        handleJpaSystemException( final ServletWebRequest request,
                                  final JpaSystemException exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should

        final ProblemDetail details = detailForException( HttpStatus.INTERNAL_SERVER_ERROR, exception );

        details.setProperty( "logref", UUID.randomUUID() );

        return ResponseEntity
                .status( HttpStatus.INTERNAL_SERVER_ERROR )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }


    // ========== IllegalArgument ==========
    /**
     * Create a standard error message for an IllegalArgumentException.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( IllegalArgumentException.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    public ResponseEntity<ProblemDetail>
        handleIllegalArgumentException( final ServletWebRequest request,
                                        final IllegalArgumentException exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should
        final ProblemDetail details = detailForException( HttpStatus.BAD_REQUEST, exception );

        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }


    // ========== IllegalState ==========
    /**
     * Create a standard error message for an IllegalArgumentException.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( IllegalStateException.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    public ResponseEntity<ProblemDetail>
        handleIllegalStateException( final ServletWebRequest request,
                                     final IllegalStateException exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should
        final ProblemDetail details = detailForException( HttpStatus.BAD_REQUEST, exception );

        return ResponseEntity
                .status( HttpStatus.INTERNAL_SERVER_ERROR )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }


    // ========== Catch-All ==========
    /**
     * Create a standard error message as a catch-all for any unanticipated exception.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( UnsupportedOperationException.class )
    @ResponseStatus( HttpStatus.NOT_IMPLEMENTED )
    public ResponseEntity<ProblemDetail>
        handleUnsupportedOperationException( final ServletWebRequest request,
                                             final UnsupportedOperationException exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should

        final ProblemDetail details = detailForException( HttpStatus.NOT_IMPLEMENTED, exception );

        details.setProperty( "x-logref", UUID.randomUUID() );
        details.setProperty( "x-exception", exception.getClass().getTypeName() );
        if ( null != request )
        {
            details.setProperty( "x-TRACEPARENT", request.getHeader( HeaderUtility.TRACEID ) );
            details.setProperty( "x-TRACESTATE", request.getHeader( HeaderUtility.TRACESTATE ) );
        }
        if ( null != exception.getCause() )
        {
            details.setProperty( "x-Cause", exception.getCause() );
        }

        return ResponseEntity
                .status( HttpStatus.NOT_IMPLEMENTED )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }




    // ========== Catch-All ==========
    /**
     * Create a standard error message as a catch-all for any unanticipated exception.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( Exception.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    public ResponseEntity<ProblemDetail>
        handleGenericException( final ServletWebRequest request,
                                final Exception exception )
    {
        // log.error( exception.getMessage(), exception );
        // TODO the MDC should include the traceId (UUID) and log pattern should

        final ProblemDetail details = detailForException( HttpStatus.INTERNAL_SERVER_ERROR, exception );

        details.setProperty( "logref", UUID.randomUUID() );

        return ResponseEntity
                .status( HttpStatus.INTERNAL_SERVER_ERROR )
                .headers( HeaderUtility.copyNeededHeaders( request ) )
                .body( details );
    }


    //    private static <T extends ServletException & ErrorResponse> ProblemDetail details( Exception arg )
    //    {
    //        log.error( String.format( "Got exception implementing ErrorResponse '%s'", arg ) );
    //
    //        return detailForException( ((ErrorResponse)arg).getBody().getStatus(), arg );
    //    }

    private static ProblemDetail detailForException( final Exception exception )
    {
        final ErrorResponse errorResponse = (ErrorResponse)exception;

        return detailForException( errorResponse
                                       .getBody()
                                       .getStatus(),
                                   exception );
    }


    private static ProblemDetail detailForException( final int status, final Throwable throwable )
    {
        return detailForException( HttpStatus.valueOf( status ), throwable );
    }

    private static ProblemDetail detailForException( final HttpStatus status, final Throwable throwable )
    {
        final ProblemDetail details = ProblemDetail.forStatusAndDetail( status,
                                                                        throwable.getMessage() );
        setDetailProperties( details, throwable );

        return details;
    }


    private static void setDetailProperties( final ProblemDetail details, final Throwable exception )
    {
        final Throwable cause = exception.getCause();
        details.setProperty( "Exception", exception.toString() );
        if ( null != cause )
        {
            details.setProperty( "Cause", cause.toString() );
        }
    }

}
