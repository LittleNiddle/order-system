package com.beyond.order_system.ordering.controller;

import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.dto.OrderingCreateListReqDto;
import com.beyond.order_system.ordering.servicec.OrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ordering")
public class OrderingController {
    private final OrderingService orderingService;
    @Autowired
    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody List<OrderingCreateListReqDto> listReqDtoList){
        Ordering ordering = orderingService.create(listReqDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(ordering.getId());
    }
}
