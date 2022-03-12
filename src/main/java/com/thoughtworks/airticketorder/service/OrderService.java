package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.request.InventoryLockRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
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
        final ClientResponse<InventoryLockResponse> response = inventoryTicketPriceClient.lockInventory(new InventoryLockRequest(
                orderCreate.getFlightId(), orderCreate.getClassType(), UUID.randomUUID().toString()
        ));
        final OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .insuranceOrderId(null)
                .userId(orderCreate.getUserId())
                .luggageWeightOrderId(null)
                .flightOrderId(response.getData().getFlightOrderId())
                .amount(orderCreate.getAmount())
                .build());
        return OrderCreated.builder().id(orderEntity.getId()).build();
    }
}
