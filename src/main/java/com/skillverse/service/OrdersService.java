package com.skillverse.service;

import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.exception.ValidationException;
import com.skillverse.model.*;
import com.skillverse.repository.OrdersRepository;
import com.skillverse.repository.OrderItemRepository;
import com.skillverse.repository.UsersRepository;
import com.skillverse.repository.CourseRepository;
import com.skillverse.security.AccessPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service class for managing Order operations.
 * Orders belong to the authenticated user; item prices are always taken from the course.
 */
@Service
public class OrdersService {

    private static final String STATUS_PENDING = "PENDING";

    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;
    private final AccessPolicy accessPolicy;

    public OrdersService(
            OrdersRepository ordersRepository,
            OrderItemRepository orderItemRepository,
            UsersRepository usersRepository,
            CourseRepository courseRepository,
            AccessPolicy accessPolicy
    ) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
        this.accessPolicy = accessPolicy;
    }

    /**
     * Creates a new order. Regular users can only create orders for themselves;
     * administrators may create one for another user by passing {@code userId}.
     *
     * @param requestedUserId optional target user (must be the caller unless the caller is an admin)
     * @return the created Orders entity
     */
    @Transactional
    public Orders createOrder(Integer requestedUserId) {
        Users owner = accessPolicy.currentUser();
        if (requestedUserId != null && !requestedUserId.equals(owner.getId())) {
            accessPolicy.requireAdmin();
            owner = findUserById(requestedUserId);
        }

        Orders order = new Orders();
        order.setUser(owner);
        order.setStatus(STATUS_PENDING);
        order.setTotal(0.0);
        order.setCreated_at(new Date());

        return ordersRepository.save(order);
    }

    /**
     * Adds a course to a pending order owned by the caller (or any order, for admins).
     * The price is the course's current price.
     */
    @Transactional
    public OrderItem addItemToOrder(Integer orderId, Integer courseId) {
        Orders order = findOrderById(orderId);
        requireOrderAccess(order);
        if (!STATUS_PENDING.equals(order.getStatus())) {
            throw new ValidationException("Items can only be added to a pending order");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        double price = course.getPrice();

        OrderItem item = new OrderItem();
        item.setOrders(order);
        item.setCourse(course);
        item.setPrice(price);

        order.addOrderItem(item);
        order.setTotal(order.getTotal() + price);
        ordersRepository.save(order);

        return orderItemRepository.save(item);
    }

    /**
     * Retrieves an order if the caller owns it (or is an admin).
     */
    @Transactional(readOnly = true)
    public Orders getOrderById(Integer id) {
        Orders order = findOrderById(id);
        requireOrderAccess(order);
        return order;
    }

    /**
     * Retrieves all orders of a user (that user or an admin).
     */
    @Transactional(readOnly = true)
    public List<Orders> getUserOrders(Integer userId) {
        accessPolicy.requireSelfOrAdmin(userId);
        return ordersRepository.findByUser(findUserById(userId));
    }

    /**
     * Retrieves all orders with a specific status (admin only).
     */
    @Transactional(readOnly = true)
    public List<Orders> getOrdersByStatus(String status) {
        accessPolicy.requireAdmin();
        return ordersRepository.findByStatus(status);
    }

    /**
     * Updates the status of an order (admin only).
     */
    @Transactional
    public Orders updateOrderStatus(Integer orderId, String status) {
        accessPolicy.requireAdmin();
        Orders order = findOrderById(orderId);
        order.setStatus(status);
        return ordersRepository.save(order);
    }

    /**
     * Retrieves all orders (admin only).
     */
    @Transactional(readOnly = true)
    public List<Orders> getAllOrders() {
        accessPolicy.requireAdmin();
        return ordersRepository.findAll();
    }

    private void requireOrderAccess(Orders order) {
        Integer ownerId = order.getUser() != null ? order.getUser().getId() : null;
        accessPolicy.requireSelfOrAdmin(ownerId);
    }

    private Users findUserById(Integer userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private Orders findOrderById(Integer orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }
}
