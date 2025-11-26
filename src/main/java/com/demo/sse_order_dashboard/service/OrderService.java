package com.demo.sse_order_dashboard.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.demo.sse_order_dashboard.model.DashboardCount;
import com.demo.sse_order_dashboard.model.Order;
import com.demo.sse_order_dashboard.model.Order.Status;
import com.demo.sse_order_dashboard.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

	public static final String DASHBOARD_CHANNEL = "order-dashboard";

	private final long DASHBOARD_EMITTER_TIMEOUT_MS = 30 * 60 * 1000L; // 30 minutes
	
	private final OrderRepository orderRepository;

    private final SseChannelService sseChannelService;

    public Order createOrder(String customerName) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setStatus(Status.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        broadcastDashboardCounts();
        return saved;
    }

    public Order updateStatus(Long id, Status status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        broadcastDashboardCounts();
        return saved;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public SseEmitter subscribeToDashboard() {
        SseEmitter emitter = sseChannelService.subscribe(DASHBOARD_CHANNEL, DASHBOARD_EMITTER_TIMEOUT_MS);

        // send initial snapshot immediately
        DashboardCount counts = calculateDashboardCount();
        try {
            emitter.send(SseEmitter.event()
                    .name("order-dashboard")
                    .data(counts));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    public void broadcastDashboardCounts() {
        DashboardCount counts = calculateDashboardCount();
        sseChannelService.sendToChannel(DASHBOARD_CHANNEL, "order-dashboard", counts);
    }

    private DashboardCount calculateDashboardCount() {
        long pending = orderRepository.countByStatus(Status.PENDING);
        long approved = orderRepository.countByStatus(Status.APPROVED);
        long delivered = orderRepository.countByStatus(Status.DELIVERED);
        long total = pending + approved + delivered;

        return new DashboardCount(pending, approved, delivered, total);
    }
}
