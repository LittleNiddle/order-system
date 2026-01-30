package com.beyond.order_system.ordering.dto;

import com.beyond.order_system.ordering.domain.OrderStatus;
import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.domain.OrderingDetail;
import com.beyond.order_system.ordering.repository.OrderingDetailRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderingDetailListResDto {
    private Long id;
    private String memberEmail;
    private OrderStatus orderStatus;
    @Builder.Default
    private List<OrderingDetailResDto> orderingDetailResDtoList = new ArrayList<>();

    public static OrderingDetailListResDto fromEntity(Ordering order, List<OrderingDetail> orderingDetails){

        List<OrderingDetailResDto> detailDtos =
                orderingDetails.stream()
                        .map(OrderingDetailResDto::fromEntity)
                        .toList();

        return OrderingDetailListResDto.builder()
                .id(order.getId())
                .memberEmail(order.getMember().getEmail())
                .orderStatus(order.getOrderStatus())
                .orderingDetailResDtoList(detailDtos)
                .build();
    }
}
