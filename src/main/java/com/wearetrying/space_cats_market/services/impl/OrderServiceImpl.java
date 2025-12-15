package com.wearetrying.space_cats_market.services.impl;
import com.wearetrying.space_cats_market.domain.Order;
import com.wearetrying.space_cats_market.entity.OrderEntity;
import com.wearetrying.space_cats_market.repository.OrderRepository;
import com.wearetrying.space_cats_market.services.OrderService;
import com.wearetrying.space_cats_market.services.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Override
    @Transactional
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);

        if (entity.getTrackingNumber() == null) {
            entity.setTrackingNumber("ORD-" + java.util.UUID.randomUUID());
        }

        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setOrder(entity));
        }

        OrderEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Order findByTrackingNumber(String trackingNumber) {
        return repository.findByTrackingNumber(trackingNumber)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Order not found with tracking number: " + trackingNumber));
    }
}