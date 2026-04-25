package com.skillverse.service;

import com.skillverse.model.*;
import com.skillverse.repository.OrdersRepository;
import com.skillverse.repository.OrderItemRepository;
import com.skillverse.repository.UsersRepository;
import com.skillverse.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Create an order
    public Orders createOrder(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setTotal(0.0);
        order.setCreated_at(new Date());

        return ordersRepository.save(order);
    }

    // Add item to order
    public OrderItem addItemToOrder(Integer orderId, Integer courseId, Double price) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        OrderItem item = new OrderItem();
        item.setOrders(order);
        item.setCourse(course);
        item.setPrice(price);

        order.addOrderItem(item);

        // Update order total
        Double newTotal = order.getTotal() + price;
        order.setTotal(newTotal);
        ordersRepository.save(order);

        return orderItemRepository.save(item);
    }

    // Get order by ID
    public Optional<Orders> getOrderById(Integer id) {
        return ordersRepository.findById(id);
    }

    // Get all orders for a user
    public List<Orders> getUserOrders(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ordersRepository.findByUser(user);
    }

    // Get all orders by status
    public List<Orders> getOrdersByStatus(String status) {
        return ordersRepository.findByStatus(status);
    }

    // Update order status
    public Orders updateOrderStatus(Integer orderId, String status) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return ordersRepository.save(order);
    }

    // Get all orders
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }
}

