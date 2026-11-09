package com.skillverse.controller;

import com.skillverse.dto.OrderDTO;
import com.skillverse.model.OrderItem;
import com.skillverse.model.Orders;
import com.skillverse.service.OrdersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Orders belong to the authenticated user. Ownership and role checks are enforced in
 * {@link OrdersService}; failures surface as 403/404 through the global exception handler.
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order management endpoints")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    /**
     * Create a new order for the authenticated user.
     * POST /api/v1/orders
     * (Admins may pass ?userId= to create an order for another user.)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestParam(required = false) Integer userId) {
        Orders order = ordersService.createOrder(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order created successfully");
        response.put("orderId", order.getId());
        response.put("userId", order.getUser().getId());
        response.put("status", order.getStatus());
        response.put("total", order.getTotal());
        return ResponseEntity.ok(response);
    }

    /**
     * Add a course to an order.
     * POST /api/v1/orders/1/items?courseId=2
     * The price is taken from the course; a {@code price} parameter, if sent, is ignored.
     */
    @PostMapping("/{orderId}/items")
    public ResponseEntity<Map<String, Object>> addItemToOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer courseId,
            @RequestParam(required = false) Double price) {
        OrderItem item = ordersService.addItemToOrder(orderId, courseId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Item added to order successfully");
        response.put("itemId", item.getId());
        response.put("courseId", item.getCourse().getId());
        response.put("price", item.getPrice());
        return ResponseEntity.ok(response);
    }

    /**
     * Get order by ID (owner or admin).
     * GET /api/v1/orders/1
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(new OrderDTO(ordersService.getOrderById(orderId)));
    }

    /**
     * Get all orders for a user (that user or admin).
     * GET /api/v1/orders/user/1
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Integer userId) {
        List<OrderDTO> orders = ordersService.getUserOrders(userId).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    /**
     * List orders, optionally filtered by status (admin only).
     * GET /api/v1/orders?status=PENDING
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestParam(required = false) String status) {
        List<Orders> orders = (status != null && !status.isEmpty())
                ? ordersService.getOrdersByStatus(status)
                : ordersService.getAllOrders();
        return ResponseEntity.ok(orders.stream().map(OrderDTO::new).collect(Collectors.toList()));
    }

    /**
     * Update order status (admin only).
     * PUT /api/v1/orders/1?status=COMPLETED
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String status) {
        Orders order = ordersService.updateOrderStatus(orderId, status);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order status updated successfully");
        response.put("orderId", order.getId());
        response.put("status", order.getStatus());
        return ResponseEntity.ok(response);
    }
}
