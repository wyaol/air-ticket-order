package com.thoughtworks.airticketorder.client;

import com.thoughtworks.airticketorder.client.request.InventoryLockRequest;
import com.thoughtworks.airticketorder.client.response.ClientResponse;
import com.thoughtworks.airticketorder.client.response.FlightRequestResponse;
import com.thoughtworks.airticketorder.client.response.InventoryLockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="inventory-ticket-price", url = "${services.inventory-ticket-price.url}")
public interface InventoryTicketPriceClient {
    @PostMapping(value = "/inventory-locks")
    ClientResponse<InventoryLockResponse> lockInventory(InventoryLockRequest inventoryLockRequest);

    @GetMapping(value = "/flight-requests/{request_id}")
    ClientResponse<FlightRequestResponse> getFlightRequest(
            @PathVariable("request_id")String requestId
    );
}
