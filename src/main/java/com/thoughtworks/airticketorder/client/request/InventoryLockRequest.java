package com.thoughtworks.airticketorder.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.airticketorder.dto.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InventoryLockRequest {
    @JsonProperty("flight_id")
    private String flightId;
    @JsonProperty("class_type")
    private ClassType classType;
    @JsonProperty("request_id")
    private String requestId;
}
