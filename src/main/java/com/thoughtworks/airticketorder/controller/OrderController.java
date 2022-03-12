package com.thoughtworks.airticketorder.controller;

import com.thoughtworks.airticketorder.controller.request.OrderCreateRequest;
import com.thoughtworks.airticketorder.controller.response.OrderCreateResponse;
import com.thoughtworks.airticketorder.controller.response.Response;
import com.thoughtworks.airticketorder.service.OrderService;
import com.thoughtworks.airticketorder.service.dto.OrderCreate;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import com.thoughtworks.airticketorder.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/air-ticket-orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<OrderCreateResponse> createOrder(
            @RequestHeader Integer userId,
            @RequestBody OrderCreateRequest orderCreateRequest
    ) {
        final OrderCreate orderCreate = ObjectMapperUtil.convert(orderCreateRequest, OrderCreate.class);
        orderCreate.setUserId(userId);
        final OrderCreated orderCreated = orderService.createOrder(orderCreate);
        return Response.success(ObjectMapperUtil.convert(orderCreated, OrderCreateResponse.class));
    }

}