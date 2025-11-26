package com.demo.sse_order_dashboard.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.demo.sse_order_dashboard.model.CreateOrderRequest;
import com.demo.sse_order_dashboard.model.Order;
import com.demo.sse_order_dashboard.model.Order.Status;
import com.demo.sse_order_dashboard.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.getCustomerName());
    }

	@PutMapping("/{id}/status")
	public Order updateStatus(@PathVariable Long id, @RequestParam Status status) {
		return orderService.updateStatus(id, status);
	}

    @GetMapping
    public List<Order> listOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping(path = "/dashboard/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamDashboard() {
        return orderService.subscribeToDashboard();
    }
}
