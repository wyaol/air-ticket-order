package com.thoughtworks.airticketorder.repository;


import com.thoughtworks.airticketorder.repository.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {

}
