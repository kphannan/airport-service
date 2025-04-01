package com.example.rest.exception;

import java.net.URI;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Exception handler to catch application specific exceptions and format them in a consistent manner.
 */
@ControllerAdvice
// @RestContollerAdvice
@Log4j2
public class GlobalExceptionHandler
{


    @ExceptionHandler( MissingServletRequestParameterException.class )
    @ResponseStatus( code = HttpStatus.BAD_REQUEST )
    public ResponseEntity<ProblemDetail> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex
            ) {

        final StringBuilder detailMessage = new StringBuilder()
                .append( ex.getParameterName() )
                .append( ", " )
                .append( ex.getMessage() );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detailMessage.toString() );

        return ResponseEntity
                .internalServerError()
                .body( details );
    }



    @ExceptionHandler( HttpMediaTypeNotSupportedException.class )
    @ResponseStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
    public ResponseEntity<ProblemDetail> handleUnsupportedMediaTypeException( final HttpMediaTypeNotSupportedException exception )
    {
        final StringBuilder detailMessage =
            new StringBuilder( "Unsupported content type: " )
                .append( exception.getContentType() )
                // .append( "\n" )
                .append( "; Supported content types: " )
                .append( MediaType.toString( exception.getSupportedMediaTypes() ) );
        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, detailMessage.toString() );
        details.setTitle( "Unsupported Media Type");

        return ResponseEntity
                .status( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
                .body( details );
    }



    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity<ProblemDetail> handleRestValidationException(  WebRequest request
                                                                        , final MethodArgumentNotValidException exception )
    {
        // List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        // List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        // List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
        // String error;
        // for (FieldError fieldError : fieldErrors) {
        // 	error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
        // 	errors.add(error);
        // }
        // for (ObjectError objectError : globalErrors) {
        // 	error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
        // 	errors.add(error);
        // }
        // ErrorMessage errorMessage = new ErrorMessage(errors);

        //Object result=ex.getBindingResult();//instead of above can allso pass the more detailed bindingResult
        // return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);

        // Build a formatted string with all the Violations
        final String detailMessage = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            // .sorted( (e1, e2) -> e1.getObjectName().compareTo( e2.getObjectName() ) )
            .map( fieldError -> String.format(  "Field: %s.%s, reason: %s, with value: '%s'"
                                                , fieldError.getObjectName()
                                                , fieldError.getField()
                                                , fieldError.getDefaultMessage()
                                                , fieldError.getRejectedValue()  ) )
            // .getGlobalErrors()
            // .map( globalError -> String.format(  "Field: %s.%s, reason: %s, with value: '%s'"
            //                                     , globalError.getObjectName()
            //                                     , globalError.getDefaultMessage() ) )
            .collect( Collectors.joining( ".  \n" ) );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detailMessage.toString() );
        details.setTitle( "RequestBody error" );

        return ResponseEntity
                .badRequest()
                .body( details );
    }



    @ExceptionHandler( ConstraintViolationException.class )
    public ResponseEntity<ProblemDetail> handleConstraintViolations( final ConstraintViolationException exception )
    {

        // List<String> errors = new ArrayList<>(constraintViolations.size() );
        // String error;
        // for (ConstraintViolation constraintViolation : constraintViolations) {
        //     error =  constraintViolation.getMessage();
        //     errors.add(error);
        // }
        // ErrorMessage errorMessage = new ErrorMessage(errors);
        // return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        final String detailMessage =
            // exception
            // .getConstraintViolations()
            constraintViolations
            .stream()
            .map( ConstraintViolation::getMessage )
            .collect( Collectors.joining( "; " ) );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detailMessage.toString() );
        details.setTitle( "Constraints" );

        return ResponseEntity
                .badRequest()
                .body( details );
    }




    @ExceptionHandler( HttpMessageNotReadableException.class )
    public ResponseEntity<ProblemDetail> handleMessageNotReadableException( final HttpMessageNotReadableException exception )
    {
        // Throwable mostSpecificCause = ex.getMostSpecificCause();
        // ErrorMessage errorMessage;
        // if (mostSpecificCause != null) {
        //     String exceptionName = mostSpecificCause.getClass().getName();
        //     String message = mostSpecificCause.getMessage();
        //     errorMessage = new ErrorMessage(exceptionName, message);
        // } else {
        //     errorMessage = new ErrorMessage(ex.getMessage());
        // }
        // return new ResponseEntity(errorMessage,  HttpStatus.BAD_REQUEST);

        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage() );

        return ResponseEntity
                .badRequest()
                .body( details );
    }


    @ExceptionHandler( HttpRequestMethodNotSupportedException.class )
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ProblemDetail> handleMethodNotSupportedException( final HttpRequestMethodNotSupportedException exception )
    {
        final StringBuilder detailMessage = new StringBuilder()
                .append( exception.getMessage() )
                .append( "; Supported methods: " )
                .append( String.join( ", ", exception.getSupportedMethods() ) );


        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, detailMessage.toString() );
        // final ProblemDetail details = ProblemDetail
        //         .builder()
        //         .status( HttpStatus.METHOD_NOT_ALLOWED )
        //         .detail( detailMessage.toString() )
        //         .build();

        return ResponseEntity
                .status( HttpStatus.METHOD_NOT_ALLOWED )
                .header( "Allow", exception.getSupportedMethods() )
                .body( details );
    }


    @ExceptionHandler( NoResourceFoundException.class )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException( final NoResourceFoundException exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should introduce this.
        //      we should not pass our traceId back to the client
        // final StringBuilder detailMessage = new
        // StringBuilder( "Resource Not Found ")
        //     .append( "logref=" )
        //     .append( UUID.randomUUID() )
        //     .append( "\n" )
        //     .append( exception.getMessage() );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage() );
        details.setTitle( "Resource Not Found" );
        // details.setTitle(exception.getClass().getSimpleName());
        details.setInstance(URI.create(exception.getResourcePath()));

        return ResponseEntity
            .status( details.getStatus()  )
            .location( details.getInstance() )
            .body( details );
    }



    @ExceptionHandler( Exception.class )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ProblemDetail> handleGenericException( final Exception exception )
    {
        // TODO the MDC should include the traceId (UUID) and log pattern should introduce this.
        //      we should not pass our traceId back to the client
        final StringBuilder detailMessage = new
            StringBuilder( "A problem occurred ")
            .append( "logref=" )
            .append( UUID.randomUUID() )
            .append( "\n" )
            .append( exception.getMessage() );

        final ProblemDetail details = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, detailMessage.toString() );

        return ResponseEntity
            .internalServerError()
            .body( details );
    }

}

