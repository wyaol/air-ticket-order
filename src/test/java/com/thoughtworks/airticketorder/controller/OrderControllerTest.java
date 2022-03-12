package com.thoughtworks.airticketorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.airticketorder.controller.request.OrderCreateRequest;
import com.thoughtworks.airticketorder.dto.ClassType;
import com.thoughtworks.airticketorder.service.OrderService;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldCreateOrderSuccess() throws Exception {
        when(orderService.createOrder(any())).thenReturn(new OrderCreated(7865));

        mockMvc.perform(post("/air-ticket-orders")
                .header("userId", 123)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(
                                OrderCreateRequest.builder()
                                        .amount(BigDecimal.valueOf(2000))
                                        .flightId("096749")
                                        .classType(ClassType.ECONOMY)
                                        .build()
                        )
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").value(""))
                .andExpect(jsonPath("$.data.id").value(7865));
    }
}
