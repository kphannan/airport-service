package com.example.airline.location.region.api;


import static com.example.rest.utility.HeaderUtility.withHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.example.airline.location.region.mapper.RegionDtoMapper;
import com.example.airline.location.region.model.Region;
import com.example.airline.location.region.persistence.model.RegionEntity;
import com.example.airline.location.region.persistence.repository.RegionRepository;
import com.example.airline.location.region.service.RegionCreateService;
import com.example.airline.location.region.service.RegionDeleteService;
import com.example.airline.location.region.service.RegionReadService;
import com.example.airline.location.region.service.RegionUpdateService;
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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest( controllers = RegionController.class )
@ComponentScan( basePackages = { "com.example.airline.location.region" } )
@AutoConfigureMockMvc( addFilters = false )
@DisplayName( "Region: API (/region)" )
class RegionControllerRestTest //extends RestControllerTestBase
{
    @Autowired
    protected MockMvc            mvc;

    @MockitoBean
    protected RegionRepository  repository;

    @MockitoSpyBean
    private RegionCreateService createService;
    @MockitoSpyBean
    private RegionReadService   readService;
    @MockitoSpyBean
    private RegionUpdateService updateService;
    @MockitoSpyBean
    private RegionDeleteService deleteService;

    @MockitoSpyBean
    private   RegionDtoMapper   mapper;


    // ========== CREATE ==========
    // ===== POST =====
    @Nested
    @DisplayName( "/region - HTTP POST" )
    class PostMethod        // NOPMD
    {
    }


    // ========== READ ==========
    // ===== GET =====
    @Nested
    @DisplayName( "HTTP GET" )
    class GetMethod        // NOPMD
    {
        @Test
        @DisplayName( "with id - 200: OK - body contains entity" )
        void restGetById_withValidId_returnsItem() throws Exception
        {
            // --- given
            final RegionEntity regionEntity = new RegionEntity( 1, "ZZZ", "LCL", "foo", "ZZ", "NA", null, null );
            final RequestBuilder request = withHeaders( get( "/location/region/{id}", 1 ) )
                    .characterEncoding( "UTF-8" );

            when( repository.findById( any() ) )
                    .thenReturn( Optional.of( regionEntity ) );

            // --- when
            final ResultActions resultActions = mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertThat( response.getContentType() )
                               .isEqualTo( MediaType.APPLICATION_JSON_VALUE ),
                       () -> resultActions
                               .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // TODO need to assert the resulting JSON....
                       () -> resultActions
                               // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                               //      don't complain about lack of assertions in tests
                               .andExpect( jsonPath( "$.id" ).value( 1 ) )
                               .andExpect( jsonPath( "$.code" ).value( "ZZZ" ) )
                               .andExpect( jsonPath( "$.localCode" ).value( "LCL" ) )
                               .andExpect( jsonPath( "$.name" ).value( "foo" ) )
                               .andExpect( jsonPath( "$.country" ).value( "ZZ" ) )
                               .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                               .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                               .andExpect( jsonPath( "$.keywords" ).doesNotExist() )
                     );
        }

        @Test
        @DisplayName( "with invalid Id - 204: No Content - empty body" )
        void restGetById_withBadId_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/region/{id}", 99 ) )
                    .characterEncoding( "UTR-8" );

            when( repository.findById( anyInt() ) )
                    .thenReturn( Optional.empty() );


