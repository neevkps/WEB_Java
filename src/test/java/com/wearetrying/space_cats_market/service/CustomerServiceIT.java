package com.wearetrying.space_cats_market.service;

import com.wearetrying.space_cats_market.AbstractIt;
import com.wearetrying.space_cats_market.domain.Customer;
import com.wearetrying.space_cats_market.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CustomerServiceIT extends AbstractIt {

    @Autowired
    private CustomerService customerService;

    @Test
    @Transactional
    void shouldCreateCustomer() {
        Customer customer = new Customer();
        customer.setUsername("Han Solo");
        customer.setEmail("han@falcon.com");
        customer.setPassword("cosmocat123");

        Customer saved = customerService.save(customer);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Han Solo", saved.getUsername());
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenCustomerNotFound() {
        Assertions.assertThrows(RuntimeException.class,
                () -> customerService.findById(99999L));
    }
}