package com.example.airline.location.continent.model;


import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author kevin
 * @since 2026-06-14
 *     <p>
 *     Copyright (c) 2020-2026
 */
public class ContinentTester extends AbstractObjectAssert<ContinentTester, Continent> implements AssertProvider<ContinentTester>
{
    private final Continent continent;

    private ContinentTester( Continent continent, Class<?> selfType )
    {
        super( continent, selfType );
        this.continent = continent;
    }

    public static ContinentTester of( Continent continent )
    {
        return new ContinentTester( continent, ContinentTester.class );
    }

    public static ContinentTester of( String json )
    {
        final ObjectMapper mapper = new ObjectMapper();
        final Continent ccc = mapper.readValue( json, Continent.class );

        return of( ccc );
    }


    @Override
    public ContinentTester assertThat()
    {
        return new ContinentTester( continent, ContinentTester.class );
    }


    // Delegating matchers
    public AbstractIntegerAssert<?> id()
    {
        return Assertions.assertThat( continent.id() )
                         .describedAs( "persistence key" );
    }

    public AbstractStringAssert<?> code()
    {
        return Assertions.assertThat( continent.code() )
                   .describedAs( "code" );
    }

    public AbstractStringAssert<?> name()
    {
        return Assertions.assertThat( continent.name() )
                         .describedAs( "name" );
    }

    public AbstractUriAssert<?> wikiLink()
    {
        return Assertions.assertThat( continent.wikiLink() )
                         .describedAs( "wikiLink" );
    }

    public AbstractStringAssert<?> keywords()
    {
        return Assertions.assertThat( continent.keywords() )
                         .describedAs( "keyword" );
    }


    // Exact matches
    public ContinentTester hasId( Integer value )
    {
        assertThat()
            .isNotNull()
            .id()
            .describedAs( "persistence  key" )
            .isEqualTo( value );

        return myself;
    }

    public ContinentTester hasCode( String value )
    {
        assertThat()
            .isNotNull()
            .code()
            .describedAs( "code" )
            .isEqualTo( value );

        return myself;
    }

    public ContinentTester hasName( String value )
    {
        assertThat()
            .isNotNull()
            .name()
            .describedAs( "name" )
            .isEqualTo( value );

        return myself;
    }

    public ContinentTester hasWikiLink( String value )
    {
        assertThat()
            .isNotNull()
            .wikiLink()
            .describedAs( "wikiLink" )
            .isEqualTo( value );

        return myself;
    }

    public ContinentTester blankWikiLink()
    {
        assertThat()
            .isNotNull()
            .wikiLink()
            .describedAs( "wikiLink" )
            .isNull();

        return myself;
    }

    public ContinentTester blankKeywords()
    {
        assertThat()
            .isNotNull()
            .keywords()
            .describedAs( "keywords" )
            .isBlank();
            // .isNull();

        return myself;
    }

    public ContinentTester hasKeywords( String value )
    {
        assertThat()
            .isNotNull()
            .keywords()
            .describedAs( "keywords" )
            .isEqualTo( value );

        return myself;
    }
}
