package com.wearetrying.space_cats_market.services;
import com.wearetrying.space_cats_market.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(Long id);
    Customer save(Customer customer);
    void deleteById(Long id);
}
