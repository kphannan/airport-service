package com.example.airline.location.continent.model;


import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;

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

    public static ContinentTester of( Continent  continent )
    {
        return new ContinentTester( continent, ContinentTester.class );
    }


    @Override
    public ContinentTester assertThat()
    {
        return new ContinentTester( continent, ContinentTester.class );
    }



    // Delegating matchers
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
    public ContinentTester hasCode( String  code )
    {
        assertThat()
            .isNotNull()
            .code()
            .describedAs( "code" )
            .isEqualTo( code );

        return myself;
    }

    public ContinentTester hasName( String  name )
    {
        assertThat()
            .isNotNull()
            .name()
            .describedAs( "name" )
            .isEqualTo( name );

        return myself;
    }

    public ContinentTester hasWikiLink( String  name )
    {
        assertThat()
            .isNotNull()
            .wikiLink()
            .describedAs( "wikiLink" )
            .isEqualTo( name );

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

    public ContinentTester hasKeywords( String  keywords )
    {
        assertThat()
            .isNotNull()
            .keywords()
            .describedAs( "keywords" )
            .isEqualTo( keywords );

        return myself;
    }
}
