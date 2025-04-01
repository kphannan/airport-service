
package com.example.rest.exception;

import java.net.URI;

import org.springframework.http.HttpStatus;


/**
 * A standard response body representing details of an error.
 *
 * See RFC-7807 from ITEF.
 */
record ProblemDetail( URI type, String title, HttpStatus status, String detail, URI instance )
{
    public static Builder builder() { return new Builder(); }

    public static class Builder
    {
        private URI type;
        private String title;
        private HttpStatus status;
        private String detail;
        private URI instance;


        // public Builder builder()
        // {
        //     return new Builder();
        // }

        public Builder type( URI type )
        {
            this.type = type;
            return this;
        }

        public Builder title( String value )
        {
            this.title = value;

            return this;
        }

        public Builder status( HttpStatus status )
        {
            this.status = status;

            return this;
        }

        public Builder detail( String detail )
        {
            this.detail = detail;

            return this;
        }

        public Builder instance( URI instance )
        {
            this.instance = instance;

            return this;
        }

        public ProblemDetail build()
        {
            return new ProblemDetail( type, title, status, detail, instance );
        }
    }
}


