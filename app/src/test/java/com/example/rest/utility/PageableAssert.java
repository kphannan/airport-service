package com.example.rest.utility;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


/**
 * Helper class to validate the specifics of a page out of a multipage result.
 */
public final class PageableAssert extends AbstractAssert<PageableAssert, Pageable>
{

    private PageableAssert( final Pageable pageable )
    {
        super( pageable, PageableAssert.class );
    }

    /**
     * Begin a fluent validation expression.
     *
     * @param actual the Pageable instance to verify.
     * @return the fluent assertion instance.
     */
    public static PageableAssert assertThat( final Pageable actual )
    {
        return new PageableAssert( actual );
    }

    /**
     * Verify the Page is of the expected size.
     *
     * @param expectedPageSize desired page size.
     * @return the chained PageableAssert.
     */
    public PageableAssert pageSizeMatches( final int expectedPageSize )
    {
        if ( !Objects.equals( actual.getPageSize(), expectedPageSize ) )
        {
            failWithMessage( "expected page size to be <%s> but was <%s>",
                             expectedPageSize, actual.getPageSize() );
        }

        return this;
    }

    /**
     * Verify the page is the expected value.
     *
     * @param expectedPageNumber the expected page.
     * @return the chained PageableAssert.
     */
    public PageableAssert pageNumberMatches( final int expectedPageNumber )
    {
        if ( !Objects.equals( actual.getPageNumber(), expectedPageNumber ) )
        {
            failWithMessage( "expected page number to be <%s> but was <%s>",
                             expectedPageNumber, actual.getPageNumber() );
        }

        return this;
    }

    /**
     * Verify that the Sort option if present specifies the attribute and collation order.
     *
     * @param field name of the attribute sorted on, optional.
     * @param direction sort diirection, ASC or DESC, required.
     * @return the chained PageableAssert.
     */
    public PageableAssert sortCriteriaMatches( final String field, final Sort.Direction direction )
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
