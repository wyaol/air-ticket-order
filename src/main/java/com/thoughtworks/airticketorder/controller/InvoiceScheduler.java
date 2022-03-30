package com.thoughtworks.airticketorder.controller;

import com.thoughtworks.airticketorder.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceScheduler {

    private InvoiceService invoiceService;

    @Scheduled(fixedRate = 3000)
    public void issueInvoice() {
        invoiceService.requestInvoices();
    }
}
