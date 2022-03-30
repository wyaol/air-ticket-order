package com.thoughtworks.airticketorder.controller;

import com.thoughtworks.airticketorder.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;

class InvoiceSchedulerTest {
    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    InvoiceScheduler invoiceScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldIssueInvoiceSuccess() {

        doNothing().when(invoiceService).requestInvoices();

        assertDoesNotThrow(() ->
                invoiceScheduler.issueInvoice()
        );
    }
}
