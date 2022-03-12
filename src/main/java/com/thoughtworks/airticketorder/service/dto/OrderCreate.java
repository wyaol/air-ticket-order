package com.thoughtworks.airticketorder.service.dto;

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
public class OrderCreate {
    private Integer userId;
    private String flightId;
    private ClassType classType;
    private BigDecimal amount;
}
