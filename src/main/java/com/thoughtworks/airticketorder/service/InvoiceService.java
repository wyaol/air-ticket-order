package com.thoughtworks.airticketorder.service;

import com.thoughtworks.airticketorder.dto.InvoiceStatus;
import com.thoughtworks.airticketorder.repository.InvoiceRepository;
import com.thoughtworks.airticketorder.repository.entity.InvoiceEntity;
import com.thoughtworks.airticketorder.service.dto.InvoiceSource;
import com.thoughtworks.airticketorder.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private InvoiceRepository invoiceRepository;

    public Integer createInvoice(InvoiceSource invoiceSource) {
        final InvoiceEntity invoiceEntity = ObjectMapperUtil.convert(invoiceSource, InvoiceEntity.class);
        invoiceEntity.setStatus(InvoiceStatus.NOT_ISSUE);
        return invoiceRepository.save(invoiceEntity).getId();
    }
}
