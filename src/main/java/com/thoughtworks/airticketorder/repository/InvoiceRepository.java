package com.thoughtworks.airticketorder.repository;

import com.thoughtworks.airticketorder.dto.InvoiceStatus;
import com.thoughtworks.airticketorder.repository.entity.InvoiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Integer> {
    List<InvoiceEntity> findAllByStatus(InvoiceStatus status);
}
