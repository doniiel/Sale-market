package com.ecom.sale.model.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditingEntity {

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;


    public AbstractAuditingEntity() {
        this.createdBy = "system";
        this.updatedBy = "system";
        this.createdDate = Instant.now();
        this.updatedDate = Instant.now();
    }
}
