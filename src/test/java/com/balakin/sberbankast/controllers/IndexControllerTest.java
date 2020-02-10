package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.services.OperatorServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {

    @Mock
    OperatorServiceImpl operatorService;

    @Mock
    Model model;

    IndexController controller;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new IndexController(operatorService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void getIndexPage() throws Exception {

        //given
        List<Operator> operators = new ArrayList<>();
        operators.add(new Operator());

        Operator operator = new Operator();
        operator.setId(1L);

        operators.add(operator);

        when(operatorService.getOperators()).thenReturn(operators);

        ArgumentCaptor<Set<Operator>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.getIndexPage(model);


        //then
        assertEquals("index", viewName);
        verify(operatorService, times(1)).getOperators();
        verify(model, times(1)).addAttribute(eq("operators"), argumentCaptor.capture());
        Set<Operator> setInController = argumentCaptor.getValue();
        assertEquals(2, setInController.size());
    }

}