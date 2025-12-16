package com.wearetrying.space_cats_market.service;

import com.wearetrying.space_cats_market.AbstractIt;
import com.wearetrying.space_cats_market.domain.Category;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.entity.CustomerEntity;
import com.wearetrying.space_cats_market.entity.OrderEntity;
import com.wearetrying.space_cats_market.entity.OrderItemEntity;
import com.wearetrying.space_cats_market.projection.TopProductProjection;
import com.wearetrying.space_cats_market.services.CategoryService;
import com.wearetrying.space_cats_market.services.ProductService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@WithMockUser(username = "admin", roles = "ADMIN")
public class ProductServiceIT extends AbstractIt {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EntityManager entityManager;

    private Category savedCategory;

    @BeforeEach
    void setUp() {
        savedCategory = categoryService.save(Category.builder().name("Cosmic Gear " + UUID.randomUUID()).build());
    }

    @Test
    @Transactional
    @DisplayName("Звіт для Комітету: Projection має повернути найпопулярніші товари")
    void shouldReturnTopPurchasedProductsViaProjection() {
        // Підготовка продуктів
        Product tuna = productService.save(createProduct("Cyber Tuna", savedCategory));
        Product nip = productService.save(createProduct("Space Catnip", savedCategory));

        // Створення клієнта та замовлення через EntityManager (обов'язково додаємо trackingNumber)
        CustomerEntity customer = new CustomerEntity();
        customer.setUsername("ExplorerCat");
        customer.setEmail("exp@galaxy.com");
        entityManager.persist(customer);

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setTrackingNumber("ORD-PROJ-001");
        order.setTotalPrice(BigDecimal.valueOf(200.0));
        entityManager.persist(order);

        // Додаємо айтеми: Catnip - 10 шт, Tuna - 2 шт
        createOrderItem(order, entityManager.find(com.wearetrying.space_cats_market.entity.ProductEntity.class, nip.getId()), 10);
        createOrderItem(order, entityManager.find(com.wearetrying.space_cats_market.entity.ProductEntity.class, tuna.getId()), 2);

        entityManager.flush();
        entityManager.clear();

        // Перевірка Projection
        List<TopProductProjection> report = productService.getTopPurchasedProducts();

        Assertions.assertFalse(report.isEmpty());
        Assertions.assertEquals("Space Catnip", report.get(0).getName());
        Assertions.assertEquals(10L, report.get(0).getTotalQuantity());
    }

    @Test
    @Transactional
    void shouldPerformFullProductCrud() {
        Product newProduct = createProduct("Laser Pointer", savedCategory);
        Product saved = productService.save(newProduct);
        Assertions.assertNotNull(saved.getId());

        List<Product> all = productService.findAll();
        Assertions.assertTrue(all.stream().anyMatch(p -> p.getName().equals("Laser Pointer")));

        Product found = productService.findById(saved.getId());
        Assertions.assertEquals("Laser Pointer", found.getName());

        productService.deleteById(saved.getId());
        Assertions.assertThrows(RuntimeException.class, () -> productService.findById(saved.getId()));
    }

    private void createOrderItem(OrderEntity order, com.wearetrying.space_cats_market.entity.ProductEntity product, int qty) {
        OrderItemEntity item = OrderItemEntity.builder()
                .order(order).product(product).quantity(qty).priceAtPurchase(product.getPrice()).build();
        entityManager.persist(item);
    }

    private Product createProduct(String name, Category cat) {
        return Product.builder().name(name).price(BigDecimal.TEN).stockQuantity(50).category(cat).build();
    }
}