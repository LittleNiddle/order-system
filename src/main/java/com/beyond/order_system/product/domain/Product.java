package com.beyond.order_system.product.domain;

import com.beyond.order_system.common.domain.BaseTimeEntity;
import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.ordering.domain.OrderingDetail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
@Builder
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    private String category;
    private int stockQuantity;
    private String image_path;
    @Builder.Default
    private LocalDateTime createdTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false)
    private Member member;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderingDetail> orderingDetailList = new ArrayList<>();

    public void updateProfileImageUrl(String url){
        this.image_path = url;
    }
}
