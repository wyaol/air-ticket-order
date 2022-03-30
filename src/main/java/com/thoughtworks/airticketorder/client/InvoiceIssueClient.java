package com.thoughtworks.airticketorder.client;

import com.thoughtworks.airticketorder.client.request.InvoiceIssueRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="invoice-issue", url = "${services.invoice-issue.url}")
public interface InvoiceIssueClient {
    @PostMapping(value = "/invoices")
    ClientResponse<Object> issueInvoice(InvoiceIssueRequest invoiceIssueRequest);
}
