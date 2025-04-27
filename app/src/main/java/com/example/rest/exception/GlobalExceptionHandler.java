/* (C) 2025 */

// Helpful reference - https://exceptiondecoded.com

package com.example.rest.exception;


import java.net.URI;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;



/**
 * Exception handler to catch application-specific exceptions and format them consistently.
 *
 */
@ControllerAdvice
// @RestContollerAdvice
@Log4j2
public class GlobalExceptionHandler
{

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
    handleMethodArgumentTypeMismatchException( final MethodArgumentTypeMismatchException exception )
    {
        final StringBuilder detailMessage =
                new StringBuilder()
                        .append( exception.getMessage() );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST,
                                                                        detailMessage.toString() );

        return new ResponseEntity<>( details, HttpStatus.BAD_REQUEST );
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
    handleMissingServletRequestParameterException( final MissingServletRequestParameterException exception )
    {

        final StringBuilder detailMessage = new StringBuilder().append( exception.getParameterName() ).append( ", " )
                .append( exception.getMessage() );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST,
                                                                        detailMessage.toString() );

        return new ResponseEntity<>( details, HttpStatus.BAD_REQUEST );
    }




    // ========== MediaType ==========
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
    handleUnsupportedMediaTypeException( final HttpMediaTypeNotSupportedException exception )
    {
        final ProblemDetail details = ProblemDetail.forStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE );
        details.setTitle( "Unsupported Media Type" );
        details.setDetail( exception.getMessage() );
        details.setProperty( "Unsupported content:", exception.getContentType().toString() );
        details.setProperty( "Supported content:",
                             exception.getSupportedMediaTypes()
                                      .stream()
                                      .map( MediaType::toString )
                                      .collect( Collectors.joining(", ") ) );

        return new ResponseEntity<>( details, HttpStatus.UNSUPPORTED_MEDIA_TYPE );
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
    @ResponseStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
    public ResponseEntity<ProblemDetail>
    handleUnacceptableMediaTypeException( final HttpMediaTypeNotAcceptableException exception )
    {
        final ProblemDetail details = ProblemDetail.forStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE );
        details.setTitle( "Unacceptable Media Type" );
        details.setDetail( exception.getMessage() );
        details.setProperty( "Supported content:",
                             exception.getSupportedMediaTypes()
                                      .stream()
                                      .map( MediaType::toString )
                                      .collect( Collectors.joining(", ") ) );

        return new ResponseEntity<>( details, HttpStatus.UNSUPPORTED_MEDIA_TYPE );
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
    public ResponseEntity<ProblemDetail> handleMediaTypeException( final HttpMediaTypeException exception )
    {

//        final ProblemDetail details = ProblemDetail.forStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE );
        final ProblemDetail details = ProblemDetail.forStatus( exception.getStatusCode() );
        details.setTitle( "Bad Media Type" );
//        details.setTitle( "Bad Media Type" );
        details.setDetail( exception.getMessage() );
        details.setProperty( "Supported content:",
                             exception.getSupportedMediaTypes()
                                      .stream()
                                      .map( MediaType::toString )
                                      .collect( Collectors.joining(", ") ) );

        return new ResponseEntity<>( details, exception.getStatusCode() );






        // TODO Message should indicate the bad media type as well as the acceptable types
//        final StringBuilder detailMessage = new StringBuilder( "Unsupported content type: " )
//                .append( MediaType.toString( exception.getSupportedMediaTypes() ) );
//        final ProblemDetail details       = ProblemDetail.forStatusAndDetail( HttpStatus.UNSUPPORTED_MEDIA_TYPE,
//                                                                              detailMessage.toString() );
//        details.setTitle( "Unsupported Media Type" );
//
//        return new ResponseEntity<>( details, HttpStatus.UNSUPPORTED_MEDIA_TYPE );
    }



    // ========== Validation ==========
