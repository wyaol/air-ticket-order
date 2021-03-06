package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.request.InventoryLockRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.FlightRequestResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
import com.thoughtworks.airticketorder.exceptions.InventoryShortageException;
import com.thoughtworks.airticketorder.exceptions.NotFoundException;
import com.thoughtworks.airticketorder.exceptions.ServiceErrorException;
import com.thoughtworks.airticketorder.repository.OrderRepository;
import com.thoughtworks.airticketorder.repository.entity.OrderEntity;
import com.thoughtworks.airticketorder.service.dto.InvoiceSource;
import com.thoughtworks.airticketorder.service.dto.OrderCreate;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OrderService {
    private InventoryTicketPriceClient inventoryTicketPriceClient;

    private OrderRepository orderRepository;

    private static final Map<Integer, RuntimeException> CLIENT_CODE_TO_EXCEPTION = Map.of(
            4001, new InventoryShortageException(4001, "inventory is not enough")
    );

    public OrderCreated createOrder(OrderCreate orderCreate) {
        String flightOrderId = retryMethod(this::getFlightOrderId, orderCreate, FlightRequestNotFoundException.class, 6);

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
        final String requestId = UUID.randomUUID().toString();
        String flightOrderId;
        try {
            final InventoryLockResponse response = extractClientResponse(inventoryTicketPriceClient.lockInventory(
                    new InventoryLockRequest(
                            orderCreate.getFlightId(), orderCreate.getClassType(), requestId
                    )));
            flightOrderId = response.getFlightOrderId();
        } catch (FeignException.GatewayTimeout e) {
            try {
                flightOrderId = retryMethod(this::getFlightRequest, requestId, FeignException.GatewayTimeout.class, 6 );
            } catch (NotFoundException notFoundException) {
                throw new FlightRequestNotFoundException();
            }
        }
        return flightOrderId;
    }

    private String getFlightRequest(String requestId) {
        String flightOrderId;
        try {
            final FlightRequestResponse response = extractClientResponse(inventoryTicketPriceClient.getFlightRequest(requestId));
            flightOrderId = response.getFlightOrderId();
        } catch (ServiceErrorException e) {
            throw new ServiceErrorWaitForRetryException();
        }
        return flightOrderId;
    }

    private <T, R, E> R retryMethod(Function<T, R> function, T t, Class<E> exception, int maxRetryTime) {
        return _retryMethod(function, t,  0, maxRetryTime, exception);
    }

    private <T, R, E> R _retryMethod(Function<T, R> function, T t, int retryTime, int maxRetryTime, Class<E> exception) {
        if (retryTime == maxRetryTime) throw new ServiceErrorException();
        try {
            return function.apply(t);
        } catch (Throwable e) {
            if (exception.isInstance(e)) {
                return _retryMethod(function, t, retryTime + 1, maxRetryTime, exception);
            } else {
                throw e;
            }
        }
    }

    private static class ServiceErrorWaitForRetryException extends ServiceErrorException {}
    private static class FlightRequestNotFoundException extends NotFoundException {}

    private <T> T extractClientResponse(ClientResponse<T> clientResponse) {
        final Integer code = clientResponse.getCode();
        if (code != 0) {
            if (CLIENT_CODE_TO_EXCEPTION.containsKey(code)) throw CLIENT_CODE_TO_EXCEPTION.get(code);
            throw new ServiceErrorException();
        }
        return clientResponse.getData();
    }
}
