package com.example.rest.utility;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.assertj.core.api.AbstractCollectionAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractMapAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.ProblemDetailJacksonMixin;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author kevin
 * @since 2026-06-14
 *     <p>
 *     Copyright (c) 2020-2026
 */
public class ProblemDetailTester extends AbstractObjectAssert<ProblemDetailTester, ProblemDetail> implements AssertProvider<ProblemDetailTester>
{
    private final ProblemDetail problemDetail;

    private ProblemDetailTester( ProblemDetail problemDetail, Class<?> selfType )
    {
        super( problemDetail, selfType );
        this.problemDetail = problemDetail;
    }

    public static ProblemDetailTester of( ProblemDetail  continent )
    {
        return new ProblemDetailTester( continent, ProblemDetailTester.class );
    }

    public static ProblemDetailTester of( String json )
    {
        if ( StringUtils.isEmpty( json ) )
        {
            return of( (ProblemDetail)null );
        }

        final ObjectMapper mapper = new ObjectMapper();
        // mapper.findAndRegisterModules();
        // mapper.addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class );
        final ProblemDetail ccc = mapper.readValue(  json, ProblemDetail.class );
        // https://www.baeldung.com/members/courses/learn-json-with-jackson/lessons/lesson-3-handling-unknown-properties
        // TODO need custom mapper to take unknown attributes and put them in 'properties' map
        return of( ccc );
    }


    @Override
    public ProblemDetailTester assertThat()
    {
        return new ProblemDetailTester( problemDetail, ProblemDetailTester.class );
    }



    // Delegating matchers
    public AbstractUriAssert<?> type()
    {
        return Assertions.assertThat( problemDetail.getType() )
                         .describedAs( "type" );
    }

    public AbstractStringAssert<?> title()
    {
        return Assertions.assertThat( problemDetail.getTitle() )
                         .describedAs( "title" );
    }

    public AbstractIntegerAssert<?> status()
    {
        return Assertions.assertThat( problemDetail.getStatus() )
                         .describedAs( "status" );
    }

    public AbstractStringAssert<?> detail()
    {
        return Assertions.assertThat( problemDetail.getDetail() )
                         .describedAs( "detail" );
    }

    public AbstractUriAssert<?> instance()
    {
        return Assertions.assertThat( problemDetail.getInstance() )
                         .describedAs( "instance" );
    }

    // SELF, ACTUAL, K, V
    public AbstractMapAssert<?, ?, String, Object> properties()
    {
        return Assertions.assertThat( problemDetail.getProperties() )
                         .describedAs( "properties" );
    }


    // Exact matches
    public ProblemDetailTester hasType( String value )
    {
        assertThat()
            .isNotNull()
            .type()
            .describedAs( "type" )
            .isEqualTo( value );

        return myself;
    }

    public ProblemDetailTester blankType()
    {
        assertThat()
            .isNotNull()
            .type()
            .describedAs( "blank type" )
            .isNull();

        return myself;
    }


    public ProblemDetailTester hasTitle( String value )
    {
        assertThat()
            .isNotNull()
            .title()
            .describedAs( "title" )
            .isEqualTo( value );

        return myself;
    }

    public ProblemDetailTester blankTitle()
    {
        assertThat()
            .isNotNull()
            .title()
            .describedAs( "blank title" )
            .isNull();

        return myself;
    }

    public ProblemDetailTester hasStatus( HttpStatus value )
    {
        return hasStatus( value.value() );
    }

    public ProblemDetailTester hasStatus( Integer value )
    {
        assertThat()
            .isNotNull()
            .status()
            .describedAs( "status" )
            .isEqualTo( value );

        return myself;
    }


    public ProblemDetailTester hasDetail( String value )
    {
        assertThat()
            .isNotNull()
            .detail()
            .describedAs( "detail" )
            .isEqualTo( value );

        return myself;
    }

    public ProblemDetailTester blankDetail()
    {
        assertThat()
            .isNotNull()
            .detail()
            .describedAs( "blank detail" )
            .isNull();

        return myself;
    }

    public ProblemDetailTester hasInstance( String value )
    {
        // final URI location = URI.create( value  )
        assertThat()
            .isNotNull()
            .instance()
            .describedAs( "instance" )
            .isEqualTo( URI.create( value ) );

        return myself;
    }

    public ProblemDetailTester blankInstance()
    {
        assertThat()
            .isNotNull()
            .instance()
            .describedAs( "blank instance" )
            .isNull();

        return myself;
    }

    // TODO value should be Map<String, String>
    public ProblemDetailTester hasProperties( Map.Entry<? extends String, ? extends Object>...  entries )
    {
        assertThat()
            .isNotNull()
            .properties()
            .describedAs( "properties" );
        // TODO implement containsOnly after mapping JSON into properties collection
            // .containsOnly( entries );

        return myself;
    }

    public ProblemDetailTester blankProperties()
    {
        assertThat()
            .isNotNull()
            .properties()
            .describedAs( "blank properties" )
            .isNull();

        return myself;
    }

    // TODO  implement 'satisfying'
    public ProblemDetailTester hasPropertiesSatisfying( Map.Entry<? extends String, ? extends Object>...  entries )
    {
        return myself;
    }

        // public final ProblemDetailTester containsOnly( Map.Entry<String, String>...  entries )
    // {
    //     return myself;
    // }
    // public final ProblemDetailTester containsOnly( Map.Entry<? extends String, ? extends String>... entries )
    // {
    //     // return containsOnlyForProxy(entries);
    // }







}