            // --- when
            final ResultActions resultActions = mvc
                    .perform( request );

            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then
            assertThat( response.getStatus() )
                    .isEqualTo( HttpStatus.NO_CONTENT.value() );
        }


        @Test
        @DisplayName( "with code - 200: OK - return entity" )
        void restGetByCode_withValidCode_returnsItem() throws Exception
        {
            // --- given
            final RegionEntity regionEntity = new RegionEntity( 2, "CC-LCL", "LCL", "::NAME::", "CC", "NA", null, null );
            final RequestBuilder request = withHeaders( get( "/location/region/code/{code}", "ZZZ" ) )
                    .characterEncoding( "UTF-8" );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.of( regionEntity ) );

            // --- when
            final ResultActions resultActions = mvc
                    .perform( request );

            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();

            // --- then

            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       () -> assertFalse( response.getHeaderNames()
                                                  .isEmpty() ),
                       () -> assertThat( response.getRedirectedUrl() )
                               .matches( "^.*/location/region/code/ZZZ" ),
                       () -> resultActions
                               .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
                               .andExpect( content().encoding( "UTF-8" ) ),
                       () -> resultActions
                               // TODO need to assert the resulting JSON....
                               // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                               //      don't complain about lack of assertions in tests
                               .andExpect( jsonPath( "$.id" ).value( 2 ) )
                               .andExpect( jsonPath( "$.code" ).value( "CC-LCL" ) )
                               .andExpect( jsonPath( "$.localCode" ).value( "LCL" ) )
                               .andExpect( jsonPath( "$.name" ).value( "::NAME::" ) )
                               .andExpect( jsonPath( "$.country" ).value( "CC" ) )
                               .andExpect( jsonPath( "$.continent" ).value( "NA" ) )
                               .andExpect( jsonPath( "$.wikipediaLink" ).doesNotExist() )
                               .andExpect( jsonPath( "$.keywords" ).doesNotExist() ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService, times( 1 ) )
                               .findRegionByCode(  anyString() ) ,
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService )
                     );
        }

        @Test
        @DisplayName( "invalid Code - 204: No Content - empty body" )
        void restGetByCode_withBadCode_returnsNoContent() throws Exception
        {
            // --- given
            final RequestBuilder request = withHeaders( get( "/location/region/code/{code}", "ZZ" ) )
                    .characterEncoding( "UTR-8" );

            when( repository.findByCode( anyString() ) )
                    .thenReturn( Optional.empty() );

            // --- when
            final ResultActions resultActions = mvc
                    .perform( request );

            // --- then
            resultActions.andDo(  print() );

            final MvcResult result = resultActions.andReturn();
            final MockHttpServletResponse response = result.getResponse();

            assertAll( () -> assertThat( response.getStatus() )
                               .isEqualTo(  HttpStatus.NO_CONTENT.value() ),
                       () -> resultActions
                               .andExpect( status().isNoContent() )
////                    .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) )
////                    .andExpect( content().encoding( "UTF-8" ))
                     );
        }


        @Test
        @DisplayName( "get Page - 200: OK - returns first page" )
        void restGetAll_returnsSuccess() throws Exception
        {
            // --- given
            final ArgumentCaptor<Pageable> pageableCaptor =
                    ArgumentCaptor.forClass( Pageable.class );
            //  -- response
            final List<RegionEntity> entities =
                    List.of(
                            new RegionEntity( 1, "XXX", "YYY", "::X_NAME_X::", "ZZ", "NA", null, null ),
                            new RegionEntity( 2, "YYY", "YYY", "::Y_NAME_Y::", "ZZ", "NA", null, null ),
                            new RegionEntity( 3, "ZZZ", "LCL", "::Z_NAME_Z::", "ZZ", "NA", null, null )
                           );
            final Page<RegionEntity> page = new PageImpl<>( entities );
            when( repository.findAll( any( Pageable.class ) ) )
                    .thenReturn( page );

            //  -- request
            final RequestBuilder request = withHeaders( get( "/location/region" ) )
                    .param( "page", "5" )
                    .param( "size", "10" )
                    .param( "sort", "id,desc" )    // <-- no space after comma!
                    .param( "sort", "name,asc" )   // <-- no space after comma!
                    .characterEncoding( "UTR-8" );


            // --- when
            final ResultActions resultActions = mvc
                    .perform( request );

            resultActions.andDo( print() );

            // --- then
//            verify( repository ).findAll( pageableCaptor.capture() );
//            final PageRequest pageable = (PageRequest)pageableCaptor.getValue();

//            final MvcResult result = resultActions.andReturn();
//            final MockHttpServletResponse response = result.getResponse();
            final MockHttpServletResponse response = resultActions
                    .andReturn()
                    .getResponse();

            assertAll( () -> assertEquals( HttpStatus.OK.value(), response.getStatus() ),
                       // Response format
                       () -> verify( repository )
                               .findAll( pageableCaptor.capture() ),
                       () -> PageableAssert
//                               .assertThat( pageable )
                               .assertThat( (PageRequest)pageableCaptor.getValue() )
                               .hasPageNumber( 5 )
                               .hasPageSize( 10 )
                               .hasSort( "name", Sort.Direction.ASC )
                               .hasSort( "id", Sort.Direction.DESC ),
//                       () -> assertFalse( response.getHeaderNames().isEmpty()),
//                       () -> assertEquals( 2, response.getHeaderNames().size()),
                       () ->  resultActions
                               .andExpect( status().isOk() )
                               .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ) ),
                       // TODO need to assert the resulting JSON....
                       () -> resultActions
                               // TODO Prefer to inspect the JSON in assertions so SonarQube and PMD
                               //      don't complain about lack of assertions in tests
                               .andExpect( jsonPath( "$.content[0].id" ).value( 1 ) )
                               .andExpect( jsonPath( "$.content[0].code" ).value( "XXX" ) )
                               .andExpect( jsonPath( "$.content[0].name" ).value( "::X_NAME_X::" ) )
                               .andExpect( jsonPath( "$.content[0].wikipediaLink" ).doesNotExist() )
                               .andExpect( jsonPath( "$.content[0].keywords" ).doesNotExist() ),
                       // Services / Persistence
                       () -> verify( repository, atLeastOnce() ).findAll( any(Pageable.class) ),
                       () -> verify( mapper, atLeastOnce() ).domainToApi( any( Region.class ) ),
                       () -> verifyNoInteractions( createService ),
                       () -> verify( readService, times( 1 ) )
                               .findAll( any( Pageable.class ) ) ,
                       () -> verifyNoInteractions( updateService ),
                       () -> verifyNoInteractions( deleteService )
                     );
        }
    }


    // ========== UPDATE ==========
    // ===== PATCH =====
    @Nested
    @DisplayName( "HTTP PATCH" )
    class PatchMethod        // NOPMD
    {
    }

    // ===== PUT =====
    @Nested
    @DisplayName( "HTTP PUT" )
    class PutMethod        // NOPMD
    {
    }

    // ========== DELETE ==========
    // ===== DELETE =====
    @Nested
    @DisplayName( "HTTP DELETE" )
    class DeleteMethod        // NOPMD
    {
    }


    // ========== Administrative ==========
    // ===== HEAD =====
    @Nested
    @DisplayName( "HTTP HEAD" )
    class HeadMethod         // NOPMD
    {
    }

    // ===== INFO =====
    @Nested
    @DisplayName( "HTTP INFO" )
    class InfoMethod         // NOPMD
    {
    }

    // ===== OPTION =====
    @Nested
    @DisplayName( "HTTP OPT" )
    class OptionsMethod      // NOPMD
    {
    }

    // ===== TRACE =====
    @Nested
    @DisplayName( "HTTP TRACE" )
    class TraceMethod        // NOPMD
    {
    }

}
