package com.beyond.order_system.ordering.repository;

import com.beyond.order_system.ordering.domain.OrderingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderingDetailRepository extends JpaRepository<OrderingDetail, Long> {
}
