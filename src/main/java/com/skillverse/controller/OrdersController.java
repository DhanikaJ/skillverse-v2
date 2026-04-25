package com.skillverse.controller;

import com.skillverse.model.Orders;
import com.skillverse.model.OrderItem;
import com.skillverse.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * Create a new order for a user
     * POST /api/v1/orders?userId=1
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestParam Integer userId) {
        try {
            Orders order = ordersService.createOrder(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order created successfully");
            response.put("orderId", order.getId());
            response.put("userId", order.getUser().getId());
            response.put("status", order.getStatus());
            response.put("total", order.getTotal());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error creating order: " + e.getMessage()
            ));
        }
    }

    /**
     * Add item to an order
     * POST /api/v1/orders/1/items?courseId=2&price=99.99
     */
    @PostMapping("/{orderId}/items")
    public ResponseEntity<Map<String, Object>> addItemToOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer courseId,
            @RequestParam Double price) {
        try {
            OrderItem item = ordersService.addItemToOrder(orderId, courseId, price);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item added to order successfully");
            response.put("itemId", item.getId());
            response.put("courseId", item.getCourse().getId());
            response.put("price", item.getPrice());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error adding item to order: " + e.getMessage()
            ));
        }
    }

    /**
     * Get order by ID
     * GET /api/v1/orders/1
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Integer orderId) {
        try {
            Optional<Orders> order = ordersService.getOrderById(orderId);
            if (order.isPresent()) {
                return ResponseEntity.ok(order.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all orders for a user
     * GET /api/v1/orders/user/1
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Orders>> getUserOrders(@PathVariable Integer userId) {
        try {
            List<Orders> orders = ordersService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all orders by status
     * GET /api/v1/orders?status=PENDING
     */
    @GetMapping
    public ResponseEntity<List<Orders>> getOrdersByStatus(@RequestParam(required = false) String status) {
        try {
            List<Orders> orders;
            if (status != null && !status.isEmpty()) {
                orders = ordersService.getOrdersByStatus(status);
            } else {
                orders = ordersService.getAllOrders();
            }
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update order status
     * PUT /api/v1/orders/1?status=COMPLETED
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String status) {
        try {
            Orders order = ordersService.updateOrderStatus(orderId, status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order status updated successfully");
            response.put("orderId", order.getId());
            response.put("status", order.getStatus());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error updating order status: " + e.getMessage()
            ));
        }
    }
}