//    /**
//     * Create a standard error message when the input request body contains invalid or
//     * missing attributes.
//     *
//     * @param exception the intercepted exception
//     *
//     * @return a formatted {@code ProblemDetail}.
//     */
//    @ExceptionHandler( MethodArgumentNotValidException.class )
//    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
//    public ResponseEntity<Map<String, String>> handleRestValidationException2( WebRequest request,
//                                                                        final MethodArgumentNotValidException exception )
//    {
//        Map<String, String> errors = new HashMap<>();
//
//        exception
//                .getBindingResult()
//                .getFieldErrors()
//                .forEach( error -> errors.put( error.getField(), error.getDefaultMessage() ) );
//
//        return ResponseEntity.badRequest().body( errors );
//    }


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
    public ResponseEntity<ProblemDetail> handleRestValidationException( WebRequest request,
                                                                        final MethodArgumentNotValidException exception )
    {
        // getBindingResult()
        // List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        // List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        // List<String> errors = new ArrayList<>(fieldErrors.size() +
        // globalErrors.size());
        // String error;
        // for (FieldError fieldError : fieldErrors) {
        // error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
        // errors.add(error);
        // }
        // for (ObjectError objectError : globalErrors) {
        // error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
        // errors.add(error);
        // }
        // ErrorMessage errorMessage = new ErrorMessage(errors);

        // Object result=ex.getBindingResult();//instead of above can allso pass the
        // more detailed bindingResult
        // return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);

        // Build a formatted string with all the Violations
        final String detailMessage = exception.getBindingResult().getFieldErrors().stream()
                // .sorted( (e1, e2) -> e1.getObjectName().compareTo( e2.getObjectName() ) )
                .map( fieldError -> String.format( "Field: %s.%s, reason: %s, with value: '%s'",
                                                   fieldError.getObjectName(), fieldError.getField(),
                                                   fieldError.getDefaultMessage(), fieldError.getRejectedValue() ) )
                // .getGlobalErrors()
                // .map( globalError -> String.format( "Field: %s.%s, reason: %s, with value:
                // '%s'"
                // , globalError.getObjectName()
                // , globalError.getDefaultMessage() ) )
                .collect( Collectors.joining( ".  \n" ) );

        // exception.getBindingResult() contains fieldErrors ... build a Map of them and add them to
        // ProblemDetails.properties -- Look at FieldError and ObjectError classes....

        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, detailMessage );
        // var y = exception.getBindingResult().getAllErrors().getFirst();
        // var x = exception.getBindingResult();
        // x.getAllErrors().getFirst();
        var globalError = exception.getBindingResult().getGlobalError();
        final String title = globalError == null ? "" : globalError.getDefaultMessage();
        details.setTitle( title );

        return new ResponseEntity<>( details, HttpStatus.BAD_REQUEST );
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
    public ResponseEntity<ProblemDetail> handleConstraintViolations( final ConstraintViolationException exception )
    {
        final Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        final String                      detailMessage        = constraintViolations.stream()
                .map( ConstraintViolation::getMessage ).collect( Collectors.joining( "; " ) );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, detailMessage );
        details.setTitle( "Constraints" );

        return new ResponseEntity<>( details, HttpStatus.BAD_REQUEST );
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
    public ResponseEntity<ProblemDetail> handleMessageNotReadableException( final HttpMessageNotReadableException exception )
    {
        // TODO potentially a problem with the content-type or lack of mapping to/from the
        // requested format and the internal POJO.
        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST,
                                                                        exception.getMessage() );
        details.setTitle( "Malformed Request" );
        details.setProperty( "Possibility 1", "Malformed request body" );
        details.setProperty( "Possibility 2", "Invalid request parameters" );
        details.setProperty( "Possibility 3", "Incompatible data format" );
        details.setProperty( "Possibility 4", "Serialization errors" );

        return new ResponseEntity<>( details, HttpStatus.BAD_REQUEST );
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
    public ResponseEntity<ProblemDetail> handleMessageNotWritableException( final HttpMessageNotWritableException exception )
    {
        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_IMPLEMENTED,
                                                                        exception.getMessage() );
        details.setTitle( "Unable to produce requested response format" );

        return new ResponseEntity<>( details, HttpStatus.NOT_IMPLEMENTED );
    }



    // ========== Unsupported Method ==========
    /**
     * Create a standard error message when an unsupported methot (GET, PUT,
     * DELETE...) is specified.
     *
     * @param exception the intercepted exception
     *
     * @return a formatted {@code ProblemDetail}.
     */
    @ExceptionHandler( HttpRequestMethodNotSupportedException.class )
    @ResponseStatus( HttpStatus.METHOD_NOT_ALLOWED )
    public ResponseEntity<ProblemDetail> handleMethodNotSupportedException( final HttpRequestMethodNotSupportedException exception )
    {
        final ProblemDetail details = exception.getBody();

        details.setProperty( "TraceId: ", UUID.randomUUID() );  // TODO change to pull the traceID from MDC

        return ResponseEntity
                .status( HttpStatus.METHOD_NOT_ALLOWED )
                .header( "Allow", exception.getSupportedMethods() )
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
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException( final NoResourceFoundException exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should

        final ProblemDetail details = exception.getBody();
        details.setProperty( "TraceId: ", UUID.randomUUID() );  // TODO change to pull the traceID from MDC
        details.setInstance( URI.create( exception.getResourcePath() ) );

        return ResponseEntity
                .status( details.getStatus() )
                .location( details.getInstance() )
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
    handleEntityNotFoundException( final EntityNotFoundException exception )
    {
        final StringBuilder detailMessage =
                new StringBuilder()
                        .append( exception.getMessage() );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.GONE,
                                                                        detailMessage.toString() );
        details.setTitle( "Not Found" );

        return new ResponseEntity<>( details, HttpStatus.GONE );
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
    public ResponseEntity<ProblemDetail> handleGenericException( final Exception exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should
        // introduce this.
        // we should not pass our traceId back to the client
        final StringBuilder detailMessage = new StringBuilder( "A problem occurred " )
                .append( "logref=" )
                .append( UUID.randomUUID() )
                .append( '\n' )
                .append( exception.getMessage() )
                .append( '\n' )
                .append( exception.getCause() );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail( HttpStatus.INTERNAL_SERVER_ERROR,
                                                                        detailMessage.toString() );

        log.error( "Generic catch-all: ", exception );
        return new ResponseEntity<>( details, HttpStatus.INTERNAL_SERVER_ERROR );
    }

}
