package com.example.airline.location.continent.api;


import static com.example.rest.utility.HeaderTestingSupport.withHeaders;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.example.airline.location.continent.mapper.ContinentDtoMapper;
import com.example.airline.location.continent.mapper.ContinentEntityMapper;
import com.example.airline.location.continent.model.Continent;
import com.example.airline.location.continent.model.ContinentTester;
import com.example.airline.location.continent.model.NewContinent;
import com.example.airline.location.continent.persistence.model.ContinentEntity;
import com.example.airline.location.continent.persistence.model.NewContinentEntity;
import com.example.airline.location.continent.persistence.repository.ContinentRepository;
import com.example.airline.location.continent.service.ContinentCreateService;
import com.example.airline.location.continent.service.ContinentDeleteService;
import com.example.airline.location.continent.service.ContinentReadService;
import com.example.airline.location.continent.service.ContinentUpdateService;
import com.example.rest.utility.ProblemDetailTester;
import com.example.utility.HeaderUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;
//import org.springframework.transaction.support.TransactionTemplate;


/**
 * Tests for the REST endpoints only, no underlying Service or Repository.
 */
//@ExtendWith( SpringExtension.class )
@WebMvcTest( controllers = ContinentController.class )
@ComponentScan( basePackages = { "com.example.airline.location.continent" } )
@AutoConfigureMockMvc( addFilters = false /*, secure = false */ )
@DisplayName( "Continent: API (/continent)" )
class ContinentControllerRestTest
{
    private static final MediaType DESIRED_CONTENT_TYPE = new MediaType( MediaType.APPLICATION_JSON,
                                                                         StandardCharsets.UTF_8 );
    @Autowired
    protected MockMvc              mvc;

    @MockitoSpyBean
    private ContinentCreateService  createService;
    @MockitoSpyBean
    private ContinentReadService    readService;
    @MockitoSpyBean
    private ContinentUpdateService  updateService;
    @MockitoSpyBean
    private ContinentDeleteService  deleteService;

    @MockitoSpyBean
    private ContinentDtoMapper    mapper;
    @MockitoSpyBean
    private ContinentEntityMapper entityMapper;

    @MockitoBean
    private ContinentRepository     repository;

    private HttpHeaders             requestHeaders;

    //    @Autowired
    // private TransactionTemplate transactionTemplate;

    @BeforeEach
    void init()
    {
        requestHeaders = new HttpHeaders();
        requestHeaders.set( HeaderUtility.TRACESTATE, "testState" );
        requestHeaders.set( HeaderUtility.TRACEID, "testParent" );
        requestHeaders.set( "NoWay", "Should not exist" );
        requestHeaders.setContentType( DESIRED_CONTENT_TYPE );
    }


