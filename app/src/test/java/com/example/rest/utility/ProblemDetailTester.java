package com.example.rest.utility;


import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractMapAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.StringUtils;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author kevin
 * @since 2026-06-14
 *     <p>
 *     Copyright (c) 2020-2026
 */
public final class ProblemDetailTester extends AbstractObjectAssert<ProblemDetailTester, ProblemDetail> implements AssertProvider<ProblemDetailTester>
{
    private final ProblemDetail problemDetail;

    private ProblemDetailTester( final ProblemDetail problemDetail, final Class<?> selfType )
    {
        super( problemDetail, selfType );
        this.problemDetail = problemDetail;
    }

    public static ProblemDetailTester of( final ProblemDetail continent )
    {
        return new ProblemDetailTester( continent, ProblemDetailTester.class );
    }

    public static ProblemDetailTester of( final String json )
    {
        if ( !StringUtils.hasText( json ) )
        {
            return of( (ProblemDetail)null );
        }

        final ObjectMapper mapper = new ObjectMapper();
        // mapper.findAndRegisterModules();
        // mapper.addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class );
        final ProblemDetail ccc = mapper.readValue( json, ProblemDetailProperties.class );
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
    public ProblemDetailTester hasType( final String value )
    {
        assertThat()
            .isNotNull()
            .type()
            .describedAs( "type" )
            .isEqualTo( value );

        return myself;
    }

    /**
     * Assert the type attribute is null.
     *
     * @return self to allow chaining.
     */
    public ProblemDetailTester blankType()
    {
        assertThat()
            .isNotNull()
            .type()
            .describedAs( "blank type" )
            .isNull();

        return myself;
    }


    /**
     * Assert the title attribute contains a specific value.
     *
     * @param value the desired title value.
     *
     * @return self to allow chaining.
     */
    public ProblemDetailTester hasTitle( final String value )
    {
        assertThat()
            .isNotNull()
            .title()
            .describedAs( "title" )
            .isEqualTo( value );

        return myself;
    }

    /**
     * Assert the title attribute is blank.
     *
     * @return self to allow chaining.
     */
    public ProblemDetailTester blankTitle()
    {
        assertThat()
            .isNotNull()
            .title()
            .describedAs( "blank title" )
            .isNull();

        return myself;
    }

    /**
     * Assert the status attribute contains a specific value.
     *
     * @param value the desired value.
     *
     * @return self to allow chaining.
     */
    public ProblemDetailTester hasStatus( final HttpStatus value )
    {
        return hasStatus( value.value() );
    }

    /**
     * Assert the status attribute contains a specific value.
     *
     * @param value the desired value.
     *
     * @return self to allow chaining.
     */
    public ProblemDetailTester hasStatus( final Integer value )
    {
        assertThat()
            .isNotNull()
            .status()
            .describedAs( "status" )
            .isEqualTo( value );

        return myself;
    }


    public ProblemDetailTester hasDetail( final String value )
    {
        assertThat()
            .isNotNull()
            .detail()
            .describedAs( "detail" )
            .isEqualTo( value );

        return myself;
    }

    /**
     * Assert the detail attribute is null.
     *
     * @return self to allow chaining
     */
    public ProblemDetailTester blankDetail()
    {
        assertThat()
            .isNotNull()
            .detail()
            .describedAs( "blank detail" )
            .isNull();

        return myself;
    }

    /**
     * Asser the instance attribute exists and has a specific value.
     *
     * @param value the desired value of the instance.
     *
     * @return self for chaining.
     */
    public ProblemDetailTester hasInstance( final String value )
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
    @SafeVarargs
    public final ProblemDetailTester hasProperties( final Map.Entry<? extends String, ?>... entries )
    {
        final Map<? extends String, ?> expected = Map.ofEntries( entries );
        assertThat()
            .isNotNull()
            .properties()
            .describedAs( "properties" )
            // TODO implement containsOnly after mapping JSON into properties collection
            .containsAllEntriesOf( expected );

        return myself;
    }

    /**
     * Assert there are no additional properties.
     *
     * @return self to allow chaining.
     */
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
    // @SafeVarargs
    // public final ProblemDetailTester hasPropertiesSatisfying( Map.Entry<? extends String, ? extends Object>...  entries )
    // {
    //     // TODO implement this method
    //     assertThat()
    //         .isNotNull()
    //         .properties()
    //         .describedAs( "satisfies" )
    //         // .
    //         .hasEntrySatisfying( entries );
    //
    //     return myself;
    // }

    public ProblemDetailTester hasPropertiesSatisfying( final String key,
                                                              final Consumer<? super Object> valueRequirements )
    {
        // TODO implement this method
        assertThat()
            .isNotNull()
            .properties()
            .describedAs( "satisfies" )
            .hasEntrySatisfying( key, valueRequirements );

        return myself;
    }

    /**
     * Fluent method to assert a named item is a property.
     *
     * @param key the named item to verify.
     * @return self to allow chaining.
     */
    public ProblemDetailTester containsKey( final String key )
    {
        assertThat()
            .isNotNull()
            .properties()
            .describedAs( "contains key" )
            .containsKey( key );

        return myself;
    }

    /**
     * Fluent method to assert a named item is not a property.
     *
     * @param key the named item to verify.
     * @return self to allow chaining.
     */
    public ProblemDetailTester doesNotContainKey( final String key )
    {
        assertThat()
            .isNotNull()
            .properties()
            .describedAs( "does not contain key" )
            .doesNotContainKey( key );

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


/**
 * Derived class of ProblemDetail needed when parsing JSON to catch unknown properties
 * and add them to the list of properties contained in the ProblemDetail, otherwise the
 * extra values are ignored.
 */
class ProblemDetailProperties extends ProblemDetail
{
    @JsonAnySetter
    void addUnknownAsProperty( final String key, final Object value )
    {
        setProperty( key, value );
    }
}
