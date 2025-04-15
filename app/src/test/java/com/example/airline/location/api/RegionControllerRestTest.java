package com.example.airline.location.api;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.airline.location.mapper.RegionMapper;
import com.example.airline.location.persistence.model.location.RegionEntity;
import com.example.airline.location.persistence.repository.location.RegionsRepository;
import com.example.airline.location.service.RegionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class RegionControllerResetTest extends RestControllerTestBase
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
        RegionEntity regionEntity = new RegionEntity( 1,  "ZZZ", "LCL", "foo", "ZZ", "NA", null, null );
//        Region       region       = new Region( 1,  "ZZZ", "LCL", "foo", "ZZ", "NA", null, null  );
//        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get("/api/v1/location/continent/{id}", 123 ) );
        RequestBuilder request = withHeaders( MockMvcRequestBuilders.get( "/location/region/{id}", 1 ) );

//        when(repository.findById( any() ))
//                .thenReturn( Optional.ofNullable( null ) );
        when(repository.findById( any() ))
                .thenReturn( Optional.of( regionEntity ) );
//        when(service.findRegionById( any() ))
//                .thenReturn( Optional.of( region ) );


        MvcResult result = mvc
                .perform( request )
                //.andExpect( content().contentTypeCompatibleWith( "application/json" ) )
                //.andExpect( result().ok() )
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // TODO need to assert the resulting JSON....
        assertAll( () -> assertEquals( HttpStatus.OK.value(), result.getResponse().getStatus() ),
                   () -> assertEquals( MediaType.APPLICATION_JSON_VALUE, response.getContentType() )
//                   () -> assertEquals( "Bar", response.getContentAsString() )
                 );
    }

}