package com.example.airline.location.api;


//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.example.airline.location.mapper.RegionMapper;
import com.example.airline.location.persistence.model.location.RegionEntity;
import com.example.airline.location.persistence.repository.location.RegionsRepository;
import com.example.airline.location.service.RegionsService;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class RegionControllerRestTest extends RestControllerTestBase
{
    @Autowired
    private RegionsService service;

    @MockitoBean
    protected RegionsRepository repository;    // ???

    @Autowired
    private RegionMapper mapper;




    @Test
    void restGetById_withId_returns() throws Exception
    {
        // --- given
        RegionEntity regionEntity = new RegionEntity( 1,  "ZZZ", "LCL", "foo", "ZZ", "NA", null, null );
//        Region       region       = new Region( 1,  "ZZZ", "LCL", "foo", "ZZ", "NA", null, null  );
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get( "/location/region/{id}", 1 ) );

        when(repository.findById( any() ))
                .thenReturn( Optional.of( regionEntity ) );


        // --- when
        MvcResult result = mvc
                .perform( request )
                .andExpect( status().isOk() )
                .andExpect( content().contentTypeCompatibleWith( MediaType.APPLICATION_JSON.toString() ))
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

}