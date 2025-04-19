package com.example.airline.location.country.api;


import static com.example.rest.utility.HeaderUtility.withHeaders;
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

import com.example.airline.location.country.mapper.CountryMapper;
import com.example.airline.location.country.persistence.repository.CountryRepository;
import com.example.airline.location.country.service.CountryService;
import com.example.airline.location.persistence.model.location.CountryEntity;
import com.example.rest.utility.PageableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest( controllers = CountryController.class )
@ComponentScan( basePackages = { "com.example.airline.location.country" } )
class CountryControllerRestTest //extends RestControllerTestBase
{
    @Autowired
    protected MockMvc mvc;

    @Autowired
    private CountryService service;

    @MockitoBean
    protected CountryRepository repository;

    @Autowired
    private CountryMapper mapper;


//    @BeforeEach
//    void setup()
//    {
//        mvc = MockMvcBuilders.standaloneSetup( service )
//                             .setCustomArgumentResolvers( new PageableHandlerMethodArgumentResolver() )
//                             // .setControllerAdvice(new SuperHeroExceptionHandler())
//                             // .addFilters(new SuperHeroFilter())
//                             .build();
//    }

    @Nested
    @DisplayName( "/country - HTTP GET")
    class Get
    {
        @Test
        void restGetById_withValidId_returnsItem() throws Exception
        {
            // --- given
            final CountryEntity countryEntity = new CountryEntity( 1,  "XXX", "::NAME::", "AS", null, null );
            final RequestBuilder request = withHeaders( get( "/location/country/{id}", 1 ) );

            when(repository.findById( any() ))
                    .thenReturn( Optional.of( countryEntity ) );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$.id" ).value( 1 ) )
                    .andExpect( jsonPath( "$.code" ).value( "XXX" ) )
                    .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                    .andExpect( jsonPath( "$.continent" ).value( "AS" ) )
                    .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                    .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();
            MockHttpServletResponse response = result.getResponse();
            String jsonString =
                    """
                    {
                       "id": 1,
                       "code: "ZZZ",
                       "localCode": "LCL",
                       "name": "foo",
                       "country": "ZZ",
                       "continent": "NA"
                    }
                    """;
//        MediaType.
            // --- then
            // TODO need to assert the resulting JSON....

//        assertThat( MediaType.parseMediaType( response.getContentType() ))
//        MediaType.parseMediaType( response.getContentType() ).isCompatibleWith( MediaType.APPLICATION_JSON );;
//        assertThat( MediaType.parseMediaType( response.getContentType() ) )
//                .isCompatibleWith( MediaType.APPLICATION_JSON );
//        assertThat( response ).isNotNull();

            assertThat( response.getContentType() )
                    .isEqualTo( MediaType.APPLICATION_JSON_VALUE );

            //        assertThat( result.getResponse() )
////                .startsWith( "Foo" )
//                .isEqualToIgnoreCase( "foo" );
//        assertThat( response )
//                .isNotNull()
//                .returns( "RP" );

//        JSONAssert.assertEquals

        }

        @Test
        void restGetById_withBadId_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/country/{id}", 99 ) );

            when(repository.findById( anyInt() ))
                    .thenReturn( Optional.ofNullable( null ) );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isNoContent() )
                    .andReturn();
            MockHttpServletResponse response = result.getResponse();

            // --- then
            assertThat( response.getStatus() )
                    .isEqualTo( HttpStatus.NO_CONTENT.value() );
//        assertThat( response.getContentType() )
//                .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }


        @Test
        void restGetByCode_withValidCode_returnsItem() throws Exception
        {
            // --- given
            final CountryEntity countryEntity = new CountryEntity(2,  "XXX", "::NAME::", "NA", null, null );
            final RequestBuilder request = withHeaders( get( "/location/country/code/{code}", 1 ) );

            when(repository.findByCode( anyString() ))
                    .thenReturn( Optional.of( countryEntity ) );


            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$.id" ).value( 2 ) )
                    .andExpect( jsonPath( "$.code" ).value( "XXX" ) )
                    .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                    .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                    .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                    .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();
            MockHttpServletResponse response = result.getResponse();
            String jsonString =
                    """
                    {
                       "id": 2,
                       "code: "XXX",
                       "name": "::NAME::",
                       "continent": "NA"
                    }
                    """;

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

            when(repository.findByCode( anyString() ))
                    .thenReturn( Optional.ofNullable( null ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isNoContent() )
//                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                    .andReturn();
            MockHttpServletResponse response = result.getResponse();

            // --- then
            assertThat( response.getStatus() )
                    .isEqualTo( HttpStatus.NO_CONTENT.value() );
//        assertThat( response.getContentType() )
//                .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }


        @Test
        void restGetAll_returnsSuccess() throws Exception
        {
            // --- given
            // ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
            final List<CountryEntity> entities =
                    List.of(
                            new CountryEntity( 1,  "XXX", "::X_NAME_X::", "AS", null, null ),
                            new CountryEntity( 2,  "YYY", "::Y_NAME_Y::", "AS", null, null ),
                            new CountryEntity( 3,  "ZZZ", "::Z_NAME_Z::", "AS", null, null )
                           );
            final RequestBuilder request = withHeaders( get( "/location/country" ) )
                    .param( "page", "5" )
                    .param( "size", "10" )
                    .param( "sort", "id,desc" )    // <-- no space after comma!
                    .param( "sort", "name,asc" );  // <-- no space after comma!

            Page<CountryEntity> page = new PageImpl( entities );
            when(repository.findAll( any( Pageable.class ) ) )
                    .thenReturn( page );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andDo( print() )
                    .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                    .andExpect( jsonPath( "$.content[0].code" ).value( "XXX" ) )
                    .andExpect( jsonPath( "$.content[0].name" ).value( "::X_NAME_X::" ) )
                    .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                    .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() )
                    .andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final String jsonString =
                    """
                    [
                        {
                           "id": 1,
                           "code: "XX",
                           "name": "::XNAMEX::"
                        },
                        {
                           "id": 2,
                           "code: "YY",
                           "name": "::YNAMEY::"
                        },
                        {
                           "id": 3,
                           "code: "ZZ",
                           "name": "::ZNAMEZ::"
                        }
                    ]
                    """;

            // --- then
            // TODO need to assert the resulting JSON....
            final ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass( Pageable.class );
            verify( repository ).findAll( pageableCaptor.capture() );
            final PageRequest pageable = (PageRequest) pageableCaptor.getValue();


            PageableAssert
                    .assertThat( pageable )
                    .hasPageNumber( 5 )
                    .hasPageSize( 10 )
                    .hasSort( "name", Sort.Direction.ASC )
                    .hasSort( "id", Sort.Direction.DESC );

            assertThat( response.getContentType() )
                    .isEqualTo( MediaType.APPLICATION_JSON_VALUE );
        }
    }

    @Nested
    @DisplayName( "/country - HTTP POST")
    class Post
    {}

    @Nested
    @DisplayName( "/country - HTTP PUT")
    class Put
    {}

    @Nested
    @DisplayName( "/country - HTTP DELETE")
    class Delete
    {}



}