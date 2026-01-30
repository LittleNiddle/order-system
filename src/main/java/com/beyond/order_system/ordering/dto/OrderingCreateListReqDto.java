package com.beyond.order_system.ordering.dto;

import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.domain.OrderingDetail;
import com.beyond.order_system.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderingCreateListReqDto {
    private Long productId;
    private int productCount;

    public OrderingDetail toEntity(Ordering ordering, Product product){
        return OrderingDetail.builder()
                .order(ordering)
                .product(product)
                .quantity(this.productCount)
                .build();
    }
}
