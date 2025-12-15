package com.wearetrying.space_cats_market.service;

import com.wearetrying.space_cats_market.AbstractIt;
import com.wearetrying.space_cats_market.domain.*;
import com.wearetrying.space_cats_market.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public class OrderServiceIT extends AbstractIt {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Test
    @Transactional
    void shouldSaveAndFindOrderByNaturalId() {
        // Підготовка даних
        Category cat = categoryService.save(Category.builder().name("Ships").build());
        Customer customer = customerService.save(Customer.builder().username("Pilot").email("pilot@nasa.gov").build());
        Product product = productService.save(Product.builder()
                .name("Falcon 9").price(BigDecimal.valueOf(1000)).category(cat).stockQuantity(1).build());

        String tracking = "NATURAL-ID-12345";

        Order order = Order.builder()
                .customer(customer)
                .trackingNumber(tracking)
                .totalPrice(BigDecimal.valueOf(1000))
                .items(List.of(OrderItem.builder().product(product).quantity(1).priceAtPurchase(product.getPrice()).build()))
                .build();

        // Збереження (спрацює генерація UUID, якщо trackingNumber null, але тут ми передаємо явно)
        Order saved = orderService.save(order);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(tracking, saved.getTrackingNumber());

        // ТЕСТ NATURAL ID: Пошук за бізнес-ключем
        Order found = orderService.findByTrackingNumber(tracking);
        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getId(), found.getId());
    }

    @Test
    @Transactional
    void shouldDeleteOrder() {
        Customer customer = customerService.save(Customer.builder().username("Ghost").email("ghost@void.com").build());
        Order saved = orderService.save(Order.builder().customer(customer).totalPrice(BigDecimal.ZERO).build());

        orderService.deleteById(saved.getId());
        Assertions.assertThrows(RuntimeException.class, () -> orderService.findById(saved.getId()));
    }
}