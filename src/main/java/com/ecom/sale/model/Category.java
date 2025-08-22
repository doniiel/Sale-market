package com.ecom.sale.model;

import com.ecom.sale.model.audit.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "category")
public class Category extends AbstractAuditingEntity {

    @Id
    @SequenceGenerator(
            name = "category_seq_gen",
            sequenceName = "category_seq_gen",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_seq_gen"
    )
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

}
