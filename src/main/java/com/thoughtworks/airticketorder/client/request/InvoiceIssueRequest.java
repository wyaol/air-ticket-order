package com.thoughtworks.airticketorder.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InvoiceIssueRequest {
    private String bank;
    @JsonProperty("bank_account")
    private String bankAccount;
    private String email;
    private String address;
    private String number;
    private BigDecimal amount;
}