    // ========== CREATE ==========
    // ===== POST =====
    /**
     * Tests for the POST http method.
     */
    @Nested
    @DisplayName( "HTTP POST" )
    class PostMethod           // NOPMD
    {
        @Test
        @DisplayName( "Existing resource - 409: Conflict - body ???" )
        void restPost_withExisting_returnsConflict() throws Exception
        {
            // --- given
            final String jsonString =
                    """
                    {
                       "code": "AS",
                       "name": "foo"
                    }
                    """;
            final RequestBuilder request = withHeaders( post( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .content( jsonString )
                    .headers( requestHeaders );

            // -- mocks behavior
            when( repository.existsByCode( anyString() ) )
                    .thenReturn( true );

            // --- when
            final ResultActions resultActions = mvc.perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            // final ArgumentCaptor<Pageable> pageableCaptor =
            //     ArgumentCaptor.forClass( Pageable.class );

            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.CONFLICT.value() ),
                       () -> resultActions
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),

                       // --- header
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       // --- body
                       () -> assertThat( ProblemDetailTester.of( response.getContentAsString() ) )
                                 .blankType()
                                 .hasStatus( HttpStatus.CONFLICT )
                                 .hasTitle( "Conflict" )
                                 .blankDetail()
                                 .hasInstance( "/location/continent" )
                                 .blankProperties(),
                       //
                       () -> verify( createService ).create( any( NewContinent.class ) ),
                       () -> verifyNoInteractions( readService ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       // persistence
                       () -> verify( repository ).existsByCode( anyString() ),
                       () -> verify( repository, never() ).save( any( NewContinentEntity.class ) ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );
        }




        @Test
        @DisplayName( "new resource - 200: Success - body is new entity" )
        void restPost_withNew_returnsCreated() throws Exception
        {
            // --- given
            final String jsonString =
                    """
                    {
                       "code": "NA",
                       "name": "foo"
                    }
                    """;
            final RequestBuilder request = withHeaders( post( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .content( jsonString )
                    .headers( requestHeaders );

            when( repository.existsByCode( eq( "NA" ) ) )
                    .thenReturn( false );

            final ContinentEntity entity = new ContinentEntity( 22, "CC", "::ZNAMEZ::", null, null );
            when( repository.save( any( NewContinentEntity.class ) ) )
                    .thenReturn( entity );


            // --- when
            final ResultActions resultActions = mvc.perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result                 = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();

            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.CREATED.value() ),
                       () -> resultActions
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // ----
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       // -----
                       () -> assertThat( response.getRedirectedUrl() )
                                 .matches( "^.*/location/continent/22" ),
                       () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                                 .anyMatch( location -> location.matches( "^.*/location/continent/22$" ) ),
                       // TODO should response include problem details indicating existing entity with same ID
                       // verify the resulting JSON....
                       () -> ContinentTester.of( response.getContentAsString() )
                                 .hasId( 22 )
                                 .hasCode( "CC" )
                                 .hasName( "::ZNAMEZ::" )
                                 .blankWikiLink()
                                 .blankKeywords(),
                       // () -> resultActions
                       //           .andExpect( jsonPath( "$.id" ).value( 22 ) )
                       //           .andExpect( jsonPath( "$.code" ).value( "CC" ) )
                       //           .andExpect( jsonPath( "$.name" ).value( "::ZNAMEZ::" ) )
                       //           .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                       //           .andExpect( jsonPath( "$.keywords" ).doesNotExist() ),
                       //
                       () -> verify( createService ).create( any( NewContinent.class ) ),
                       () -> verifyNoInteractions( readService ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       // persistence
                       () -> verify( repository ).existsByCode( eq( "NA" ) ),
                       () -> verify( repository ).save( any( NewContinentEntity.class ) ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
            );
        }



        // @Disabled
        @Nested
        @DisplayName( "validations" )
        class Validation
        {
            MockHttpServletRequestBuilder request;

            @BeforeEach
            void setup()
            {
                request = withHeaders( post( "/location/continent" ) )
                                                   .characterEncoding( "UTF-8" )
                                                   .headers( requestHeaders );
            }

            // @Disabled
            @Test
            @DisplayName( "No name or Code - 400: Bad Request - Problem details with messages" )
            void restPost_withNoRequiredParams_returnsValidationError() throws Exception
            {
                // --- given
                final String jsonString =
                        """
                        {
                           "keywords": "will report missing code, name fields"
                        }
                        """;
                request.content( jsonString );

                // --- when
                final ResultActions resultActions = mvc.perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();
                final Collection<String> headerNames   = response.getHeaderNames();
                // final HttpHeadersAssert  headersAssert = new HttpHeadersAssert( result.getResponse(). .getHeaders() );
                final String             body          = result.getResponse().getContentAsString();

                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.BAD_REQUEST.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                           // ----
                           () -> assertThat( headerNames )
                                     .contains( "TRACEPARENT" )
                                     .contains( "TRACESTATE" )
                                     .contains( "Content-Type" )
                                     .doesNotContain( "NoWay" ),
                           // -----
                           // () -> assertThat( response.getRedirectedUrl() )
                           //           .matches( "^.*/location/continent/22" ),
                           // () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                           //           .anyMatch( location -> location.matches( "^.*/location/continent/22$" ) ),
                           // TODO should response include problem details indicating existing entity with same ID
                           // verify the resulting JSON....
                           () -> assertThat( ProblemDetailTester.of( response.getContentAsString() ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Validation failed on 'newContinentDTO'" )
                                     .hasDetail( "Invalid request content." )
                                     .hasInstance( "/location/continent" )
                                     .hasProperties( entry( "name", "Continent name must be between 2 to 52 characters, provided: [null]" ),
                                                     entry( "code",
                                                            """
                                                            Code must be one of these character sequences:
                                                            AF, AN, AS, EU, NA, OC, SA
                                                            , provided: [null]"""  )
                                                   ),
                           //
                           () -> verifyNoInteractions( createService ),
                           () -> verifyNoInteractions( readService ),
                           () -> verifyNoInteractions( updateService ),
                           () -> verifyNoInteractions( deleteService ),
                           // persistence
                           () -> verifyNoInteractions( repository ),
                           () -> verifyNoInteractions( mapper )
                         );
            }



            // @Disabled
            @Test
            @DisplayName( "blank name and code - 400: Bad Request - problem details indicate blank values" )
            void restPost_withBlankRequiredParams_returnsValidationError() throws Exception
            {
                // --- given
                final String jsonString =
                        """
                        {
                           "code": "  ",
                           "name": "     "
                        }
                        """;
                request.content( jsonString );

                // --- when
                final ResultActions resultActions = mvc.perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result                 = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();
                final Collection<String> headerNames   = response.getHeaderNames();

                // --- then
                // TODO verify the JSON is the created entity
                // final String body = response.getContentAsString();

                // It is desired to have all 'asserts' as soft asserts.
                assertAll( () -> assertEquals( HttpStatus.BAD_REQUEST.value(), response.getStatus() ),
                           () -> assertThat( response.getHeaderNames() )
                                   .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                           // ----
                           () -> assertThat( headerNames )
                                     .contains( "TRACEPARENT" )
                                     .contains( "TRACESTATE" )
                                     .contains( "Content-Type" )
                                     .doesNotContain( "NoWay" ),
                           // -----
                           // verify the resulting JSON....
                           () -> assertThat( ProblemDetailTester.of( response.getContentAsString() ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Validation failed on 'newContinentDTO'" )
                                     .hasDetail( "Invalid request content." )
                                     .hasInstance( "/location/continent" )
                                     .hasProperties( entry( "name", "Continent name must be between 2 and 52 characters, provided: [     ]" ),
                                                     entry( "code",
                                                            """
                                                                Code must be one of these character sequences:
                                                                AF, AN, AS, EU, NA, OC, SA
                                                                , provided: [  ]""" )
                                                   )
                );
            }



            @Test
            @DisplayName( "invalid Wiki URI - 400: Bad Request - problem detail shows malformed URI" )
            void restPost_withInvalidWikiLink_returnsValidationError() throws Exception
            {
                // --- given
                // "detail" : "JSON parse error: Unexpected character ('/' (code 47)): maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)",
                //                "instance" : "/location/continent",
                //                                 "Exception" : "org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Unexpected character ('/' (code 47)): maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)",
                //                                                   "Cause" : "tools.jackson.core.exc.StreamReadException: Unexpected character ('/' (code 47)): maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); byte offset: #5]",
                final String jsonString =
                        """
                        {
                           "code": "NA",
                           "name": "A valid length name",
                           "wikiLink": "https://wikipedia.com/bad url/not encoded",
                           "keywords": "will report missing code, name fields"
                        }
                        """;
                request.content( jsonString );


                // --- when
                final ResultActions resultActions = mvc.perform( request );

                // --- then
                resultActions.andDo(  print() );

                final MvcResult result                 = resultActions.andReturn();
                final MockHttpServletResponse response = result.getResponse();
                final Collection<String> headerNames   = response.getHeaderNames();

                // TODO Include Problem Details as response body
                assertAll( () -> assertThat( response.getStatus() )
                                     .isEqualTo( HttpStatus.BAD_REQUEST.value() ),
                           () -> resultActions
                                     .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),

                           // ----
                           () -> assertThat( headerNames )
                                     .contains( "TRACEPARENT" )
                                     .contains( "TRACESTATE" )
                                     .contains( "Content-Type" )
                                     .doesNotContain( "NoWay" ),
                           // -----
                           // verify the resulting JSON....
                           () -> assertThat( ProblemDetailTester.of( response.getContentAsString() ) )
                                     .blankType()
                                     .hasStatus( HttpStatus.BAD_REQUEST )
                                     .hasTitle( "Malformed Request")
                                     .hasDetail( "JSON parse error: Cannot deserialize value of type `java.net.URI` from String \"https://wikipedia.com/bad url/not encoded\": not a valid textual representation, problem: Illegal character in path at index 25: https://wikipedia.com/bad url/not encoded" )
                                     .hasInstance( "/location/continent" )
                                     .hasProperties( entry( "Exception",
                                                            "org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `java.net.URI` from String \"https://wikipedia.com/bad url/not encoded\": not a valid textual representation, problem: Illegal character in path at index 25: https://wikipedia.com/bad url/not encoded"
                                                          ),
                                                     entry( "Cause",
                                                            "tools.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `java.net.URI` from String \"https://wikipedia.com/bad url/not encoded\": not a valid textual representation, problem: Illegal character in path at index 25: https://wikipedia.com/bad url/not encoded\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); byte offset: #68] (through reference chain: com.example.airline.location.continent.NewContinentDTO[\"wikiLink\"])"
                                                          ),
                                                     entry( "Possibility 1", "Malformed request body" ),
                                                     entry( "Possibility 2", "Invalid request parameters" ),
                                                     entry( "Possibility 3", "Incompatible data format" ),
                                                     entry( "Possibility 4", "Serialization errors" )
                                                   ),
                           //
                           () -> verifyNoInteractions( createService ),
                           () -> verifyNoInteractions( readService ),
                           () -> verifyNoInteractions( updateService ),
                           () -> verifyNoInteractions( deleteService ),
                           // persistence
                           () -> verifyNoInteractions( repository ),
                           () -> verifyNoInteractions( mapper )
                         );
            }

        }
    }


    // ========== READ ==========
    // ===== GET =====
    /**
     * Tests for the GET http method.
     */
    @Nested
    @DisplayName( "HTTP GET" )
    class GetMethod           // NOPMD
    {
        @Test
        @DisplayName( "with valid ID - 200: Success - entity in response body" )
        void restGetById_withId_returnsItem() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/{id}", 1 ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders );

            final Continent      continent        = new Continent( 1, "NA", "North", null, null );

            when( readService.findById( anyInt() ) )
                    .thenReturn( Optional.of( continent ) );

            // --- when
            final ResultActions resultActions =
                mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();

            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.OK.value() ),
                       () -> resultActions
                                 .andExpect( content().encoding( "UTF-8" ) )
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),

                       // ----
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                // -----
                //        () -> assertThat( response.getRedirectedUrl() )
                //                  .matches( "^.*/location/continent/22" ),
                //        () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                //                  .anyMatch( location -> location.matches( "^.*/location/continent/22$" ) ),
                // // TODO should response include problem details indicating existing entity with same ID
                // verify the resulting JSON....
                       () -> resultActions
                                 .andExpect( jsonPath( "$.id" ).value( 1 ) )
                                 .andExpect( jsonPath( "$.code" ).value( "NA" ) )
                                 .andExpect( jsonPath( "$.name" ).value( "North" ) ),
                       //
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
                     );
        }


        @Test
        @DisplayName( "with invalid id - 204: No Content - Empty body" )
        void restGetById_withWrongId_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/{id}", 1 ) )
                    .headers( requestHeaders );

            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final ResultActions resultActions =
                mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();

            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.NO_CONTENT.value() ),
                       () -> resultActions
                                 .andExpect( content().encoding( "UTF-8" ) )
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // ----
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       // -----
                       // verify the resulting JSON....
                       // () -> resultActions
                       //           .andExpect( jsonPath( "$.id" ).value( 1 ) )
                       //           .andExpect( jsonPath( "$.code" ).value( "NA" ) )
                       //           .andExpect( jsonPath( "$.name" ).value( "North" ) ),
                       //
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findById( anyInt() ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );
        }


