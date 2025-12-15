package com.wearetrying.space_cats_market.services.impl;
import com.wearetrying.space_cats_market.domain.Customer;
import com.wearetrying.space_cats_market.entity.CustomerEntity;
import com.wearetrying.space_cats_market.repository.CustomerRepository;
import com.wearetrying.space_cats_market.services.CustomerService;
import com.wearetrying.space_cats_market.services.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}