package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
import com.thoughtworks.airticketorder.dto.ClassType;
import com.thoughtworks.airticketorder.repository.OrderRepository;
import com.thoughtworks.airticketorder.repository.entity.OrderEntity;
import com.thoughtworks.airticketorder.service.dto.OrderCreate;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryTicketPriceClient inventoryTicketPriceClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderSuccess() {

        when(orderRepository.save(any())).thenReturn(
                OrderEntity.builder().id(234) .build()
        );
        when(inventoryTicketPriceClient.lockInventory(any())).thenReturn(
                new ClientResponse<>(0, "", new InventoryLockResponse("5d8y6v"))
        );

        final OrderCreated orderCreated = orderService.createOrder(
                new OrderCreate(123, "flightId", ClassType.ECONOMY, BigDecimal.valueOf(2000)));

        assertEquals(234, orderCreated.getId());
    }

}
