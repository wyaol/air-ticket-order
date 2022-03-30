package com.thoughtworks.airticketorder.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.trafficlistener.ConsoleNotifyingWiremockNetworkTrafficListener;
import com.thoughtworks.airticketorder.client.request.InvoiceIssueRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableFeignClients(basePackages = "com.thoughtworks.airticketorder.client.*")
class InvoiceIssueClientTest {
    private static final WireMockConfiguration wireMockConfiguration = WireMockConfiguration
            .wireMockConfig().networkTrafficListener(
                    new ConsoleNotifyingWiremockNetworkTrafficListener()).port(8002);
    private static final WireMockServer wireMockServer = new WireMockServer(wireMockConfiguration);

    @Autowired
    private InvoiceIssueClient invoiceIssueClient;

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
        ClientResponse<Object> response = invoiceIssueClient.issueInvoice(
                InvoiceIssueRequest.builder()
                        .address("address")
                        .bank("bank")
                        .bankAccount("bankAccount")
                        .email("email")
                        .number("number")
                        .amount(BigDecimal.valueOf(1000))
                        .build()
        );
        assertEquals(0, response.getCode());
    }
}
