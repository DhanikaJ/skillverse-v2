package com.skillverse.repository;

import com.skillverse.model.Orders;
import com.skillverse.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUser(Users user);
    List<Orders> findByStatus(String status);
}

