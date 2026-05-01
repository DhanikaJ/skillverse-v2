package com.skillverse.service;

import com.skillverse.model.*;
import com.skillverse.repository.OrdersRepository;
import com.skillverse.repository.OrderItemRepository;
import com.skillverse.repository.UsersRepository;
import com.skillverse.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Order operations.
 * Provides functionality for creating orders, managing order items, and retrieving order information.
 */
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;

    public OrdersService(
            OrdersRepository ordersRepository,
            OrderItemRepository orderItemRepository,
            UsersRepository usersRepository,
            CourseRepository courseRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Creates a new order for a user.
     *
     * @param userId the ID of the user creating the order
     * @return the created Orders entity
     * @throws RuntimeException if the user is not found
     */
    @Transactional
    public Orders createOrder(Integer userId) {
        Users user = findUserById(userId);

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setTotal(0.0);
        order.setCreated_at(new Date());

        return ordersRepository.save(order);
    }

    /**
     * Adds an item to an existing order.
     *
     * @param orderId the ID of the order
     * @param courseId the ID of the course to add
     * @param price the price of the course
     * @return the created OrderItem entity
     * @throws RuntimeException if the order or course is not found
     */
    @Transactional
    public OrderItem addItemToOrder(Integer orderId, Integer courseId, Double price) {
        Orders order = findOrderById(orderId);
        Course course = findCourseById(courseId);

        OrderItem item = new OrderItem();
        item.setOrders(order);
        item.setCourse(course);
        item.setPrice(price);

        order.addOrderItem(item);

        Double newTotal = order.getTotal() + price;
        order.setTotal(newTotal);
        ordersRepository.save(order);

        return orderItemRepository.save(item);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the order ID
     * @return an Optional containing the Orders if found
     */
    public Optional<Orders> getOrderById(Integer id) {
        return ordersRepository.findById(id);
    }

    /**
     * Retrieves all orders for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of Orders entities
     * @throws RuntimeException if the user is not found
     */
    public List<Orders> getUserOrders(Integer userId) {
        Users user = findUserById(userId);
        return ordersRepository.findByUser(user);
    }

    /**
     * Retrieves all orders with a specific status.
     *
     * @param status the order status to filter by
     * @return a list of Orders entities matching the status
     */
    public List<Orders> getOrdersByStatus(String status) {
        return ordersRepository.findByStatus(status);
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId the ID of the order
     * @param status the new status
     * @return the updated Orders entity
     * @throws RuntimeException if the order is not found
     */
    @Transactional
    public Orders updateOrderStatus(Integer orderId, String status) {
        Orders order = findOrderById(orderId);
        order.setStatus(status);
        return ordersRepository.save(order);
    }

    /**
     * Retrieves all orders.
     *
     * @return a list of all Orders entities
     */
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    /**
     * Finds a user by ID.
     *
     * @param userId the user ID
     * @return the Users entity
     * @throws RuntimeException if the user is not found
     */
    private Users findUserById(Integer userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Finds a course by ID.
     *
     * @param courseId the course ID
     * @return the Course entity
     * @throws RuntimeException if the course is not found
     */
    private Course findCourseById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    }

    /**
     * Finds an order by ID.
     *
     * @param orderId the order ID
     * @return the Orders entity
     * @throws RuntimeException if the order is not found
     */
    private Orders findOrderById(Integer orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }
}

