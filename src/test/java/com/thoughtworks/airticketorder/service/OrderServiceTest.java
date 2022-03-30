package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.FlightRequestResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
import com.thoughtworks.airticketorder.dto.ClassType;
import com.thoughtworks.airticketorder.exceptions.InventoryShortageException;
import com.thoughtworks.airticketorder.exceptions.NotFoundException;
import com.thoughtworks.airticketorder.exceptions.ServiceErrorException;
import com.thoughtworks.airticketorder.repository.OrderRepository;
import com.thoughtworks.airticketorder.repository.entity.OrderEntity;
import com.thoughtworks.airticketorder.service.dto.OrderCreate;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    final FeignException.GatewayTimeout gatewayTimeout = new FeignException.GatewayTimeout(
            "",
            Request.create(
                    Request.HttpMethod.POST, "", Map.of(), new byte[]{}, Charset.defaultCharset()), new byte[]{}, Map.of()
    );

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

    @Test
    void shouldCreateOrderSuccessWhenGetLockInventoryResponseFailed() {
        when(orderRepository.save(any())).thenReturn(
                OrderEntity.builder().id(234) .build()
        );
        when(inventoryTicketPriceClient.lockInventory(any())).thenThrow(gatewayTimeout);
        when(inventoryTicketPriceClient.getFlightRequest(any())).thenReturn(
                new ClientResponse<>(0, "", new FlightRequestResponse("5d8y6v")));

        final OrderCreated orderCreated = orderService.createOrder(
                new OrderCreate(123, "flightId", ClassType.ECONOMY, BigDecimal.valueOf(2000)));

        assertEquals(234, orderCreated.getId());
    }

    @Test
    void shouldCreateOrderSuccessWhenGetLockInventoryResponseTwice() {
        when(orderRepository.save(any())).thenReturn(
                OrderEntity.builder().id(234) .build()
        );
        when(inventoryTicketPriceClient.lockInventory(any()))
                .thenThrow(gatewayTimeout)
                .thenReturn(new ClientResponse<>(0, "", new InventoryLockResponse("5d8y6v")));
        when(inventoryTicketPriceClient.getFlightRequest(any())).thenThrow(new NotFoundException());

        final OrderCreated orderCreated = orderService.createOrder(
                new OrderCreate(123, "flightId", ClassType.ECONOMY, BigDecimal.valueOf(2000)));

        assertEquals(234, orderCreated.getId());
    }

    @Test
    void shouldThrowInventoryShortageExceptionWhenGetLockInventoryResponseCodeIs4001() {
        when(inventoryTicketPriceClient.lockInventory(any()))
                .thenReturn(new ClientResponse<>(4001, "inventory is not enough", null));

        assertThrows(InventoryShortageException.class, () -> orderService.createOrder(
                new OrderCreate(123, "flightId", ClassType.ECONOMY, BigDecimal.valueOf(2000))));
    }

    @Test
    void shouldThrowServiceErrorExceptionWhenGetFlightRequestFailedSixTimes() {

        when(inventoryTicketPriceClient.lockInventory(any()))
                .thenThrow(gatewayTimeout);
        when(inventoryTicketPriceClient.getFlightRequest(any()))
                .thenThrow(gatewayTimeout)
                .thenThrow(gatewayTimeout)
                .thenThrow(gatewayTimeout)
                .thenThrow(gatewayTimeout)
                .thenThrow(gatewayTimeout)
                .thenThrow(gatewayTimeout);

        assertThrows(ServiceErrorException.class, () -> orderService.createOrder(
                new OrderCreate(123, "flightId", ClassType.ECONOMY, BigDecimal.valueOf(2000))));
    }

}
