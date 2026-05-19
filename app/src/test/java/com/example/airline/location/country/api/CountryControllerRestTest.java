package com.example.airline.location.country.api;


import static com.example.rest.utility.HeaderTestingSupport.withHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.example.airline.location.country.persistence.model.CountryEntity;
import com.example.airline.location.country.persistence.repository.CountryRepository;
import com.example.rest.utility.PageableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;


/**
 * Tests of REST controller for Country.
 */
@DisplayName( "Country: API (/country)" )
@WebMvcTest( controllers = CountryController.class )
@ComponentScan( basePackages = { "com.example.airline.location.country" } )
@AutoConfigureMockMvc( addFilters = false )
class CountryControllerRestTest //extends RestControllerTestBase
{
    @Autowired
    protected MockMvc mvc;
    @MockitoBean
    protected CountryRepository repository;
    // @Autowired
    // private CountryService service;
    // @Autowired
    // private DtoMapper mapper;


    // @BeforeEach
    // void init()
    // {
    //    mvc = MockMvcBuilders.standaloneSetup( service )
    //                         .setCustomArgumentResolvers( new PageableHandlerMethodArgumentResolver() )
    //                         // .setControllerAdvice(new SuperHeroExceptionHandler())
    //                         // .addFilters(new SuperHeroFilter())
    //                         .build();
    // }



    // ========== CREATE ==========
    // ===== POST =====

    // ========== READ ==========
    // ===== GET =====

    // ========== UPDATE ==========
    // ===== PATCH =====
    // ===== PUT =====

    // ========== DELETE ==========
    // ===== DELETE =====

    // ========== Administrative ==========
    // ===== HEAD =====
    // ===== INFO =====
    // ===== OPTION =====
    // ===== TRACE =====






    // ========== CREATE ==========
    // ===== POST =====
    /**
     * Tests for Http POST method.
     */
    @Nested
    @DisplayName( "HTTP POST" )
    class PostMethod            // NOPMD
    {
    }


    // ========== READ ==========
    // ===== GET =====
    /**
     * Tests for Http GET method.
     */
    @Nested
    @DisplayName( "HTTP GET" )
    class GetMethod
    {
        @Test
        void restGetById_withValidId_returnsItem() throws Exception
        {
            // --- given
            final CountryEntity  countryEntity = new CountryEntity( 1, "XXX", "::NAME::", "AS", null, null );
            final RequestBuilder request       = withHeaders( get( "/location/country/{id}", 1 ) );

            when( repository.findById( any() ) )
                .thenReturn( Optional.of( countryEntity ) );


            // --- when
            final MvcResult result = mvc
                .perform( request )
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                //      don't complain about lack of assertions in tests
                .andExpect( jsonPath( "$.id" ).value( 1 ) )
                .andExpect( jsonPath( "$.code" ).value( "XXX" ) )
                .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                .andExpect( jsonPath( "$.continent" ).value( "AS" ) )
                .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            // TODO need to assert the resulting JSON....

            assertThat( response.getContentType() )
                .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }

        @Test
        void restGetById_withBadId_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/country/{id}", 99 ) );

            when( repository.findById( anyInt() ) )
                .thenReturn( Optional.empty() );


            // --- when
            final MvcResult result = mvc
                .perform( request )
                .andDo( print() )
                .andExpect( status().isNoContent() )
                .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            assertThat( response.getStatus() )
                .isEqualTo( HttpStatus.NO_CONTENT.value() );
            // assertThat( response.getContentType() )
            //     .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }


        @Test
        void restGetByCode_withValidCode_returnsItem() throws Exception
        {
            // --- given
            final CountryEntity  countryEntity = new CountryEntity( 2, "XXX", "::NAME::", "NA", null, null );
            final RequestBuilder request       = withHeaders( get( "/location/country/code/{code}", 1 ) );

            when( repository.findByCode( anyString() ) )
                .thenReturn( Optional.of( countryEntity ) );


            // --- when
            final MvcResult result = mvc
                .perform( request )
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                //      don't complain about lack of assertions in tests
                .andExpect( jsonPath( "$.id" ).value( 2 ) )
                .andExpect( jsonPath( "$.code" ).value( "XXX" ) )
                .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            // TODO need to assert the resulting JSON....

            assertThat( response.getContentType() )
                .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }

        @Test
        void restGetByCode_withBadCode_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/country/code/{code}", "ZZ" ) );

            when( repository.findByCode( anyString() ) )
                .thenReturn( Optional.empty() );

            // --- when
            final MvcResult result = mvc
                .perform( request )
                .andDo( print() )
                .andExpect( status().isNoContent() )
                // .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            assertThat( response.getStatus() )
                .isEqualTo( HttpStatus.NO_CONTENT.value() );
        }


        @Test
        void restGetAll_returnsSuccess() throws Exception
        {
            // --- given
            // ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
            final List<CountryEntity> entities =
                List.of(
                    new CountryEntity( 1, "XXX", "::X_NAME_X::", "AS", null, null ),
                    new CountryEntity( 2, "YYY", "::Y_NAME_Y::", "AS", null, null ),
                    new CountryEntity( 3, "ZZZ", "::Z_NAME_Z::", "AS", null, null )
                       );
            final RequestBuilder request = withHeaders( get( "/location/country" ) )
                .param( "page", "5" )
                .param( "size", "10" )
                .param( "sort", "id,desc" )    // <-- no space after comma!
                .param( "sort", "name,asc" );  // <-- no space after comma!

            final Page<CountryEntity> page = new PageImpl<>( entities );
            when( repository.findAll( any( Pageable.class ) ) )
                .thenReturn( page );

            // --- when
            final MvcResult result = mvc
                .perform( request )
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                //      don't complain about lack of assertions in tests
                .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                .andExpect( jsonPath( "$.content[0].code" ).value( "XXX" ) )
                .andExpect( jsonPath( "$.content[0].name" ).value( "::X_NAME_X::" ) )
                .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() )
                .andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            // TODO need to assert the resulting JSON....
            final ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass( Pageable.class );
            verify( repository ).findAll( pageableCaptor.capture() );
            final PageRequest pageable = (PageRequest)pageableCaptor.getValue();


            PageableAssert
                .assertThat( pageable )
                .pageNumberMatches( 5 )
                .pageSizeMatches( 10 )
                .sortCriteriaMatches( "name", Sort.Direction.ASC )
                .sortCriteriaMatches( "id", Sort.Direction.DESC );

            assertThat( response.getContentType() )
                .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }
    }

    // --- Single ---
    // --- Multiple ---

    // ========== UPDATE ==========
    // ===== PATCH =====
    /**
     * Tests for Http PATCH method.
     */
    @Nested
    @DisplayName( "HTTP PATCH" )
    class PatchMethod           // NOPMD
    {
    }

    // ===== PUT =====
    /**
     * Tests for Http PUT method.
     */
    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod             // NOPMD
    {
    }


    // ========== DELETE ==========
    // ===== DELETE =====
    /**
     * Tests for Http DELETE method.
     */
    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod          // NOPMD
    {
    }


    // ========== Administrative ==========
    // ===== HEAD =====
    /**
     * Tests for Http HEAD method.
     */
    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod            // NOPMD
    {
    }

    // ===== INFO =====
    /**
     * Tests for Http INFO method.
     */
    @Nested
    @DisplayName( "HTTP INFO" )
    class InfoMethod            // NOPMD
    {
    }

    // ===== OPTION =====
    /**
     * Tests for Http OPTIONS method.
     */
    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod         // NOPMD
    {
    }

    // ===== TRACE =====

}
