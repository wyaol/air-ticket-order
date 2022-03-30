package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.InvoiceIssueClient;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.FlightOrderResponse;
import com.thoughtworks.airticketorder.dto.InvoiceStatus;
import com.thoughtworks.airticketorder.repository.InvoiceRepository;
import com.thoughtworks.airticketorder.repository.entity.InvoiceEntity;
import com.thoughtworks.airticketorder.service.dto.InvoiceSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceIssueClient invoiceIssueClient;

    @Mock
    private InventoryTicketPriceClient inventoryTicketPriceClient;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateInvoiceSuccess() {

        final InvoiceEntity invoiceEntity = InvoiceEntity.builder().id(234)
                .address("address")
                .bank("bank")
                .bankAccount("bankAccount")
                .email("email")
                .number("number")
                .flightOrderId("flightOrderId")
                .build();
        when(invoiceRepository.save(any())).thenReturn(
                invoiceEntity
        );

        Integer id = invoiceService.createInvoice(
                InvoiceSource.builder()
                        .address("address")
                        .bank("bank")
                        .bankAccount("bankAccount")
                        .email("email")
                        .number("number")
                        .flightOrderId("flightOrderId")
                        .build());

        assertEquals(234, id);
    }

    @Test
    void shouldIssueInvoiceSuccess() {

        final InvoiceEntity invoiceEntity = InvoiceEntity.builder().id(234)
                .address("address")
                .bank("bank")
                .bankAccount("bankAccount")
                .email("email")
                .number("number")
                .flightOrderId("flightOrderId")
                .status(InvoiceStatus.NOT_ISSUED)
                .build();
        when(invoiceRepository.findAllByStatus(any())).thenReturn(
                List.of(invoiceEntity)
        );
        invoiceEntity.setStatus(InvoiceStatus.ISSUED);
        when(invoiceRepository.save(any())).thenReturn(
                invoiceEntity
        );
        when(invoiceIssueClient.issueInvoice(any())).thenReturn(new ClientResponse<>(0, "", null));
        when(inventoryTicketPriceClient.getFlightOrder(any())).thenReturn(
                new ClientResponse<>(0, "", FlightOrderResponse.builder().amount(BigDecimal.valueOf(1000)).build()));

        assertDoesNotThrow(() -> invoiceService.requestInvoices());
    }
}
