package com.thoughtworks.airticketorder.repository;

import com.thoughtworks.airticketorder.repository.entity.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Integer> {
}
