package com.beyond.order_system.ordering.servicec;

import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.repository.MemberRepository;
import com.beyond.order_system.ordering.domain.OrderStatus;
import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.domain.OrderingDetail;
import com.beyond.order_system.ordering.dto.OrderingCreateListReqDto;
import com.beyond.order_system.ordering.dto.OrderingDetailListResDto;
import com.beyond.order_system.ordering.dto.OrderingDetailResDto;
import com.beyond.order_system.ordering.repository.OrderingDetailRepository;
import com.beyond.order_system.ordering.repository.OrderingRepository;
import com.beyond.order_system.product.domain.Product;
import com.beyond.order_system.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderingService {
    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final OrderingDetailRepository orderingDetailRepository;
    private final ProductRepository productRepository;
    @Autowired
    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository, OrderingDetailRepository orderingDetailRepository, ProductRepository productRepository) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
        this.orderingDetailRepository = orderingDetailRepository;
        this.productRepository = productRepository;
    }

    public Ordering create(List<OrderingCreateListReqDto> listReqDtoList) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("해당 이메일이 없습니다."));
        Ordering ordering = Ordering.builder()
                .orderStatus(OrderStatus.ORDERED)
                .member(member)
                .build();
        Ordering orderingDb = orderingRepository.save(ordering);

        for(OrderingCreateListReqDto od : listReqDtoList){
            Product product = productRepository.findById(od.getProductId()).orElseThrow(()->new NoSuchElementException("해당 상품이 없습니다"));
            orderingDetailRepository.save(od.toEntity(orderingDb, product));
        }

        return orderingDb;
    }

    public List<OrderingDetailListResDto> findAll() {
        List<Ordering> orderingList = orderingRepository.findAll();
        List<OrderingDetailListResDto> resDtoList = new ArrayList<>();
        for(Ordering ordering : orderingList){
            List<OrderingDetail> orderingDetailList = orderingDetailRepository.findAllByOrderId(ordering.getId());
            resDtoList.add(OrderingDetailListResDto.fromEntity(ordering, orderingDetailList));
        }
        return resDtoList;
    }

    public List<OrderingDetailResDto> findMyInfo(String principal) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("해당 이메일이 없습니다."));
        List<Ordering> orderingList = orderingRepository.findAllByMemberId(member.getId());
        List<OrderingDetailResDto> resDtoList = new ArrayList<>();
        for(Ordering ordering : orderingList){
            List<OrderingDetail> orderingDetailList = orderingDetailRepository.findAllByOrderId(ordering.getId());
            for(OrderingDetail od : orderingDetailList){
                resDtoList.add(OrderingDetailResDto.fromEntity(od));
            }
        }
        return resDtoList;
    }
}
