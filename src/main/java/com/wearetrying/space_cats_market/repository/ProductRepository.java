package com.wearetrying.space_cats_market.repository;

import com.wearetrying.space_cats_market.entity.ProductEntity;
import com.wearetrying.space_cats_market.projection.TopProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
    boolean existsByName(String name);
    @Query("SELECT p.name AS name, SUM(oi.quantity) AS totalQuantity " +
            "FROM OrderItemEntity oi " +
            "JOIN oi.product p " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<TopProductProjection> findMostPurchasedProducts();
}
