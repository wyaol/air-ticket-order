package com.thoughtworks.airticketorder.controller;

import com.thoughtworks.airticketorder.controller.request.InvoiceRequest;
import com.thoughtworks.airticketorder.controller.request.OrderCreateRequest;
import com.thoughtworks.airticketorder.controller.response.OrderCreateResponse;
import com.thoughtworks.airticketorder.controller.response.Response;
import com.thoughtworks.airticketorder.exceptions.BusinessException;
import com.thoughtworks.airticketorder.exceptions.ServiceErrorException;
import com.thoughtworks.airticketorder.service.OrderService;
import com.thoughtworks.airticketorder.service.dto.InvoiceSource;
import com.thoughtworks.airticketorder.service.dto.OrderCreate;
import com.thoughtworks.airticketorder.service.dto.OrderCreated;
import com.thoughtworks.airticketorder.util.ObjectMapperUtil;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/{flightOrderId}/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public Response<OrderCreateResponse> createInvoice(
            @PathVariable String flightOrderId,
            @RequestBody InvoiceRequest invoiceRequest
    ) {
        final InvoiceSource invoiceSource = ObjectMapperUtil.convert(invoiceRequest, InvoiceSource.class);
        invoiceSource.setFlightOrderId(flightOrderId);
        orderService.createInvoice(invoiceSource);
        return Response.success(null);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Object> handlerBusinessException(BusinessException businessException) {
        return new Response<>(businessException.getCode(), businessException.getMsg(), null);
    }

    @ExceptionHandler({ServiceErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Object> handlerBusinessException(Exception e) {
        return new Response<>(5000, "unknown error", null);
    }

}
