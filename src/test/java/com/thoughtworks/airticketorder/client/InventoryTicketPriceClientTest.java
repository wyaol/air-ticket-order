package com.thoughtworks.airticketorder.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.trafficlistener.ConsoleNotifyingWiremockNetworkTrafficListener;
import com.thoughtworks.airticketorder.client.request.InventoryLockRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.FlightOrderResponse;
import com.thoughtworks.airticketorder.client.response.FlightRequestResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
import com.thoughtworks.airticketorder.dto.ClassType;
import com.thoughtworks.airticketorder.exceptions.NotFoundException;
import com.thoughtworks.airticketorder.exceptions.ServiceErrorException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableFeignClients(basePackages = "com.thoughtworks.airticketorder.client.*")
public class InventoryTicketPriceClientTest {
    private static final WireMockConfiguration wireMockConfiguration = WireMockConfiguration
            .wireMockConfig().networkTrafficListener(
                    new ConsoleNotifyingWiremockNetworkTrafficListener()).port(8001);
    private static final WireMockServer wireMockServer = new WireMockServer(wireMockConfiguration);

    @Autowired
    private InventoryTicketPriceClient inventoryTicketPriceClient;

    @BeforeAll
    static void startWireMock() {
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @Test
    void shouldLockInventorySuccess() {
        ClientResponse<InventoryLockResponse> response = inventoryTicketPriceClient
                .lockInventory(
                        InventoryLockRequest.builder().requestId("123").classType(ClassType.ECONOMY).flightId("096749").build());
        assertEquals("5d8y6v", response.getData().getFlightOrderId());
    }

    @Test
    void shouldGetFlightRequestSuccess() {
        ClientResponse<FlightRequestResponse> response = inventoryTicketPriceClient
                .getFlightRequest("124");
        assertEquals("5d8y6v", response.getData().getFlightOrderId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetFlightRequestReturn404() {
        assertThrows(NotFoundException.class, () -> inventoryTicketPriceClient.getFlightRequest("125"));
    }

    @Test
    void shouldGetFlightOrderSuccess() {
        ClientResponse<FlightOrderResponse> response = inventoryTicketPriceClient
                .getFlightOrder("5d8y6v");
        assertEquals(BigDecimal.valueOf(1000), response.getData().getAmount());
    }
}
