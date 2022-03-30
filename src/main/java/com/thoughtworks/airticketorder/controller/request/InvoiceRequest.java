package com.thoughtworks.airticketorder.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InvoiceRequest {
    private String email;
    private String address;
    private String bank;
    private String bankAccount;
    private String number;
}
