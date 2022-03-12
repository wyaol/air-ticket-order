package com.thoughtworks.airticketorder.repository;

import com.thoughtworks.airticketorder.repository.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void  shouldCreateOrderSuccess() {
        OrderEntity orderEntity1 = OrderEntity.builder()
                .flightOrderId("54r45g")
                .amount(BigDecimal.valueOf(2000))
                .luggageWeightOrderId("8d6h4e")
                .insuranceOrderId("987676")
                .userId(123)
            .build();
        OrderEntity orderEntity = orderRepository.save(
            orderEntity1
        );
        assertEquals(orderEntity, orderEntity1);
    }
}
