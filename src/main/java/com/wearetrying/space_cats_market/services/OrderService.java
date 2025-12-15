package com.wearetrying.space_cats_market.services;

import com.wearetrying.space_cats_market.domain.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(Long id);
    Order save(Order order);
    void deleteById(Long id);
    Order findByTrackingNumber(String trackingNumber);
}