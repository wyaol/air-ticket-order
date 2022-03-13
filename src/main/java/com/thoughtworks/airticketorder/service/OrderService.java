package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.request.InventoryLockRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.FlightRequestResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
import com.thoughtworks.airticketorder.exceptions.ThirdServiceException;
import com.thoughtworks.airticketorder.repository.OrderRepository;
import com.thoughtworks.airticketorder.repository.entity.OrderEntity;
import com.thoughtworks.airticketorder.service.dto.OrderCreate;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private InventoryTicketPriceClient inventoryTicketPriceClient;

    private OrderRepository orderRepository;

    public OrderCreated createOrder(OrderCreate orderCreate) {
        final String requestId = UUID.randomUUID().toString();
        String flightOrderId;
        try {
            final ClientResponse<InventoryLockResponse> response = inventoryTicketPriceClient.lockInventory(new InventoryLockRequest(
                    orderCreate.getFlightId(), orderCreate.getClassType(), requestId
            ));
            flightOrderId = response.getData().getFlightOrderId();
        } catch (ThirdServiceException e) {
            final ClientResponse<FlightRequestResponse> response = inventoryTicketPriceClient.getFlightRequest(requestId);
            flightOrderId = response.getData().getFlightOrderId();
        }

        final OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .insuranceOrderId(null)
                .userId(orderCreate.getUserId())
                .luggageWeightOrderId(null)
                .flightOrderId(flightOrderId)
                .amount(orderCreate.getAmount())
                .build());
        return OrderCreated.builder().id(orderEntity.getId()).build();
    }
}