        @Test
        @DisplayName( "with non-numeric ID - 400: Bad Request - body with Problem Details" )
        void restGetById_withTextId_returnsBadRequestWithProblemDetail() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );

            // --- when
            final ResultActions resultActions =  mvc.perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            // final String body = response.getContentAsString();

            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.BAD_REQUEST.value() ),
                       () -> resultActions
                                 .andExpect( content().encoding( "UTF-8" ) )
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // ----
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       // -----
                       // verify the resulting JSON....
                       // --- body
                       () -> assertThat( ProblemDetailTester.of( response.getContentAsString() ) )
                                 .blankType()
                                 .hasStatus( HttpStatus.BAD_REQUEST )
                                 .hasTitle( "Parameter Type Mismatch" )
                                 .hasDetail( "Method parameter 'continentId': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"code\"" )
                                 .hasInstance( "/location/continent/code" )
                                 // TODO validate properties (Exception, Cause)
                                 .hasProperties( entry( "Exception", "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Method parameter 'continentId': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"code\"" ),
                                 //                entry( "Exception", "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException:" ),
                                 //                entry( "Exception", "Method parameter 'continentId': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"code\"" ),
                                                 entry( "Cause", "java.lang.NumberFormatException: For input string: \"code\"" )
                                               ),
                                 // .blankProperties(),
                       //
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService, never() ).findById( anyInt() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       // () -> verify( repository, never() ).findById( anyInt() ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );
        }


        @Test
        @DisplayName( "with code - 200: Success - body with entity" )
        void restGetByCode_withCode_returnsItem() throws Exception
        {
            // --- given
            final ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null );
            final RequestBuilder  request         = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( eq( "ZZ" ) ) )
                    .thenReturn( Optional.of( continentEntity ) );

            // --- when
            final ResultActions resultActions =
                mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            // final String body = response.getContentAsString();

            // ContinentTester continentTester = ContinentTester.of( response.getContentAsString() );
            // assertAll( () -> assertThat( continentTester )
            //                      .hasName( "North")
            //                      .hasCode( "NA" )
            //                      .blankWikiLink()
            //                      .blankKeywords(),


            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.OK.value() ),
                       () -> resultActions
                                 .andExpect( content().encoding( "UTF-8" ) )
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // ----
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       // -----
                       //        () -> assertFalse( body.isBlank() ),
                       // verify the resulting JSON....
                       () -> assertThat( ContinentTester.of( response.getContentAsString() ) )
                                 .hasId( 1 )
                                 .hasName( "::NAME::" )
                                 .hasCode( "ZZ" )
                                 .blankWikiLink()
                                 .blankKeywords(),
                       //
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findByCode( anyString() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findByCode( anyString() ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
            );
        }

        @Test
        @DisplayName( "with invalid code - 204: No Content - body is empty" )
        void restGetByCode_withInvalidCode_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/{code}", "ZZ" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );

            // --- when
            final ResultActions resultActions =
                mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();

            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.NO_CONTENT.value() ),
                       () -> resultActions
                                 .andExpect( content().encoding( "UTF-8" ) )
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // ----
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       // -----
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findByCode( anyString() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findByCode( anyString() ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );
        }

        @Test
        @DisplayName( "with no code - 404: Not Found - body is problem detail" )
        void restGetByCode_withNoCode_returnsNotFoundWithProblemDetail() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent/code/" ) )
                    .headers( requestHeaders );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );

            // --- when
            final ResultActions resultActions =
                mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            // final String body = response.getContentAsString();

            // TODO Include Problem Details as response body
            assertAll( () -> assertThat( response.getStatus() )
                                 .isEqualTo( HttpStatus.NOT_FOUND.value() ),
                       () -> resultActions
                                 .andExpect( content().encoding( "UTF-8" ) )
                                 .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // ----
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       // -----
                       //        () -> assertFalse( body.isBlank() ),
                       // verify the resulting JSON....
                       //        () -> resultActions
                       //                  .andExpect( jsonPath( "$.id" ).value( 1 ) )
                       //                  .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
                       //                  .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                       //                  .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                       //                  .andExpect( jsonPath( "$.keywords" ).doesNotExist() ),
                       //
                       () -> verifyNoInteractions( createService ),
                       () -> verifyNoInteractions( readService ),
                       // () -> verify( readService ).findByCode( anyString() ),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       // () -> verify( repository ).findByCode( anyString() ),
                       () -> verifyNoInteractions( mapper )
                       // () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );
        }


        @Test
        @DisplayName( "get all - 200: Success - body is list of entities" )
        void restGetAll_returnsSuccess() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/continent" ) )
                    .headers( requestHeaders );

            // ContinentEntity continentEntity = new ContinentEntity( 1, "ZZ", "::NAME::", null, null  );
            final List<ContinentEntity> resultList =
                    List.of(
                            new ContinentEntity( 1, "XX", "::XNAMEX::", null, null ),
                            new ContinentEntity( 2, "YY", "::YNAMEY::", null, null ),
                            new ContinentEntity( 3, "ZZ", "::ZNAMEZ::", null, null )
                           );

            when( repository.findAll() )
                    .thenReturn( resultList );

            // --- when
            final ResultActions actions = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( status().isOk() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                    //      don't complain about lack of assertions in tests
                    .andExpect( jsonPath( "$[0].id" ).value( 1 ) );
            // .andExpect( jsonPath( "$.code" ).value( "ZZ" ) )
            // .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
            // .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
            // .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
            final MvcResult result = actions.andReturn();

            // final String jsonString =
            //         """
            //                 [
            //                     {
            //                        "id": 1,
            //                        "code: "XX",
            //                        "name": "::XNAMEX::"
            //                     },
            //                     {
            //                        "id": 2,
            //                        "code: "YY",
            //                        "name": "::YNAMEY::"
            //                     },
            //                     {
            //                        "id": 3,
            //                        "code: "ZZ",
            //                        "name": "::ZNAMEZ::"
            //                     }
            //                 ]
            //                 """;

            // --- then
            // TODO need to assert the resulting JSON....
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       // () -> assertEquals( "application/json;charset=UTF-8", response.getHeader( "Content-Type" ) ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService ).findAll(),
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService ),
                       () -> verify( repository ).findAll()
            );
        }
    }

    // ========== UPDATE ==========
    // ===== PATCH =====
    /**
     * Tests for the PATCH http method.
     */
    @Nested
    @DisplayName( "HTTP PATCH" )
    class PatchMethod           // NOPMD
    {
        //        @Disabled       // TODO research JsonPatch more.
        @Test
        void patchContinent_withKeywords_returnsOk() throws Exception
        {
            // --- given
            // String x = "[{\"op\":\"move\",\"from\":\"/a\",\"path\":\"/b/2\"}]";
            final String json = "[{\"op\": \"replace\", \"path\": \'/keywords\", \"value\": \"Key 7, Key 8\"}]";

            final ContinentEntity continentEntity = new ContinentEntity( 1, "NA", "North", null, null );

            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.of( continentEntity ) );

            final MediaType patchContentType = new MediaType( "application",
                                                              "json-patch+json",
                                                              StandardCharsets.UTF_8 );

            requestHeaders.setContentType( patchContentType );

            final RequestBuilder request = withHeaders( patch( "/location/continent/{id}", 1 ) )
                    .characterEncoding( "UTF-8" )
                    // .contentType( "application/json-patch+json" )
                    .headers( requestHeaders )
                    .content( json );

            // --- when
            final ResultActions resultActions =
                mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NOT_IMPLEMENTED.value(), result.getResponse().getStatus() ),
                       () -> resultActions
                                 .andExpect( content().encoding( "UTF-8" ) )
                                 .andExpect( content().contentTypeCompatibleWith( "application/json-patch+json;charset=UTF-8" ) ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       //
                       () -> assertThat( ContinentTester.of( result.getResponse().getContentAsString() ) )
                                 .hasId( 1 )
                                 .hasName( "North" )
                                 .hasCode( "NA" )
                                 .blankWikiLink()
                                 .blankKeywords(),
                       //
                       // () -> assertThat( result.getResponse().getHeaderNames() )
                       //           .contains( "Content-Type", HeaderTestingSupport.TRACESTATE, HeaderTestingSupport.TRACEID )
                       () -> verify( readService ).findById(  anyInt() ),
                       () -> verify( repository ).findById( anyInt() ),
                       // () -> verify( repository ).save( any( ContinentEntity.class ) ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
            );

            // final Optional<Continent> original = readService.findById( 7 );
            //
            // final Optional<Continent> updated = applyPatchEntity( json, original );


        }

    }

    // ===== PUT =====
    /**
     * Tests for the PUT http method.
     */
    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod           // NOPMD
    {
        @Test
        @DisplayName( "entity does not exist - 409: Conflict - can't create a new instance" )
        void restPut_withNewEntity_returnsConflict() throws Exception
        {
            // --- given
            final ContinentEntity entity = new ContinentEntity( 22, "NA", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "SA",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( put( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( false );
            when( repository.getReferenceById( anyInt() ) )
                    .thenReturn( entity );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.CONFLICT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> verify( updateService ).update( any( Continent.class ) ),
                       () -> verify( repository ).existsById( anyInt() ),
                       () -> verify( repository, never() ).save( any( ContinentEntity.class ) ),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );
        }

        @Test
        @DisplayName( "existing entity - 200: Success  - update/return an existing instance" )
        void restPut_withExistingEntity_returnsOK() throws Exception
        {
            // --- given
            final ContinentEntity entity = new ContinentEntity( 22, "NA", "::ZNAMEZ::", null, null );
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "AF",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( put( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( true );
            when( repository.save( any( ContinentEntity.class ) ) )
                    .thenReturn( entity );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( content().contentTypeCompatibleWith( "application/json" ) )
                    .andExpect( jsonPath( "$.id" ).value( 22 ) )
                    .andExpect( jsonPath( "$.code" ).value( "NA" ) )
                    .andExpect( jsonPath( "$.name" ).value( "::ZNAMEZ::" ) )
                    .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                    .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                      () -> assertThat( result.getResponse().getHeaderNames() )
                              .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       // () -> verify( repository )
                       //           .existsById( eq( 77 ) ),
                       () -> verify( updateService ).update( any( Continent.class ) ),
                       () -> verify( repository ).existsById( anyInt() ),
                       () -> verify( repository ).save( any( ContinentEntity.class ) ),
                       () -> verify( mapper ).domainToApi( any( Continent.class ) )
                       // () -> verify( repository, times( 1 ) )
                       //           .save( any( ContinentEntity.class ) )
            );
        }
    }


    // ========== DELETE ==========
    // ===== DELETE =====
    /**
     * Tests for the DELETE http method.
     */
    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod           // NOPMD
    {
        @Test
        @DisplayName( "by ID - 204: No Content - empty body" )
        void restDeleteById_withId_returnsGone() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) )
                    .headers( requestHeaders );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( true );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();

            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       // () -> assertThat( result.getResponse().getHeaderNames() )
                       //           .contains( "Content-Type", HeaderTestingSupport.TRACESTATE, HeaderTestingSupport.TRACEID ),
                       () -> verify( deleteService ).deleteById( anyInt() ),
                       () -> verify( repository ).deleteById( anyInt() )
            );
        }

        @Test
        @DisplayName( "by invalid ID - 404: Not Found - empty body" )
        void restDeleteByUnknownId_withId_returnsNotFound() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( delete( "/location/continent/{id}", 99 ) )
                    .headers( requestHeaders );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( false );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( repository ).deleteById( anyInt() )
            );
        }


        @Test
        @DisplayName( "by entity - 204: No Content - empty body" )
        void restDelete_withEntity_returnsNoContent() throws Exception
        {
            // --- given
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "EU",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( delete( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( true );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       // () -> assertThat( result.getResponse().getHeaderNames() )
                       //           .contains( "Content-Type", HeaderTestingSupport.TRACESTATE, HeaderTestingSupport.TRACEID ),
                       () -> verify( deleteService ).delete( any( Continent.class ) ),
                       () -> verify( repository ).delete( any( ContinentEntity.class ) )
            );
        }

        @Test
        @DisplayName( "by unknown entity - 404: Not Found - empty body" )
        void restDelete_withUnknownEntity_returnsNotFound() throws Exception
        {
            // --- given
            final String jsonString =
                    """
                            {
                               "id": 77,
                               "code": "OC",
                               "name": "foo"
                            }
                            """;
            final RequestBuilder request = withHeaders( delete( "/location/continent" ) )
                    .characterEncoding( "UTF-8" )
                    .headers( requestHeaders )
                    .content( jsonString );

            when( repository.existsById( anyInt() ) )
                    .thenReturn( false );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            // final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertEquals( HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus() ),
                       () -> assertThat( result.getResponse().getHeaderNames() )
                               .contains( "Content-Type", HeaderUtility.TRACESTATE, HeaderUtility.TRACEID ),
                       () -> verify( repository ).delete( any( ContinentEntity.class ) )
            );
        }
    }

    // ========== Administrative ==========
    // ===== HEAD =====
    /**
     * Tests for the HEAD http method.
     */
    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod           // NOPMD
    {
        @Test
        @DisplayName( "no parameters - 200: empty body" )
        void restHead_withoutId_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( head( "/location/continent" ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> verify( readService ).findAll(),
                       () -> verify( repository ).findAll(),
                       () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );
        }

        @Test
        @DisplayName( "with ID - 200: No body - has headers" )
        void restHead_withId_returnsHeaders() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( head( "/location/continent/{continentId}", 123 ) )
                    .headers( requestHeaders );

            final ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.of( entity ) );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" ),
                       () -> assertTrue( response.getContentAsString().isEmpty() ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verify( repository ).findById(  anyInt() )
                       // () -> verify( mapper, never() ).domainToApi( any( Continent.class ) )
            );

        }


        @Test
        @DisplayName( "with ID - 204: No Content - has headers" )
        void restHead_withBadId_returnsHeaders() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( head( "/location/continent/{continentId}", 123 ) )
                    .headers( requestHeaders );

            when( readService.findById( anyInt() ) )
                    .thenReturn( Optional.empty() );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                                 .contains( "TRACEPARENT" )
                                 .contains( "TRACESTATE" )
                                 .contains( "Content-Type" )
                                 .doesNotContain( "NoWay" ),
                       () -> assertTrue( response.getContentAsString().isEmpty() ),
                       () -> verify( readService ).findById( anyInt() ),
                       () -> verify( repository ).findById(  anyInt() ),
                       () -> verify( entityMapper, never() ).entityToDomain( any( ContinentEntity.class ) )
            );
        }
        // final Continent continent = MAPPER.entityToDomain( from.get() );


        // final ContinentEntity entity = new ContinentEntity( 22, "ZZ", "::ZNAMEZ::", null, null );
        // final String jsonString =
        //        """
        //                {
        //                   "id": 77,
        //                   "code": "CC",
        //                   "name": "foo"
        //                }
        //                """;
        // final RequestBuilder request = withHeaders( put( "/location/continent" ) )
        //        .characterEncoding( "UTF-8" )
        //        .content( jsonString );
        //
        // when( repository.existsById( anyInt() ) )
        //    .thenReturn( false );
        // when( repository.getReferenceById( anyInt() ) )
        //    .thenReturn( entity );
        //
        //




    }

    // ===== INFO =====
    /**
     * Tests for the INFO http method.
     */
    @Nested
    @DisplayName( "HTTP INFO" )
    class InfoMethod           // NOPMD
    {
        // @Test
        // void restInfo_returnsNoContent() throws Exception
        // {
        //     // --- given
        //     final RequestBuilder request = withHeaders( MockMvcRequestBuilders.request( HttpMethod.INFO,
        //                                                                                 "/location/continent" ) );
        //
        //     // --- when
        //     final MvcResult result = mvc
        //             .perform( request )
        //             .andDo( print() )
        //             .andReturn();
        //
        //     // --- then
        //     final MockHttpServletResponse response = result.getResponse();
        //
        //     assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus() )
        //              );
        // }
    }

    // ===== OPTION =====
    /**
     * Tests for the OPTIONS http method.
     */
    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod           // NOPMD
    {
        @Test
        @DisplayName( "no args - 204: No Content - empty body" )
        void restOptions_returnsHeaders() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( options( "/location/continent" ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();

            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.NO_CONTENT.value(), response.getStatus() ),
                       () -> assertThat( response.getHeaders( HttpHeaders.ACCEPT ) )
                               .contains( "application/json,application/yaml,application/xml" ),
                       () -> assertThat( response.getHeader( HttpHeaders.ALLOW ) )
                               .contains( "DELETE" )
                               .contains( "GET" )
                               .contains( "HEAD" )
                               .contains( "OPTIONS" )
                               .contains( "PATCH" )
                               .contains( "POST" )
                               .contains( "PUT" )
                               .contains( "TRACE" ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" )
            );
        }
    }

    // ===== TRACE =====
    /**
     * Tests for the TRACE http method.
     */
    @Nested
    @DisplayName( "HTTP TRACE" )
    class TraceMethod           // NOPMD
    {
        @Test
        @DisplayName( "no args - 200: OK - empty body" )
        void restTrace_returnsOk() throws Exception
        {
            // --- given
            final RequestBuilder request =
                withHeaders( MockMvcRequestBuilders
                                 .request( HttpMethod.TRACE,
                                           "/location/continent" ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = response.getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .contains( "Location" )
                               .doesNotContain( "NoWay" ),
                       () -> assertThat( response.getRedirectedUrl() )
                               .matches( "^.*/location/continent$" ),
                       () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                               .anyMatch( location -> location.matches( "^.*/location/continent$" ) )
            );
        }


        @Test
        @DisplayName( "no args - 200: OK - empty body" )
        void restTrace_withId_returnsOk() throws Exception
        {
            // --- given
            final RequestBuilder request =
                withHeaders( MockMvcRequestBuilders
                                 .request( HttpMethod.TRACE,
                                           "/location/continent/{continentId}",
                                           123
                                         ) )
                    .headers( requestHeaders );

            // --- when
            final MvcResult result = mvc
                    .perform( request )
                    .andDo( print() )
                    .andReturn();

            // --- then
            final MockHttpServletResponse response = result.getResponse();
            final Collection<String> headerNames   = result.getResponse().getHeaderNames();
            assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                       () -> assertThat( headerNames )
                               .contains( "TRACEPARENT" )
                               .contains( "TRACESTATE" )
                               .contains( "Content-Type" )
                               .doesNotContain( "NoWay" )
                               .contains( HttpHeaders.LOCATION ),
                       () -> assertThat( result.getResponse().getRedirectedUrl() )
                               .matches( "^.*/location/continent/123$" ),
                       () -> assertThat( response.getHeaders( HttpHeaders.LOCATION ) )
                               .anyMatch( location -> location.matches( "^.*/location/continent/123$" ) )
            );
        }

    }





    // ========== Support Methods ==========

    private static String asJsonString( final Object obj )
    {
        try
        {
            return new ObjectMapper().writeValueAsString( obj );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }


    private static ObjectMapper buildMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        // mapper
        //    .deserializationConfig()
        //    .configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        return mapper;
    }

}

