package com.ecom.sale.model;

import com.ecom.sale.model.audit.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends AbstractAuditingEntity {

    @Id
    @SequenceGenerator(
            name = "role_seq_gen",
            sequenceName = "role_seq_gen",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_seq_gen"
    )
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

}
