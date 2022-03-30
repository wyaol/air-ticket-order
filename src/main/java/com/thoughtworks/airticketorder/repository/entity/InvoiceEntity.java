package com.thoughtworks.airticketorder.repository.entity;

import com.thoughtworks.airticketorder.dto.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_invoice")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String flightOrderId;
    private String bank;
    private String bankAccount;
    private String email;
    private String address;
    private String number;
    private InvoiceStatus status;
    @LastModifiedDate
    private Long updateTime;
}
