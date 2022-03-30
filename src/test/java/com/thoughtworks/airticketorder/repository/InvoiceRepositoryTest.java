package com.thoughtworks.airticketorder.repository;

import com.thoughtworks.airticketorder.dto.InvoiceStatus;
import com.thoughtworks.airticketorder.repository.entity.InvoiceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void  shouldCreateInvoiceSuccess() {
        InvoiceEntity invoiceEntity = InvoiceEntity.builder()
                .address("address")
                .bank("bank")
                .bankAccount("bankAccount")
                .email("email")
                .number("number")
                .flightOrderId("flightOrderId")
                .build();
        InvoiceEntity invoiceEntity1 = invoiceRepository.save(
                invoiceEntity
        );
        assertEquals(invoiceEntity, invoiceEntity1);
    }

    @Test
    void  shouldFindAllByStatus() {
        InvoiceEntity invoiceEntity = InvoiceEntity.builder()
                .address("address")
                .bank("bank")
                .bankAccount("bankAccount")
                .email("email")
                .number("number")
                .flightOrderId("flightOrderId")
                .status(InvoiceStatus.ISSUED)
                .build();
        invoiceRepository.save(invoiceEntity);

        final List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllByStatus(InvoiceStatus.ISSUED);

        assertEquals(1, invoiceEntities.size());
    }
}
