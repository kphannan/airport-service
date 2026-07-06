package com.example.airline.location.country.model;


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
public class CountryTester extends AbstractObjectAssert<CountryTester, Country> implements AssertProvider<CountryTester>
{
    private final Country country;

    private CountryTester( Country country, Class<?> selfType )
    {
        super( country, selfType );
        this.country = country;
    }

    public static CountryTester of( Country continent )
    {
        return new CountryTester( continent, CountryTester.class );
    }


    @Override
    public CountryTester assertThat()
    {
        return new CountryTester( country, CountryTester.class );
    }


    // Delegating matchers
    public AbstractStringAssert<?> code()
    {
        return Assertions.assertThat( country.code() )
                   .describedAs( "code" );
    }

    public AbstractStringAssert<?> name()
    {
        return Assertions.assertThat( country.name() )
                         .describedAs( "name" );
    }

    public AbstractUriAssert<?> wikiLink()
    {
        return Assertions.assertThat( country.wikipediaLink() )
                         .describedAs( "wikipediaLink" );
    }

    public AbstractStringAssert<?> keywords()
    {
        return Assertions.assertThat( country.keywords() )
                         .describedAs( "keyword" );
    }


    // Exact matches
    public CountryTester hasCode( String  code )
    {
        assertThat()
            .isNotNull()
            .code()
            .describedAs( "code" )
            .isEqualTo( code );

        return myself;
    }

    public CountryTester hasName( String name )
    {
        assertThat()
            .isNotNull()
            .name()
            .describedAs( "name" )
            .isEqualTo( name );

        return myself;
    }

    public CountryTester hasWikiLink( String name )
    {
        assertThat()
            .isNotNull()
            .wikiLink()
            .describedAs( "wikiLink" )
            .isEqualTo( name );

        return myself;
    }

    public CountryTester blankWikiLink()
    {
        assertThat()
            .isNotNull()
            .wikiLink()
            .describedAs( "wikiLink" )
            .isNull();

        return myself;
    }

    public CountryTester blankKeywords()
    {
        assertThat()
            .isNotNull()
            .keywords()
            .describedAs( "keywords" )
            .isBlank();
            // .isNull();

        return myself;
    }

    public CountryTester hasKeywords( String keywords )
    {
        assertThat()
            .isNotNull()
            .keywords()
            .describedAs( "keywords" )
            .isEqualTo( keywords );

        return myself;
    }
}
