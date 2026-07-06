package com.example.airline.location.continent.model;


import com.example.airline.location.continent.ContinentDTO;
import org.assertj.core.api.AbstractIntegerAssert;
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
public class ContinentDTOTester extends AbstractObjectAssert<ContinentDTOTester, ContinentDTO> implements AssertProvider<ContinentDTOTester>
{
    private final ContinentDTO continent;

    private ContinentDTOTester( ContinentDTO continent, Class<?> selfType )
    {
        super( continent, selfType );
        this.continent = continent;
    }

    public static ContinentDTOTester of( ContinentDTO continent )
    {
        return new ContinentDTOTester( continent, ContinentDTOTester.class );
    }


    @Override
    public ContinentDTOTester assertThat()
    {
        return new ContinentDTOTester( continent, ContinentDTOTester.class );
    }


    // Delegating matchers
    public AbstractIntegerAssert<?> id()
    {
        return Assertions.assertThat( continent.id() )
                         .describedAs( "id" );
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
    public ContinentDTOTester hasId( Integer id )
    {
        assertThat()
            .isNotNull()
            .id()
            .describedAs( "id" )
            .isEqualTo( id );

        return myself;
    }

    public ContinentDTOTester hasCode( String code )
    {
        assertThat()
            .isNotNull()
            .code()
            .describedAs( "code" )
            .isEqualTo( code );

        return myself;
    }

    public ContinentDTOTester hasName( String name )
    {
        assertThat()
            .isNotNull()
            .name()
            .describedAs( "name" )
            .isEqualTo( name );

        return myself;
    }

    public ContinentDTOTester hasWikiLink( String name )
    {
        assertThat()
            .isNotNull()
            .wikiLink()
            .describedAs( "wikiLink" )
            .isEqualTo( name );

        return myself;
    }

    public ContinentDTOTester blankWikiLink()
    {
        assertThat()
            .isNotNull()
            .wikiLink()
            .describedAs( "wikiLink" )
            .isNull();

        return myself;
    }

    public ContinentDTOTester blankKeywords()
    {
        assertThat()
            .isNotNull()
            .keywords()
            .describedAs( "keywords" )
            .isBlank();
            // .isNull();

        return myself;
    }

    public ContinentDTOTester hasKeywords( String keywords )
    {
        assertThat()
            .isNotNull()
            .keywords()
            .describedAs( "keywords" )
            .isEqualTo( keywords );

        return myself;
    }
}
