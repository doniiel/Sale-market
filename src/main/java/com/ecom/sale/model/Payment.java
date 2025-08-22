package com.ecom.sale.model;

import com.ecom.sale.enums.PaymentMethod;
import com.ecom.sale.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@ToString
@Table(name = "payment")
public class Payment {

    @Id
    @SequenceGenerator(
            name = "payment_seq_gen",
            sequenceName = "payment_seq_gen",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payment_seq_gen"
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

}
