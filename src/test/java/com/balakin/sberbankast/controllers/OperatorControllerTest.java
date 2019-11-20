package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.services.OperatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OperatorControllerTest {



    @Mock
    OperatorServiceImpl operatorService;

   OperatorController controller;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new OperatorController(operatorService);
    }

    @Test
    public void testGetRecipe() throws Exception {

        Operator operator = new Operator();
        operator.setId(1L);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(operatorService.findById(anyLong())).thenReturn(operator);

        mockMvc.perform(get("/operator/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("operator/show")).
                andExpect(model().attributeExists("operator"));
    }
}