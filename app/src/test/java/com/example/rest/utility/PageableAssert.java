package com.example.rest.utility;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableAssert extends AbstractAssert<PageableAssert, Pageable>
{

    PageableAssert( final Pageable pageable )
    {
        super( pageable, PageableAssert.class );
    }

    public static PageableAssert assertThat( final Pageable actual )
    {
        return new PageableAssert( actual );
    }

    public PageableAssert hasPageSize( int expectedPageSize )
    {
        if ( !Objects.equals( actual.getPageSize(), expectedPageSize ) )
        {
            failWithMessage( "expected page size to be <%s> but was <%s>",
                             expectedPageSize, actual.getPageSize() );
        }

        return this;
    }

    public PageableAssert hasPageNumber( final int expectedPageNumber )
    {
        if ( !Objects.equals( actual.getPageNumber(), expectedPageNumber ) )
        {
            failWithMessage( "expected page number to be <%s> but was <%s>",
                             expectedPageNumber, actual.getPageNumber() );
        }

        return this;
    }

    public PageableAssert hasSort( final String field, final Sort.Direction direction )
    {
        final Sort.Order actualOrder = actual.getSort().getOrderFor( field );

        if ( actualOrder == null )
        {
            failWithMessage( "expected sort for field <%s> to be <%s> but was null",
                             field, direction );
        }
        else if ( actualOrder.getDirection() != direction )
        {
            failWithMessage( "expected sort for field <%s> to be <%s> but was <%s>",
                             field, direction, actualOrder.getDirection() );
        }

        return this;
    }
}
