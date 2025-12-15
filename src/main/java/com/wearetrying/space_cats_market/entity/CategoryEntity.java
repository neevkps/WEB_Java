package com.wearetrying.space_cats_market.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter @Setter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq_gen")
    @SequenceGenerator(name = "categories_seq_gen", sequenceName = "categories_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
