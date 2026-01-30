package com.beyond.order_system.ordering.controller;

import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.dto.OrderingCreateListReqDto;
import com.beyond.order_system.ordering.dto.OrderingDetailListResDto;
import com.beyond.order_system.ordering.dto.OrderingDetailResDto;
import com.beyond.order_system.ordering.servicec.OrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(ordering.getId());
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll(){
        List<OrderingDetailListResDto> dtoList = orderingService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @GetMapping("/myorders")
    public ResponseEntity<?> findMyInfo(@AuthenticationPrincipal String principal){
        List<OrderingDetailResDto> dtoList = orderingService.findMyInfo(principal);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
}
