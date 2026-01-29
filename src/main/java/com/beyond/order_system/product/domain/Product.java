package com.beyond.order_system.product.domain;

import com.beyond.order_system.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
@Builder
@Entity
public class Product {
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

    public void updateProfileImageUrl(String url){
        this.image_path = url;
    }
}
