package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.client.InventoryTicketPriceClient;
import com.thoughtworks.airticketorder.client.InvoiceIssueClient;
import com.thoughtworks.airticketorder.client.request.InvoiceIssueRequest;
import com.thoughtworks.airticketorder.dto.InvoiceStatus;
import com.thoughtworks.airticketorder.repository.InvoiceRepository;
import com.thoughtworks.airticketorder.repository.entity.InvoiceEntity;
import com.thoughtworks.airticketorder.service.dto.InvoiceSource;
import com.thoughtworks.airticketorder.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    private InvoiceIssueClient invoiceIssueClient;

    private InventoryTicketPriceClient inventoryTicketPriceClient;

    public Integer createInvoice(InvoiceSource invoiceSource) {
        final InvoiceEntity invoiceEntity = ObjectMapperUtil.convert(invoiceSource, InvoiceEntity.class);
        invoiceEntity.setStatus(InvoiceStatus.NOT_ISSUED);
        return invoiceRepository.save(invoiceEntity).getId();
    }

    public void requestInvoices() {
        final List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllByStatus(InvoiceStatus.NOT_ISSUED);
        invoiceEntities.forEach(it -> {
            final BigDecimal amount = inventoryTicketPriceClient.getFlightOrder(it.getFlightOrderId()).getData().getAmount();
            final InvoiceIssueRequest invoiceIssueRequest = InvoiceIssueRequest.builder()
                    .amount(amount)
                    .address(it.getAddress())
                    .bank(it.getBank())
                    .bankAccount(it.getBankAccount())
                    .email(it.getEmail())
                    .number(it.getNumber())
                    .build();
            invoiceIssueClient.issueInvoice(
                    invoiceIssueRequest
            );
            it.setStatus(InvoiceStatus.ISSUED);
            invoiceRepository.save(it);
        });
    }
}
