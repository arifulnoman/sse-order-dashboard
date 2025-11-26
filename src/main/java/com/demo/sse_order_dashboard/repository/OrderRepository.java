package com.demo.sse_order_dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.sse_order_dashboard.model.Order;
import com.demo.sse_order_dashboard.model.Order.Status;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	long countByStatus(Status status);
}
