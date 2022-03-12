package com.thoughtworks.airticketorder.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InventoryLockResponse {
    @JsonProperty("flight_order_id")
    private String flightOrderId;
}
