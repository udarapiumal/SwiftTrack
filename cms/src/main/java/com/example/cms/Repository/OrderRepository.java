package com.example.cms.Repository;

import com.example.cms.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByClient_ClientId(String clientId);
}
