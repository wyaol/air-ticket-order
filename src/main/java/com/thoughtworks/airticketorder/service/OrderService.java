package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.request.InventoryLockRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.FlightRequestResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
import com.thoughtworks.airticketorder.exceptions.NotFoundException;
import com.thoughtworks.airticketorder.exceptions.ServiceErrorException;
import com.thoughtworks.airticketorder.repository.OrderRepository;
import com.thoughtworks.airticketorder.repository.entity.OrderEntity;
import com.thoughtworks.airticketorder.service.dto.OrderCreate;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OrderService {
    private InventoryTicketPriceClient inventoryTicketPriceClient;

    private OrderRepository orderRepository;

    public OrderCreated createOrder(OrderCreate orderCreate) {
        String flightOrderId = getFlightOrderId(orderCreate);

        final OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .insuranceOrderId(null)
                .userId(orderCreate.getUserId())
                .luggageWeightOrderId(null)
                .flightOrderId(flightOrderId)
                .amount(orderCreate.getAmount())
                .build());
        return OrderCreated.builder().id(orderEntity.getId()).build();
    }

    private String getFlightOrderId(OrderCreate orderCreate) {
        return retryGetFlightOrderId(orderCreate, 0, 6);
    }

    private String retryGetFlightOrderId(OrderCreate orderCreate, int retryTime, int maxRetryTime) {
        if (retryTime == maxRetryTime) throw new ServiceErrorException();
        try {
            return _getFlightOrderId(orderCreate);
        } catch (FlightRequestNotFoundException e) {
            return retryGetFlightOrderId(orderCreate, retryTime + 1, maxRetryTime);
        }
    }

    private String _getFlightOrderId(OrderCreate orderCreate) {
        final String requestId = UUID.randomUUID().toString();
        String flightOrderId;
        try {
            final ClientResponse<InventoryLockResponse> response = inventoryTicketPriceClient.lockInventory(new InventoryLockRequest(
                    orderCreate.getFlightId(), orderCreate.getClassType(), requestId
            ));
            flightOrderId = response.getData().getFlightOrderId();
        } catch (ServiceErrorException e) {
            try {
                final ClientResponse<FlightRequestResponse> response = inventoryTicketPriceClient.getFlightRequest(requestId);
                flightOrderId = response.getData().getFlightOrderId();
            } catch (NotFoundException notFoundException) {
                throw new FlightRequestNotFoundException();
            }
        }
        return flightOrderId;
    }

    private static class FlightRequestNotFoundException extends NotFoundException { }
}
