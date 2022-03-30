package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.repository.InvoiceRepository;
import com.thoughtworks.airticketorder.repository.entity.InvoiceEntity;
import com.thoughtworks.airticketorder.service.dto.InvoiceSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

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
}
