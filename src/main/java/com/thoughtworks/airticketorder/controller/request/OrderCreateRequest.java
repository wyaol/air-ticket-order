package com.thoughtworks.airticketorder.controller.request;

import com.thoughtworks.airticketorder.dto.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderCreateRequest {
    private String flightId;
    private ClassType classType;
    private BigDecimal amount;
}
