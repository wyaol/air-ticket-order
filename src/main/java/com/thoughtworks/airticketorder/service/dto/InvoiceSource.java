package com.thoughtworks.airticketorder.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InvoiceSource {
    private String email;
    private String address;
    private String bank;
    private String bankAccount;
    private String number;
    private String flightOrderId;
}
