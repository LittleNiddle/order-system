package com.beyond.order_system.ordering.dto;

import com.beyond.order_system.ordering.domain.OrderingDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderingDetailResDto {
    private Long id;
    private String name;
    private int productCount;

    public static OrderingDetailResDto fromEntity(OrderingDetail orderingDetail){
        return OrderingDetailResDto.builder()
                .id(orderingDetail.getId())
                .name(orderingDetail.getProduct().getName())
                .productCount(orderingDetail.getQuantity())
                .build();
    }
}
