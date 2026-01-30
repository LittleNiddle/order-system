package com.beyond.order_system.ordering.domain;

import com.beyond.order_system.common.domain.BaseTimeEntity;
import com.beyond.order_system.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
@Builder
@Entity
public class OrderingDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false)
    private Ordering order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;
}
